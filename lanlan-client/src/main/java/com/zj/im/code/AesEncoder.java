package com.zj.im.code;


import com.google.protobuf.Message;
import com.zj.im.config.AppConfiguration;
import com.zj.im.factory.SpringBeanFactory;
import com.zj.protobuf.Chat;
import com.zj.util.CryptUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Date: 2019-05-11
 * Time: 12:49
 *
 * @author yrw
 */
@Slf4j
public class AesEncoder extends MessageToMessageEncoder<Message> {


    private AppConfiguration appConfiguration;

    /**
     * 聊天消息的消息体才用aesKey加密
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        try {
            if (msg instanceof Chat.ChatMsg) {
                appConfiguration = SpringBeanFactory.getBean(AppConfiguration.class);
                Chat.ChatMsg cm = (Chat.ChatMsg) msg;
                String aesKey = appConfiguration.getAesKey();
                String msgBody = cm.getMsgBody();
                String encodeBody = CryptUtil.aesEncode(aesKey, msgBody);

                Chat.ChatMsg encodeMsg = Chat.ChatMsg.newBuilder().mergeFrom(cm)
                    .setMsgBody(encodeBody).build();

                log.info("[encode] encode message: {}", encodeMsg);

                out.add(encodeMsg);
            } else {
                out.add(msg);
            }
        } catch (Exception e) {
            log.error("[encode] has error", e);
        }
    }
}
