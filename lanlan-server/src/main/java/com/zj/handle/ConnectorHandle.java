package com.zj.handle;

import com.google.protobuf.Message;
import com.zj.bo.RouteInfo;
import com.zj.cache.ServerContext;
import com.zj.factory.SpringBeanFactory;
import com.zj.parse.AbstractMsgParser;
import com.zj.parse.InternalParser;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;
import com.zj.server.ImConnector;
import com.zj.service.ConnectorService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

import static com.zj.parse.AbstractMsgParser.checkDest;
import static com.zj.parse.AbstractMsgParser.checkFrom;

/**
 * 这里不加@Sharable注解，太妙了！
 * 因为加了@Sharable注解的handler会多线程共享，就比如google的protobuf 粘包处理器就没加@Sharable，因为它内部有个缓存区，会引发线程安全问题
 * 而StringDecoder这种就无所谓，因为是无状态的
 * 因此网上哪些用static来解决自动注入问题的都是错的，因为那样就会导致handler共享
 * @author xiaozj
 */
@Slf4j
public class ConnectorHandle extends SimpleChannelInboundHandler<Message> {


    private ServerContext serverContext;


    private ImConnector imConnector;


    private ConnectorService connectorService;

    public ConnectorHandle() {
        serverContext = SpringBeanFactory.getBean(ServerContext.class);
        imConnector = SpringBeanFactory.getBean(ImConnector.class);
        connectorService = SpringBeanFactory.getBean(ConnectorService.class);
    }

    private final FromTransferParser fromTransferParser = new FromTransferParser();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        serverContext.getCtxSet().add(ctx);
        connectorService.sendGreetToTransfer(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {

        log.debug("[transfer] get msg: {}", msg.toString());

        checkFrom(msg, Internal.InternalMsg.Module.TRANSFER);
        checkDest(msg, Internal.InternalMsg.Module.SERVER);

        fromTransferParser.parse(msg, ctx);
    }


    /**
     * 如果transfer主动断开连接，server端通过ctx取出对应的ip和port调用execute()将重连封装成一个任务放入
     * nioLoop的任务队列中，尝试重连
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        serverContext.removeCtx(ctx);
        // TODO 服务端重连
        InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        int port = ipSocket.getPort();
        ctx.executor().execute(() -> {
            imConnector.connectToTransfer(new RouteInfo(clientIp, port));
        });
        log.info("客户端ip地址：{}",clientIp);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage(), cause);
    }


    class FromTransferParser extends AbstractMsgParser {

        @Override
        public void registerParsers() {
            InternalParser parser = new InternalParser(2);
            parser.register(Internal.InternalMsg.MsgType.FRIENDReq, (m, ctx) -> connectorService.doSendFriendReqToClient(m));
            register(Chat.ChatMsg.class, (m, ctx) -> connectorService.doChatToClientAndFlush(m));
            register(Ack.AckMsg.class, (m, ctx) -> connectorService.doSendAckToClientAndFlush(m));
            register(Internal.InternalMsg.class, parser.generateFun());
            register(Check.CheckMsg.class, (m, ctx) -> connectorService.receiveHeartBeat(m, ctx));
        }
    }
}
