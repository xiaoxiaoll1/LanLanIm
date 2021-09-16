package com.zj.im.init;

import com.zj.code.MsgDecoder;
import com.zj.code.MsgEncoder;
import com.zj.im.code.AesDecoder;
import com.zj.im.code.AesEncoder;
import com.zj.im.handle.ClientHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 23/02/2018 22:47
 * @since JDK 1.8
 */
public class ClientHandleInitializer extends ChannelInitializer<Channel> {

    private final ClientHandle handler = new ClientHandle();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();

        //拆包解码
        p.addLast(new ProtobufVarint32FrameDecoder());
        //in
        p.addLast("MsgDecoder", new MsgDecoder());
        p.addLast("AesDecoder", new AesDecoder());


        //拆包编码
        p.addLast(new ProtobufVarint32LengthFieldPrepender());

        p.addLast("MsgEncoder", new MsgEncoder());
        //out
        p.addLast("AesEncoder", new AesEncoder());


        p.addLast("ClientConnectorHandler", handler);
    }
}
