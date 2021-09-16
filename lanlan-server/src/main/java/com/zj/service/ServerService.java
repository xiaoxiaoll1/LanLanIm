package com.zj.service;

import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
public interface ServerService {


    void doChatToTransferAndFlush(Chat.ChatMsg msg) throws ExecutionException;



    void doSendAckToTransferAndFlush(Ack.AckMsg ackMsg) throws ExecutionException;


    void userConnect(Internal.InternalMsg m, ChannelHandlerContext ctx);

    void sendHeartBeatToClient(ChannelHandlerContext ctx) throws InterruptedException;

    void disConnect(ChannelHandlerContext ctx);

    void receiveHeartBeat(String fromUser);

    void doSendFriendReqToTransfer(Internal.InternalMsg m);
}
