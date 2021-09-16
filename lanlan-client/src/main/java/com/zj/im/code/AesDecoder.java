package com.zj.im.code;


import com.google.protobuf.Message;
import com.zj.im.config.AppConfiguration;
import com.zj.im.factory.SpringBeanFactory;
import com.zj.protobuf.Chat;
import com.zj.util.CryptUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Date: 2019-05-11
 * Time: 13:34
 *
 * @author yrw
 */
@Slf4j
public class AesDecoder extends MessageToMessageDecoder<Message> {

    private AppConfiguration appConfiguration;

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        if (msg instanceof Chat.ChatMsg) {
            appConfiguration = SpringBeanFactory.getBean(AppConfiguration.class);
            Chat.ChatMsg cm = (Chat.ChatMsg) msg;
            String aesKey = appConfiguration.getAesKey();
            String msgBody = cm.getMsgBody();
            String decodeBody = CryptUtil.aesDecode(aesKey, msgBody);

            Chat.ChatMsg decodeMsg = Chat.ChatMsg.newBuilder().mergeFrom(cm)
                .setMsgBody(decodeBody).build();

            out.add(decodeMsg);
            log.info("用户{}收到消息:{}", appConfiguration.getUserName(), decodeBody);
        } else {
            out.add(msg);
        }
    }
}
