package com.zj.handle;

import com.google.protobuf.Message;
import com.zj.factory.SpringBeanFactory;
import com.zj.parse.AbstractMsgParser;
import com.zj.parse.InternalParser;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;
import com.zj.service.ServerService;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 17/05/2018 18:52
 * @since JDK 1.8
 */
@Slf4j
public class ServerHandle extends SimpleChannelInboundHandler<Message> {


    private ServerService serverService;

    public ServerHandle() {
        serverService = SpringBeanFactory.getBean(ServerService.class);
    }

    private final FromClientParser fromClientParser = new FromClientParser();



    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        serverService.disConnect(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                serverService.sendHeartBeatToClient(ctx);
            }
        }
    }


    /**
     * 收到消息时先检查发送模块和接受模块，再利用策略模式来完成对应消息的处理，此时已经知道了消息的类型，直接获取对应的consumer调用accept方法
     * @param ctx
     * @param msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {

        log.debug("[client] get msg: {}", msg.toString());

        AbstractMsgParser.checkFrom(msg, Internal.InternalMsg.Module.CLIENT);
        AbstractMsgParser.checkDest(msg, Internal.InternalMsg.Module.SERVER);

        fromClientParser.parse(msg, ctx);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
    }


    /**
     * 这里的ctx是与客户端交互的，逻辑较为简单
     */
    class FromClientParser extends AbstractMsgParser {

        @Override
        public void registerParsers() {
            InternalParser parser = new InternalParser(3);
            parser.register(Internal.InternalMsg.MsgType.GREET,
                    (m, ctx) -> serverService.userConnect(m, ctx));
            parser.register(Internal.InternalMsg.MsgType.FRIENDReq, (m, ctx) -> {
                serverService.doSendFriendReqToTransfer(m);
            });

            register(Chat.ChatMsg.class, (m, ctx) -> serverService.doChatToTransferAndFlush(m));
            register(Ack.AckMsg.class, (m, ctx) -> serverService.doSendAckToTransferAndFlush(m));
            register(Internal.InternalMsg.class, parser.generateFun());
            register(Check.CheckMsg.class, (m, ctx) -> serverService.receiveHeartBeat(m.getFromUser()));
        }
    }
}
