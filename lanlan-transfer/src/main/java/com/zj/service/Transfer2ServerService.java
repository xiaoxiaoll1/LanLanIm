package com.zj.service;

import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
public interface Transfer2ServerService {


    void doChatToServer(Chat.ChatMsg msg) throws ExecutionException;

    void doAckToServer(Ack.AckMsg msg) throws ExecutionException;

    void disConnect(ChannelHandlerContext ctx);

    void replyGreetFromServer(Internal.InternalMsg m, ChannelHandlerContext ctx);

    void doFriendReqToServer(Internal.InternalMsg msg) throws ExecutionException;

    void receiveHeartBeat(String fromUser);

    void sendHeartBeatToClient(ChannelHandlerContext ctx);
}
