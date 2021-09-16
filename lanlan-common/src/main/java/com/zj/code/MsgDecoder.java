package com.zj.code;

import com.google.protobuf.Message;
import com.zj.message.ParseService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author xiaozj
 */

public class MsgDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MsgDecoder.class);

    private ParseService parseService;

    public MsgDecoder() {
        this.parseService = new ParseService();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final int length = in.readableBytes();


        int code = in.readInt();

        ByteBuf byteBuf = Unpooled.buffer(length - 4);
        try {
            in.readBytes(byteBuf);

            byte[] body = byteBuf.array();

            Message msg = parseService.getMsgByCode(code, body);
            out.add(msg);

            logger.debug("[IM msg decoder]received message: content length {}, msgTypeCode: {}", length, code);
        } finally {
            byteBuf.release();
        }


    }
}
