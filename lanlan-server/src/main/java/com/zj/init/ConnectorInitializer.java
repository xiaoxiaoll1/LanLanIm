package com.zj.init;

import com.zj.code.MsgDecoder;
import com.zj.code.MsgEncoder;
import com.zj.handle.ConnectorHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author xiaozj
 */
public class ConnectorInitializer extends ChannelInitializer<Channel> {
    private ConnectorHandle connectorHandle = new ConnectorHandle() ;

    @Override
    protected void initChannel(Channel ch) {

        ch.pipeline()

                .addLast(new ProtobufVarint32FrameDecoder())
                //11 秒没有向客户端发送消息就发生心跳

                .addLast(new MsgDecoder())
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new MsgEncoder())
                .addLast(connectorHandle);
    }
}
