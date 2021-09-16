package com.zj.init;


import com.zj.code.MsgDecoder;
import com.zj.code.MsgEncoder;
import com.zj.handle.ServerHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 17/05/2018 18:51
 * @since JDK 1.8
 */
public class ServerInitializer extends ChannelInitializer<Channel> {

    private final ServerHandle serverHandle = new ServerHandle() ;

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline()
        //拆包解码
                .addLast(new IdleStateHandler(10, 0, 0))
                .addLast(new ProtobufVarint32FrameDecoder())
                //11 秒没有向客户端发送消息就发生心跳

                .addLast(new MsgDecoder())
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new MsgEncoder())
                .addLast(serverHandle);
    }
}
