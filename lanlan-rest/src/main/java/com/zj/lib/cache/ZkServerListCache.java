package com.zj.lib.cache;

import com.google.common.cache.LoadingCache;
import com.zj.lib.zookeeper.kit.ZKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ZkServerListCache {

    private static Logger logger = LoggerFactory.getLogger(ZkServerListCache.class) ;

    @Resource
    private LoadingCache<String, String> cache;



    @Autowired
    private ZKit zKit;

    public void addCache(String key) {
        cache.put(key, key);
    }


    /**
     * 更新所有缓存/先删除 再新增
     *
     * @param currentChildren
     */
    public void updateCache(List<String> currentChildren) {
        cache.invalidateAll();
        for (String currentChild : currentChildren) {
            addCache(currentChild);
        }
    }


    /**
     * 获取所有的服务列表
     *
     * @return
     */
    public List<String> getServerList() {

        List<String> list = new ArrayList<>();

        if (cache.size() == 0) {
            List<String> allNode = zKit.getAllNode();
            for (String node : allNode) {
                addCache(node);
            }
        }
        // 返回所有服务列表
        for (Map.Entry<String, String> entry : cache.asMap().entrySet()) {
            list.add(entry.getKey());
        }
        return list;

    }

    /**
     * rebuild cache list
     */
    public void rebuildCacheList(){
        updateCache(getServerList()) ;
    }

}
