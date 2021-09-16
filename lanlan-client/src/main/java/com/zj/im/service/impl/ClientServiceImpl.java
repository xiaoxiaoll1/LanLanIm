package com.zj.im.service.impl;


import com.zj.im.api.UserApi;
import com.zj.im.cache.ClientCache;
import com.zj.im.cache.MessageCache;
import com.zj.im.config.AppConfiguration;
import com.zj.im.service.ClientService;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.util.IdWorker;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    @Resource
    private ClientCache cache;

    @Resource
    private UserApi userApi;

    @Resource
    private AppConfiguration appConfiguration;

    @Resource
    private MessageCache messageCache;

    @Resource
    private Check.CheckMsg pongHeartBeat;

    @Override
    public void read(Chat.ChatMsg chatMsg) throws ExecutionException {
        /**
         * 收到消息时，将消息的id存入一个线程安全的set中，防止重复消费
         * 然后响应一个Ack的已读消息
         * ack的id必须对应chat的id
         */
        if (!messageCache.getSet().contains(chatMsg.getId())) {
            messageCache.getSet().add(chatMsg.getId());
            log.info("收到新消息,来自{}发送的{}", chatMsg.getFromUser(), chatMsg.getMsgBody());
        }
        Ack.AckMsg ackMsg = hasRead(chatMsg.getFromUser(), chatMsg.getId());
        ChannelHandlerContext ctx = (ChannelHandlerContext) cache.getCache("ctx");
        ctx.writeAndFlush(ackMsg);
    }

    /**
     * 这里Chat对应的ack消息有两种
     * @param id chatMsg msg id
     */
    @Override
    public void receiveDeliveredAck(long id) {
        messageCache.receiveDelivered(id);
    }

    @Override
    public void receiveReadAck(long id) {
        messageCache.receiveAck(id);
    }


    /**
     * 收到好友添加成功的请求时在当前的好友缓存中添加
     * @param friendName
     */
    @Override
    public void flushFriendSet(String friendName) {
        Set<String> friends = (Set<String>) cache.getCache("friends");
        friends.add(friendName);
        cache.addCache("friends", friends);
    }

    /**
     * 收到好友删除时在当前的好友缓存中移除
     * @param friendName
     */
    @Override
    public void deleteFriendSet(String friendName) {
        Set<String> friends = (Set<String>) cache.getCache("friends");
        friends.remove(friendName);
        cache.addCache("friends", friends);
    }


    @Override
    public void dealWithFriendReq(ChannelHandlerContext ctx, String friendName) {
        Ack.AckMsg ackMsg = userApi.replyToFriendReq(friendName, true);
        ctx.writeAndFlush(ackMsg);
    }

    @Override
    public void heartBeat(Check.CheckMsg m, ChannelHandlerContext ctx) {
        if (m.getMsgType().equals(Check.CheckMsg.MsgType.PING)) {
            ctx.writeAndFlush(pongHeartBeat);
        }
    }


    private Ack.AckMsg hasRead(String destUser, long msgId) {
        return Ack.AckMsg.newBuilder()
                .setId(IdWorker.genId())
                .setVersion(1)
                .setFromUser(appConfiguration.getUserName())
                .setDestUser(destUser)
                .setCreateTime(System.currentTimeMillis())
                .setAckMsgId(msgId)
                .setMsgType(Ack.AckMsg.MsgType.READ)
                .build();
    }
}
