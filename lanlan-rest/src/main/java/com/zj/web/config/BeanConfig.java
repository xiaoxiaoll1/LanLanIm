package com.zj.web.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zj.lib.route.RouteHandle;
import com.zj.lib.route.consistentHash.ConsistentHashHandle;
import com.zj.lib.zookeeper.config.ZkConfiguration;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author xiaozj
 */
@Configuration
public class BeanConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory listenerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Resource
    private ZkConfiguration zkConfiguration;

    @Bean
    public ZkClient buildZKClient() {
        return new ZkClient(zkConfiguration.getZkAddress(), zkConfiguration.getZkConnectTimeout());
    }

    @Bean
    public LoadingCache<String, String> Cache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String s) throws Exception {
                        return null;
                    }
                });
    }


    @Bean
    public RouteHandle buildRouteHandle() throws Exception {
        return new ConsistentHashHandle();
    }

    @Bean
    public Executor businessPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //???????????????
        executor.setMaxPoolSize(5);
        //???????????????
        executor.setCorePoolSize(1);
        //?????????????????????
        executor.setQueueCapacity(10);
        //???????????????
        executor.setThreadNamePrefix("reconnect-thread");
        //??????????????????
        executor.setKeepAliveSeconds(30);

        /**
         * ??????????????????
         * CallerRunsPolicy()??????????????????????????????????????? main ?????????
         * AbortPolicy()????????????????????????
         * DiscardPolicy()??????????????????
         * DiscardOldestPolicy()????????????????????????????????????
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //???????????????
        executor.initialize();
        return executor;
    }
}
