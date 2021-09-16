package com.zj.lib.zookeeper.kit;

import com.alibaba.fastjson.JSON;

import com.zj.lib.cache.ZkServerListCache;
import com.zj.lib.zookeeper.config.ZkConfiguration;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author xiaozj
 */
@Component
public class ZKit {

    private static Logger logger = LoggerFactory.getLogger(ZKit.class);


    @Autowired
    private ZkClient zkClient;

    @Autowired
    private ZkServerListCache zkServerListCache;

    @Resource
    private ZkConfiguration zkConfiguration;


    /**
     * 监听事件
     *
     * @param path
     */
    public void subscribeServerEvent(String path) {
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChildren) {
                logger.info("Clear and update local cache parentPath=[{}],currentChildren=[{}]", parentPath,currentChildren.toString());

                // 这里的child格式为 ip:port
                zkServerListCache.updateCache(currentChildren) ;
            }
        });
    }




    /**
     * get all server node from zookeeper
     * @return
     */
    public List<String> getAllNode(){
        List<String> children = zkClient.getChildren(zkConfiguration.getZkServer());
        logger.info("Query all node =[{}] success.", JSON.toJSONString(children));
        return children;
    }


}
