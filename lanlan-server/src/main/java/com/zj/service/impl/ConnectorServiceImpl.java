package com.zj.service.impl;

import com.zj.cache.ServerCache;
import com.zj.config.AppConfiguration;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;
import com.zj.service.ConnectorService;
import com.zj.util.IdWorker;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TransferService的职责
 * 1.负责接受transfer服务器的消息并转发给客户端
 * @author xiaozj
 */
@Slf4j
@Service
public class ConnectorServiceImpl implements ConnectorService {

    @Resource
    private ServerCache serverCache;

    @Resource
    private AppConfiguration appConfiguration;

    @Resource
    private Check.CheckMsg pongHeartBeat;

    @Override
    public void doChatToClientAndFlush(Chat.ChatMsg m) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) serverCache.getCache(m.getDestUser());
        if (ctx == null) {
            log.error("send chat to client failed",
                    m.getDestUser());
            return;
        }
        ctx.writeAndFlush(m);
    }

    @Override
    public void doSendAckToClientAndFlush(Ack.AckMsg m) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) serverCache.getCache(m.getDestUser());
        if (ctx == null) {
            log.error("send ack to client failed",
                    m.getDestUser());
            return;
        }
        ctx.writeAndFlush(m);
    }

    @Override
    public void sendGreetToTransfer(ChannelHandlerContext ctx) {
        Internal.InternalMsg greetMsg = buildGreetMsg();
        ctx.writeAndFlush(greetMsg);
    }

    @Override
    public void doSendFriendReqToClient(Internal.InternalMsg m) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) serverCache.getCache(m.getDestUser());
        if (ctx == null) {
            log.error("send friendReq to client failed",
                    m.getDestUser());
            return;
        }
        ctx.writeAndFlush(m);
    }

    @Override
    public void receiveHeartBeat(Check.CheckMsg m, ChannelHandlerContext ctx) {
        if (m.getMsgType().equals(Check.CheckMsg.MsgType.PING)) {
            ctx.writeAndFlush(pongHeartBeat);
        }
    }

    private Internal.InternalMsg buildGreetMsg() {
        Internal.InternalMsg greet = Internal.InternalMsg.newBuilder()
                .setId(IdWorker.genId())
                .setFrom(Internal.InternalMsg.Module.SERVER)
                .setDest(Internal.InternalMsg.Module.TRANSFER)
                .setCreateTime(System.currentTimeMillis())
                .setVersion(1)
                .setMsgType(Internal.InternalMsg.MsgType.GREET)
                .setFromUser(appConfiguration.getServerName())
                .build();
        return greet;
    }
}
