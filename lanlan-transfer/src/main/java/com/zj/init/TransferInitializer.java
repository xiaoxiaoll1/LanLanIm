package com.zj.init;

import com.zj.code.MsgDecoder;
import com.zj.code.MsgEncoder;
import com.zj.handle.TransferHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 卷卷卷卷卷卷卷卷卷
 * @author xiaozj
 */

public class TransferInitializer extends ChannelInitializer<Channel> {


    @Override
    protected void initChannel(Channel channel) throws Exception {

        channel.pipeline()
                .addLast(new IdleStateHandler(30, 0, 0))
                .addLast(new ProtobufVarint32FrameDecoder())

                .addLast(new MsgDecoder())

                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new MsgEncoder())
                .addLast(new TransferHandle());
    }
}
