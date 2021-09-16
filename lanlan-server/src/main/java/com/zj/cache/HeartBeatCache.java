package com.zj.cache;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaozj
 */
@Component
public class HeartBeatCache {

    private ConcurrentHashMap<String, Integer> heartBeatMap = new ConcurrentHashMap<>(16);


    public void refresh(String username) {
        heartBeatMap.put(username, 0);
    }

    public boolean deadClient(String username) {
        Integer count = heartBeatMap.getOrDefault(username, 0);
        if (count > 3) {
            heartBeatMap.remove(username);
            return true;
        }
        heartBeatMap.put(username, ++count);
        return false;
    }




}
