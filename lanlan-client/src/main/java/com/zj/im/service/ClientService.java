package com.zj.im.service;

import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
public interface ClientService {


        /**
         * read a msg
         *
         * @param chatMsg
         */
        void read(Chat.ChatMsg chatMsg) throws ExecutionException;

        /**
         * do when a msg has been delivered
         *
         * @param id chatMsg msg id
         */
        void receiveDeliveredAck(long id);

        /**
         * do when a msg has been read
         *
         * @param id chatMsg msg id
         */
        void receiveReadAck(long id);


        void flushFriendSet(String friendName) throws ExecutionException;

        void deleteFriendSet(String friendName) throws ExecutionException;

        void dealWithFriendReq(ChannelHandlerContext ctx, String friendName);

    void heartBeat(Check.CheckMsg m, ChannelHandlerContext ctx);
}
