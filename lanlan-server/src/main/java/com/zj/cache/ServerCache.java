package com.zj.cache;

import com.google.common.cache.LoadingCache;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
@Component
public class ServerCache {

    @Resource
    private LoadingCache<String, Object> cache;

    @Resource
    private LoadingCache<ChannelHandlerContext, String> reverseCache;

    public void addCache(String key, Object value) {
        cache.put(key, value);
    }

    public Object getCache(String key) {
        Object value = cache.getUnchecked(key);
        return value;
    }

    public void remove(String key) {
        cache.invalidate(key);
    }


    public void addReverseCache(ChannelHandlerContext key, String value) {
        reverseCache.put(key, value);
    }

    public String getReverseCache(ChannelHandlerContext key) {
        String value = reverseCache.getUnchecked(key);
        return value;
    }

    public void removeReverse(ChannelHandlerContext key) {
        reverseCache.invalidate(key);
    }

}
