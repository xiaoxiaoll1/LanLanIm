package com.zj.web.rabbit;

import com.googlecode.protobuf.format.JsonFormat;
import com.rabbitmq.client.Channel;
import com.zj.constant.BusinessConstant;
import com.zj.lib.service.MessageService;
import com.zj.protobuf.Chat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiaozj
 */
@Slf4j
@Component
public class HistoryMsgReceiver implements ChannelAwareMessageListener {

    @Resource
    private MessageService messageService;


    @Override
    @RabbitHandler
    @RabbitListener(queues = BusinessConstant.MQ_OFFLINE_QUEUE, containerFactory = "listenerFactory")
    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
        log.info("[HistoryMsgReceiver] getUser msg: {}", message.toString());
        try {
            byte[] body = message.getBody();
            Chat.ChatMsg msg = Chat.ChatMsg.parseFrom(body);
            messageService.saveMsgToDb(msg);
        } catch (Exception e) {
            log.error("[OfflineConsumer] has error", e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}