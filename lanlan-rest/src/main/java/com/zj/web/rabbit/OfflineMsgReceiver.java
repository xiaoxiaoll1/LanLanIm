package com.zj.web.rabbit;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import com.rabbitmq.client.Channel;
import com.zj.constant.BusinessConstant;
import com.zj.dto.HistoryMsgDto;
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
@Component
@Slf4j
public class OfflineMsgReceiver implements ChannelAwareMessageListener {

    @Resource
    private MessageService messageService;


    @Override
    @RabbitHandler
    @RabbitListener(queues = BusinessConstant.MQ_OFFLINE_QUEUE, containerFactory = "listenerFactory")
    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
        log.info("[OfflineConsumer] getUserSpi msg: {}", message.toString());
        try {
            byte[] body = message.getBody();
            Chat.ChatMsg msg = Chat.ChatMsg.parseFrom(body);
            HistoryMsgDto historyMsgDto = new HistoryMsgDto();
            historyMsgDto.setMsgId(msg.getId());
            historyMsgDto.setContent(msg.getMsgBody());
            historyMsgDto.setDestUsername(msg.getDestUser());
            historyMsgDto.setFromUsername(msg.getFromUser());
            historyMsgDto.setMsgCreateTime(msg.getCreateTime());
            messageService.addOfflineMsg(historyMsgDto);

        } catch (Exception e) {
            log.error("[OfflineConsumer] has error", e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
