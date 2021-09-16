package com.zj.im.cache;

import com.google.common.cache.LoadingCache;
import io.netty.channel.ChannelHandlerContext;
import org.apache.el.util.ConcurrentCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author xiaozj
 */
@Component
public class ClientCache {



    @Resource
    private LoadingCache<String, Object> cache;


    public void addCache(String key, Object value) {
        cache.put(key, value);
    }

    public Object getCache(String key) {
        Object value = cache.getUnchecked(key);
        return value;
    }



}
