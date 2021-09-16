package com.zj.handle;

import com.google.protobuf.Message;
import com.zj.factory.SpringBeanFactory;
import com.zj.parse.AbstractMsgParser;
import com.zj.parse.InternalParser;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;
import com.zj.service.Transfer2ServerService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.zj.parse.AbstractMsgParser.*;

/**
 * @author xiaozj
 */
@Slf4j
public class TransferHandle extends SimpleChannelInboundHandler<Message> {


    private Transfer2ServerService transferService;

    private final FromConnectorParser fromConnectorParser = new FromConnectorParser();

    public TransferHandle() {
        transferService = SpringBeanFactory.getBean(Transfer2ServerService.class);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        transferService.disConnect(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        log.debug("[transfer] get msg: {}", msg.toString());

        checkFrom(msg, Internal.InternalMsg.Module.SERVER);
        checkDest(msg, Internal.InternalMsg.Module.TRANSFER);

        fromConnectorParser.parse(msg, ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                transferService.sendHeartBeatToClient(ctx);
            }
        }
    }


    class FromConnectorParser extends AbstractMsgParser {

        @Override
        public void registerParsers() {

            InternalParser internalParser = new InternalParser(2);
            internalParser.register(Internal.InternalMsg.MsgType.GREET, (m, ctx) -> transferService.replyGreetFromServer(m, ctx));
            internalParser.register(Internal.InternalMsg.MsgType.FRIENDReq, (m, ctx) -> transferService.doFriendReqToServer(m));
            /**
             * 这里由于chat msg的处理逻辑都一样，因此不用generateFun()
             */
            register(Chat.ChatMsg.class, (m, ctx) -> transferService.doChatToServer(m));
            register(Ack.AckMsg.class, (m, ctx) -> transferService.doAckToServer(m));
            register(Internal.InternalMsg.class, internalParser.generateFun());
            register(Check.CheckMsg.class, (m, ctx) -> transferService.receiveHeartBeat(m.getFromUser()));
        }
    }
}
