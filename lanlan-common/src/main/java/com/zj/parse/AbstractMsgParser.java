package com.zj.parse;


import com.google.protobuf.Message;
import com.zj.exception.BusinessException;
import com.zj.function.ImBiConsumer;
import com.zj.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xiaozj
 */
public abstract class AbstractMsgParser {
    private Logger logger = LoggerFactory.getLogger(AbstractMsgParser.class);

    /**
     * map的key是protobuf消息的类型，value是一个两个参数的consumer，第一个参数是message，第二个参数是ctx
     */
    private Map<Class<? extends Message>, ImBiConsumer<? extends Message, ChannelHandlerContext>> parserMap;

    protected AbstractMsgParser() {
        this.parserMap = new HashMap<>();
        registerParsers();
    }

    public static void checkFrom(Message message, Internal.InternalMsg.Module module) {
        if (message instanceof Internal.InternalMsg) {
            Internal.InternalMsg m = (Internal.InternalMsg) message;
            if (m.getFrom() != module) {
                throw new BusinessException("[unexpected msg] expect msg from: " + module.name() +
                    ", but received msg from: " + m.getFrom().name() + "\n\rmsg: " + m.toString());
            }
        }
    }

    public static void checkDest(Message message, Internal.InternalMsg.Module module) {
        if (message instanceof Internal.InternalMsg) {
            Internal.InternalMsg m = (Internal.InternalMsg) message;
            if (m.getDest() != module) {
                throw new BusinessException("[unexpected msg] expect msg to: " + module.name() +
                    ", but received msg to: " + m.getFrom().name());
            }
        }
    }

    /**
     * 注册msg处理方法
     */
    public abstract void registerParsers();

    protected <T extends Message> void register(Class<T> clazz, ImBiConsumer<T, ChannelHandlerContext> consumer) {
        parserMap.put(clazz, consumer);
    }

    @SuppressWarnings("unchecked")
    public void parse(Message msg, ChannelHandlerContext ctx) {
        ImBiConsumer consumer = parserMap.get(msg.getClass());
        if (consumer == null) {
            logger.warn("[message parser] unexpected msg: {}", msg.toString());
        }
        doParse(consumer, msg.getClass(), msg, ctx);
    }

    private <T extends Message> void doParse(ImBiConsumer<T, ChannelHandlerContext> consumer, Class<T> clazz, Message msg, ChannelHandlerContext ctx) {
        T m = clazz.cast(msg);
        try {
            consumer.accept(m, ctx);
        } catch (Exception e) {
            throw new BusinessException("[msg parse] has error");
        }
    }
}
