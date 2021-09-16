package com.zj.service.impl;

import com.zj.cache.HeartBeatCache;
import com.zj.cache.TransferCache;
import com.zj.common.redis.RedisUtils;
import com.zj.constant.CommonCacheKey;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;
import com.zj.service.RabbitService;
import com.zj.service.Transfer2ServerService;
import com.zj.util.CryptUtil;
import com.zj.util.IdWorker;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
@Slf4j
@Service
public class Transfer2ServerServiceImpl implements Transfer2ServerService {

    @Resource
    private TransferCache transferCache;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RabbitService rabbitService;

    @Resource
    private HeartBeatCache heartBeatCache;

    @Resource
    private Check.CheckMsg pingHeartBeat;

    /**
     * transfer接受到server端发送过来的chat消息后，先通过redis查找发送者是否在线，并获取对应的aesKey
     * 然后将消息解密，如果在线，那么先访问redis获取接收者的aesKey和对应的serverId，通过本地缓存找到对应的ctx，加密后发送给server
     * 如果离线，直接将消息放入rabbitmq离线队列，同时发送ack消息给发送者对应的server。
     * 此外，所有的消息都会放入rabbitmq的历史消息队列
     * 在rest服务端，离线消息和离线的好友请求会放在redis中的list。历史消息会存在是数据库中，这种方式还保证了幂等性
     * 数据库消息id是唯一的，redis取离线消息出来时会做id去重处理
     * @param msg
     * @throws ExecutionException
     */
    @Override
    public void doChatToServer(Chat.ChatMsg msg) throws ExecutionException {
        String status = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getDestUser(), CommonCacheKey.USER_STATUS_KEY);
        String deKey = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getFromUser(), CommonCacheKey.AES_KEY);

        String decodeStr = CryptUtil.aesDecode(msg.getMsgBody(), deKey);
        Chat.ChatMsg deCodeMsg = Chat.ChatMsg.newBuilder().mergeFrom(msg)
                .setMsgBody(decodeStr).build();
        if (StringUtils.isBlank(status) || !"online".equals(status)) {
            String enKey = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getDestUser(), CommonCacheKey.AES_KEY);
            String connectorId = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getDestUser(), CommonCacheKey.CONNECTOR_KEY);
            String encodeStr = CryptUtil.aesEncode(decodeStr, enKey);
            Chat.ChatMsg enCodeMsg = Chat.ChatMsg.newBuilder().mergeFrom(msg)
                    .setMsgBody(encodeStr).build();
            ChannelHandlerContext ctx = (ChannelHandlerContext) this.transferCache.getCache(connectorId);
            ctx.writeAndFlush(enCodeMsg);
        } else {
            rabbitService.sendOfflineMsg(deCodeMsg);
            Ack.AckMsg hasReadMsg = buildHasReadMsg(msg);
            String connectorId = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getFromUser(), CommonCacheKey.CONNECTOR_KEY);
            ChannelHandlerContext ctx = (ChannelHandlerContext) this.transferCache.getCache(connectorId);
            ctx.writeAndFlush(hasReadMsg);
        }
        rabbitService.sendAllMsg(deCodeMsg);
    }

    @Override
    public void doAckToServer(Ack.AckMsg msg) throws ExecutionException {
        String status = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getDestUser(), CommonCacheKey.USER_STATUS_KEY);
        if (StringUtils.isNotBlank(status) && !"online".equals(status)) {
            String connectorId = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getDestUser(), CommonCacheKey.CONNECTOR_KEY);
            ChannelHandlerContext ctx = (ChannelHandlerContext) this.transferCache.getCache(connectorId);
            ctx.writeAndFlush(msg);
        }
    }

    @Override
    public void disConnect(ChannelHandlerContext ctx) {
        String clientId = transferCache.getReverseCache(ctx);
        log.info("断开与server客户端{}的连接", clientId);
        ctx.channel().close();
        transferCache.remove(clientId);
        transferCache.removeReverse(ctx);
    }

    /**
     * 保存所有serverId与ctx的映射关系
     * @param m
     * @param ctx
     */
    @Override
    public void replyGreetFromServer(Internal.InternalMsg m, ChannelHandlerContext ctx) {
        String serverId = m.getFromUser();
        heartBeatCache.refresh(serverId);
        transferCache.addCache(serverId, ctx);
        transferCache.addReverseCache(ctx, serverId);
    }

    @Override
    public void doFriendReqToServer(Internal.InternalMsg msg) throws ExecutionException {
        String status = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getDestUser(), CommonCacheKey.USER_STATUS_KEY);

        if (StringUtils.isBlank(status) || !"online".equals(status)) {
            String connectorId = (String) redisUtils.hget(CommonCacheKey.HASH_PREFIX + msg.getDestUser(), CommonCacheKey.CONNECTOR_KEY);

            ChannelHandlerContext ctx = (ChannelHandlerContext) this.transferCache.getCache(connectorId);
            ctx.writeAndFlush(msg);
        } else {
            rabbitService.sendFriendReq(msg);
        }
    }

    @Override
    public void receiveHeartBeat(String fromUser) {
        heartBeatCache.refresh(fromUser);
    }

    @Override
    public void sendHeartBeatToClient(ChannelHandlerContext ctx) {
        /**
         * 这里检查发送心跳没有回应的次数
         */
        String clientId = transferCache.getReverseCache(ctx);
        log.info("发送心跳包给{}", clientId);
        if (heartBeatCache.deadClient(clientId)) {
            log.info("客户端{}长时间没有回应心跳，强行关闭连接", clientId);
            ctx.channel().close();
        }
        else {
            ctx.writeAndFlush(pingHeartBeat);
        }
    }


    /**
     * 当检测到对应客户端离线时，发送给服务端已读消息，避免重复发送
     * @param msg
     * @return
     */
    private Ack.AckMsg buildHasReadMsg(Chat.ChatMsg msg) {
        return Ack.AckMsg.newBuilder()
                .setId(msg.getId())
                .setVersion(1)
                .setFromUser(msg.getFromUser())
                .setDestUser(msg.getDestUser())
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(Ack.AckMsg.MsgType.READ)
                .setAckMsgId(msg.getId())
                .build();
    }
}
