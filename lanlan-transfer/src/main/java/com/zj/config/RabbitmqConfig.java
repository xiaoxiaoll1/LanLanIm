package com.zj.config;


import com.zj.constant.BusinessConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.connection.SimpleRoutingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaozj
 */
@Configuration
public class RabbitmqConfig {

    public final static String all = BusinessConstant.MQ_ALL_QUEUE;
    public final static String offline = BusinessConstant.MQ_OFFLINE_QUEUE;
    public final static String friend = BusinessConstant.MQ_FRIEND_QUEUE;

    @Bean
    public Queue allMsgQueue() {
        return new Queue(all);
    }

    @Bean
    public Queue offlineMsgQueue() {
        return new Queue(offline);
    }

    @Bean
    public Queue friendReqQueue() {
        return new Queue(friend);
    }

    @Bean
    TopicExchange msgExchange() {
        return new TopicExchange("msgExchange");
    }


    @Bean
    Binding bindingFriendReq() {
        return BindingBuilder.bind(allMsgQueue()).to(msgExchange()).with(friend);
    }
    /**
     * 将allMsgQueue和topicExchange绑定,而且绑定的键值为msg.#
     * 这样只要是消息携带的路由键以msg开头,都会分发到该队列
     * @return
     */
    @Bean
    Binding bindingAllExchangeMessage() {
        return BindingBuilder.bind(allMsgQueue()).to(msgExchange()).with(all);
    }

    //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
    // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    Binding bindingOfflineExchangeMessage() {
        return BindingBuilder.bind(offlineMsgQueue()).to(msgExchange()).with(offline);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory();
    }

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
                System.out.println("ConfirmCallback:     "+"确认情况："+ack);
                System.out.println("ConfirmCallback:     "+"原因："+cause);
            }
        });

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {

            @Override
            public void returnedMessage(ReturnedMessage returned) {

            }
        });

        return rabbitTemplate;
    }


}

