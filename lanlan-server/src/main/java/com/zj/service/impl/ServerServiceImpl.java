package com.zj.service.impl;

import com.zj.cache.HeartBeatCache;
import com.zj.cache.ServerCache;
import com.zj.cache.ServerContext;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;
import com.zj.service.ServerService;
import com.zj.util.IdWorker;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * connectorService的职责
 * 1.发送delivered的ack消息给client
 * 2.发送chat到transfer
 * @author xiaozj
 */
@Slf4j
@Service
public class ServerServiceImpl implements ServerService {

    @Resource
    private ServerCache serverCache;

    @Resource
    private ServerContext serverContext;


    @Resource
    private Check.CheckMsg pingHeartBeat;

    @Resource
    private HeartBeatCache heartBeatCache;

    /**
     * 将client传过来的chat消息传送给transfer
     * @param msg
     * @throws ExecutionException
     */
    @Override
    public void doChatToTransferAndFlush(Chat.ChatMsg msg) {
        ChannelHandlerContext ctx = getChannelHandlerContext();
        ctx.writeAndFlush(msg);
        doSendAckToClientAndFlush(getDelivered(msg));
    }


    private void doSendAckToClientAndFlush(Ack.AckMsg ackMsg) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) serverCache.getCache(ackMsg.getFromUser());
        if (ctx == null) {
            log.error("[send msg to client] not one the machine, userId: {}, connectorId: {}",
                    ackMsg.getDestUser());
            return;
        }
        ctx.writeAndFlush(ackMsg);
    }


    /**
     * 将client传递过来的确认消息发送到transfer
     * @param ackMsg
     * @throws ExecutionException
     */
    @Override
    public void doSendAckToTransferAndFlush(Ack.AckMsg ackMsg) throws ExecutionException {
        ChannelHandlerContext ctx = getChannelHandlerContext();
        ctx.writeAndFlush(ackMsg);
    }

    /**
     * 将用户与ctx和ctx与用户的对应关系存在缓存中
     * @param m
     * @param ctx
     */
    @Override
    public void userConnect(Internal.InternalMsg m, ChannelHandlerContext ctx) {
        heartBeatCache.refresh(m.getFromUser());
        serverCache.addCache(m.getFromUser(), ctx);
        serverCache.addReverseCache(ctx, m.getFromUser());
    }

    @Override
    public void sendHeartBeatToClient(ChannelHandlerContext ctx) throws InterruptedException {
        /**
         * 这里检查发送心跳没有回应的次数
         */
        String clientId = serverCache.getReverseCache(ctx);
        log.info("发送心跳包给{}", clientId);
        if (heartBeatCache.deadClient(clientId)) {
            log.info("客户端{}长时间没有回应心跳，强行关闭连接", clientId);
            ctx.channel().close().sync();
        }
        else {
            ctx.writeAndFlush(pingHeartBeat);
        }
    }

    @Override
    public void disConnect(ChannelHandlerContext ctx) {
        String clientId = serverCache.getReverseCache(ctx);
        log.info("断开与客户端{}的连接", clientId);
        ctx.channel().close();
        serverCache.remove(clientId);
        serverCache.removeReverse(ctx);
    }

    @Override
    public void receiveHeartBeat(String fromUser) {
        heartBeatCache.refresh(fromUser);
    }

    @Override
    public void doSendFriendReqToTransfer(Internal.InternalMsg m) {
        ChannelHandlerContext ctx = getChannelHandlerContext();
        ctx.writeAndFlush(m);
    }

    /**
     * 随机选取一个transfer服务器
     * @return
     */
    private ChannelHandlerContext getChannelHandlerContext() {
        Set<ChannelHandlerContext> ctxSet = serverContext.getCtxSet();
        ArrayList<ChannelHandlerContext> list = new ArrayList<>(ctxSet);
        int randomIndex = new Random().nextInt(list.size());
        ChannelHandlerContext ctx = list.get(randomIndex);
        return ctx;
    }

    /**
     * 服务端收到消息会响应一个已送达的消息给客户端，设置ackId为消息的id
     * @param msg
     * @return
     */
    private Ack.AckMsg getDelivered(Chat.ChatMsg msg) {
        return Ack.AckMsg.newBuilder()
                .setId(IdWorker.genId())
                .setVersion(1)
                .setFromUser(msg.getFromUser())
                .setDestUser(msg.getDestUser())
                .setCreateTime(System.currentTimeMillis())
                .setMsgType(Ack.AckMsg.MsgType.DELIVERED)
                .setAckMsgId(msg.getId())
                .build();
    }


}
