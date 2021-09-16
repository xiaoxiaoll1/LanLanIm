package com.zj.service.impl;

import com.google.protobuf.Message;
import com.zj.config.RabbitmqConfig;
import com.zj.service.RabbitService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaozj
 */
@Service
public class RabbitServiceImpl implements RabbitService {


    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendAllMsg(Message msg) {
        rabbitTemplate.convertAndSend("msgExchange", RabbitmqConfig.all, msg);
    }

    @Override
    public void sendOfflineMsg(Message msg) {
        byte[] bytes = msg.toByteArray();
        rabbitTemplate.convertAndSend("msgExchange", RabbitmqConfig.offline, bytes);
    }

    @Override
    public void sendFriendReq(Message msg) {
        byte[] bytes = msg.toByteArray();
        rabbitTemplate.convertAndSend("msgExchange", RabbitmqConfig.friend, bytes);
    }
}
