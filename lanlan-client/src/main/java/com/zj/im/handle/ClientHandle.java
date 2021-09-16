package com.zj.im.handle;

import com.google.protobuf.Message;
import com.zj.im.api.UserApi;
import com.zj.im.cache.ClientCache;
import com.zj.im.client.ImClient;
import com.zj.im.service.ClientService;
import com.zj.im.factory.SpringBeanFactory;
import com.zj.parse.AbstractMsgParser;
import com.zj.parse.AckParser;
import com.zj.parse.InternalParser;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Check;
import com.zj.protobuf.Internal;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author xiaozj
 */

@Slf4j
public class ClientHandle extends SimpleChannelInboundHandler<Message> {

    /**
     * 由于加载顺序的原因，这里不能自动注入，需要springFactory手动注入
     */
    private UserApi userApi;

    private ImClient imClient;

    private ClientService clientService;

    public ClientHandle() {
        userApi = SpringBeanFactory.getBean(UserApi.class);
        imClient = SpringBeanFactory.getBean(ImClient.class);
        clientService = SpringBeanFactory.getBean(ClientService.class);
    }

    private final FromConnectorParser fromConnectorParser = new FromConnectorParser();

    /**
     * 刚建立连接时将ctx加入cache
     * 并发送greet消息，greet消息包含了用户名，这样服务端收到消息时可以做用户名和ctx的映射
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ClientCache clientCache = SpringBeanFactory.getBean("clientCache", ClientCache.class);
        clientCache.addCache("ctx", ctx);
        //客户端和服务端建立连接时调用
        log.info("cim server connect success!");
        userApi.userLoginInit();
    }

    /**
     * 如果服务端主动断开连接，比如心跳原因，客户端会先异步尝试一次重连，失败则使用token重新登录
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        imClient.reconnect(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {

        log.debug("[client] get msg: {}", msg.toString());

        AbstractMsgParser.checkFrom(msg, Internal.InternalMsg.Module.SERVER);
        AbstractMsgParser.checkDest(msg, Internal.InternalMsg.Module.CLIENT);

        fromConnectorParser.parse(msg, ctx);

    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常时断开连接
        cause.printStackTrace() ;
        ctx.close() ;
    }



    class FromConnectorParser extends AbstractMsgParser {

        @Override
        public void registerParsers() {
            InternalParser internalParser = new InternalParser(2);
            internalParser.register(Internal.InternalMsg.MsgType.FRIENDReq, (m, ctx) -> clientService.dealWithFriendReq(ctx, m.getFromUser()));

            AckParser ackParser = new AckParser(4);
            ackParser.register(Ack.AckMsg.MsgType.DELIVERED, (m, ctx) -> clientService.receiveDeliveredAck(m.getAckMsgId()));
            ackParser.register(Ack.AckMsg.MsgType.READ, (m, ctx) -> clientService.receiveReadAck(m.getAckMsgId()));
            ackParser.register(Ack.AckMsg.MsgType.FRIENDAGREE, (m, ctx) -> clientService.flushFriendSet(m.getFromUser()));
            ackParser.register(Ack.AckMsg.MsgType.FRIENDDELETE, (m, ctx) -> clientService.deleteFriendSet(m.getFromUser()));
            //ackParser.register(Check.CheckMsg.MsgType.PONG);

            /**
             * 前面调用fromConnectorParser.parse(msg, ctx)是出发的是注册在
             * Map<Class<? extends Message>, ImBiConsumer<? extends Message, ChannelHandlerContext>> parserMap中的方法
             * 而调用generateFun()是调用Map<E, ImBiConsumer<M, ChannelHandlerContext>> parseMap中的方法, M为type
             */
            register(Chat.ChatMsg.class, (m, ctx) -> clientService.read(m));
            register(Ack.AckMsg.class, ackParser.generateFun());
            register(Internal.InternalMsg.class, internalParser.generateFun());
            register(Check.CheckMsg.class, (m, ctx) -> clientService.heartBeat(m, ctx));
        }
    }
}
