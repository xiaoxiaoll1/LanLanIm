package com.zj.service;

import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
public interface ConnectorService {


    void doChatToClientAndFlush(Chat.ChatMsg m) throws ExecutionException;

    void doSendAckToClientAndFlush(Ack.AckMsg m) throws ExecutionException;

    void sendGreetToTransfer(ChannelHandlerContext ctx);

    void doSendFriendReqToClient(Internal.InternalMsg m);

    void receiveHeartBeat(Check.CheckMsg m, ChannelHandlerContext ctx);
}
