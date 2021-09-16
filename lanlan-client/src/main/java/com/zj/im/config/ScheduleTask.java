package com.zj.im.config;

import com.zj.im.cache.MessageCache;
import com.zj.im.cache.MessageRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
@Component
@EnableAsync
public class ScheduleTask {

    @Resource
    private MessageCache messageCache;

    /**
     * 这里利用线程池异步扫描缓存，如果用默认的配置可能会产生饥饿现象
     */
    @Async("asyncPool")
    @Scheduled(fixedRate = 2000)
    public void checkRead() throws ExecutionException {
        messageCache.roundCheck();
    }

}