package com.zj.code;

import com.google.protobuf.Message;
import com.zj.message.MsgTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 2019-04-14
 * Time: 16:35
 *
 * @author xiaozj
 */
@ChannelHandler.Sharable
@Slf4j
public class MsgEncoder extends MessageToByteEncoder<Message> {
    private static final Logger logger = LoggerFactory.getLogger(MsgEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        ByteBuf buf = null;
        try {
            /**
             * 先写一个4字节的int字段，保存该消息类对应的code
             */
            byte[] bytes = msg.toByteArray();
            int code = MsgTypeEnum.getByClass(msg.getClass()).getCode();
            int length = bytes.length;

            buf = Unpooled.buffer(4 + length);
            buf.writeInt(code);
            buf.writeBytes(bytes);
            out.writeBytes(buf);

            log.info("发送消息, 远程主机: {}, 消息长度 {}, 消息code: {}", ctx.channel().remoteAddress(), length, code);
        } catch (Exception e) {
            logger.error("消息encode出现错误", e);
        }
        finally {
            buf.release();
        }
    }
}
