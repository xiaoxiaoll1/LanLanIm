package com.zj.im.api;

import com.zj.im.cache.ClientCache;
import com.zj.im.cache.MessageCache;
import com.zj.im.cache.MessageRecord;
import com.zj.im.config.AppConfiguration;
import com.zj.protobuf.Chat;
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
public class ChatApi {


    @Resource
    private AppConfiguration appConfiguration;

    @Resource
    private ClientCache cache;

    @Resource
    private MessageCache messageCache;

    /**
     * 发送消息前会检查好友列表中是否包含目标
     * 然后会将消息存入一个concurrentHashmap，key就是消息Id，value是messageRecord对象，包含了消息内容，重发次数，是否送达
     * @param destUser
     * @param text
     */
    public void send(String destUser, String text) {
        Set<String> friends = (Set<String>) cache.getCache("friends");
        if (!friends.contains(destUser)) {
            log.error("{}不是好友!", destUser);
            return;
        }
        Chat.ChatMsg chatMsg = Chat.ChatMsg.newBuilder()
                .setId(IdWorker.genId())
                .setFromUser(appConfiguration.getUserName())
                .setDestUser(destUser)
                .setFromModule(Chat.ChatMsg.Module.CLIENT)
                .setCreateTime(System.currentTimeMillis())
                .setVersion(1)
                .setMsgBody(text)
                .build();

        ChannelHandlerContext ctx = (ChannelHandlerContext) cache.getCache("ctx");
        ctx.writeAndFlush(chatMsg);
        messageCache.waitForAck(chatMsg.getId(), new MessageRecord(chatMsg, 0, false));
    }

    /**
     * 仅用于重发消息
     * @param msg
     */
    public void resendMsg(Chat.ChatMsg msg) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) cache.getCache("ctx");
        ctx.writeAndFlush(msg);
    }



}
