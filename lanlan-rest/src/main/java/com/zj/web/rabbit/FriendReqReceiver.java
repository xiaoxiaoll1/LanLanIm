package com.zj.web.rabbit;

import com.rabbitmq.client.Channel;
import com.zj.constant.BusinessConstant;
import com.zj.dto.FriendReqDto;
import com.zj.dto.HistoryMsgDto;
import com.zj.lib.service.MessageService;
import com.zj.protobuf.Chat;
import com.zj.protobuf.Internal;
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
public class FriendReqReceiver implements ChannelAwareMessageListener {

    @Resource
    private MessageService messageService;


    @Override
    @RabbitHandler
    @RabbitListener(queues = BusinessConstant.MQ_FRIEND_QUEUE, containerFactory = "listenerFactory")
    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
        log.info("[OfflineConsumer] getUserSpi msg: {}", message.toString());
        try {
            byte[] body = message.getBody();
            Internal.InternalMsg internalMsg = Internal.InternalMsg.parseFrom(body);
            FriendReqDto friendReqDto = new FriendReqDto();
            friendReqDto.setFromUsername(internalMsg.getFromUser());
            friendReqDto.setDestUsername(internalMsg.getDestUser());
            friendReqDto.setMsgCreateTime(internalMsg.getCreateTime());
            messageService.addFriendReq(friendReqDto);

        } catch (Exception e) {
            log.error("[OfflineConsumer] has error", e);
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}
