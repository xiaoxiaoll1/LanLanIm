package com.zj.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zj.protobuf.Check;
import io.netty.channel.ChannelHandlerContext;
import org.I0Itec.zkclient.ZkClient;
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

    @Resource
    private ZkConfiguration zkConfiguration;

    @Resource
    private AppConfiguration appConfiguration;

    @Bean
    public ZkClient zkClient(){
        return new ZkClient(zkConfiguration.getZkAddress(), zkConfiguration.getZkConnectTimeout());
    }

    @Bean
    public LoadingCache<String, Object> cache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public String load(String s) {
                        return null;
                    }
                });
    }


    @Bean
    public LoadingCache<ChannelHandlerContext, String> reverseCache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<ChannelHandlerContext, String>() {
                    @Override
                    public String load(ChannelHandlerContext s) {
                        return null;
                    }
                });
    }

    /**
     * 创建心跳单例
     * @return
     */
    @Bean(value = "pongHeartBeat")
    public Check.CheckMsg pongHeartBeat() {
        Check.CheckMsg heart = Check.CheckMsg.newBuilder()
                // 约定用ip:port的格式代表server的id
                .setFromUser(appConfiguration.getServerName())
                .setMsgType(Check.CheckMsg.MsgType.PONG)
                .build();
        return heart;
    }

    /**
     * 创建心跳单例
     * @return
     */
    @Bean(value = "pingHeartBeat")
    public Check.CheckMsg pingHeartBeat() {
        Check.CheckMsg heart = Check.CheckMsg.newBuilder()
                .setMsgType(Check.CheckMsg.MsgType.PING)
                .build();
        return heart;
    }

    @Bean
    public Executor businessPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最大线程数
        executor.setMaxPoolSize(5);
        //核心线程数
        executor.setCorePoolSize(1);
        //任务队列的大小
        executor.setQueueCapacity(10);
        //线程前缀名
        executor.setThreadNamePrefix("reconnect-thread");
        //线程存活时间
        executor.setKeepAliveSeconds(30);

        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        //线程初始化
        executor.initialize();
        return executor;
    }
}
