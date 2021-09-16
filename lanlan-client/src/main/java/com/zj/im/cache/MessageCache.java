package com.zj.im.cache;

import com.zj.im.api.ChatApi;
import com.zj.im.api.UserApi;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
@Component
@Slf4j
public class MessageCache {

    @Resource
    private UserApi userApi;

    @Resource
    private ChatApi chatApi;

    private ConcurrentHashMap<Long, MessageRecord> messageCache = new ConcurrentHashMap<>(16);

    private Set<Long> set = new CopyOnWriteArraySet<>();

    public Set<Long> getSet() {
        return set;
    }

    public void waitForAck(long messageId, MessageRecord messageRecord) {
        messageCache.put(messageId, messageRecord);
    }

    /**
     * 第一种是收到服务器已送达的消息, 将已送达设置为true
     * @param messageId
     */
    public void receiveDelivered(long messageId) {
        if (messageCache.contains(messageId)) {
            MessageRecord record = messageCache.get(messageId);
            record.setDelivered(true);
            messageCache.put(messageId, record);
        }
    }

    /**
     * 第二种是收到已读的ack，则直接在map中删除对应的消息
     * @param messageId
     */
    public void receiveAck(long messageId) {
        if (messageCache.contains(messageId)) {
            messageCache.remove(messageId);
        }
    }

    /**
     * 这里通过线程池异步执行定时任务，每两秒钟轮询一次已发消息的缓存，进行重发，当重发次数到达5次，此时如果已送达标志还是false，
     * 则断开与当前服务器连接，用token执行免密登录。
     * 当重发次数到达10次时，从已发消息缓存中移除
     * @throws ExecutionException
     */
    public void roundCheck() throws ExecutionException {
        Iterator<Long> iterator = messageCache.keySet().iterator();
        while (iterator.hasNext()) {
            Long messageId = iterator.next();
            MessageRecord record = messageCache.get(messageId);

            if (record.getRetryCount() >= 0 && record.getRetryCount() < 5) {
                if (record.getRetryCount() != 0) {
                    // TODO 重发消息
                    chatApi.resendMsg(record.getMsg());
                }
                record.setRetryCount(record.getRetryCount() + 1);
                messageCache.put(messageId, record);

            }
            else if (record.getRetryCount() >= 10) {
                if (!record.isDelivered()) {
                    // TODO 重新登录，切换server
                    userApi.userLoginWithToken();
                } else {
                    log.error("服务器出错，请稍后重发内容为 {} 的消息", record.getMsg().getMsgBody());
                }
                iterator.remove();
            }
        }
    }


}





