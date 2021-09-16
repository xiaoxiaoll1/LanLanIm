package com.zj.zookeeper;


import com.zj.config.ZkConfiguration;


import com.zj.util.RouteInfoParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * zk工具类
 * @author xiaozj
 */
@Component
@Slf4j
public class ZkUtil {

    @Resource
    private ZkClient zkClient;

    @Autowired
    private ZkConfiguration zkConfiguration ;



    /**
     * 创建父级节点
     */
    public void createServerRootNode(){
        boolean exists = zkClient.exists(zkConfiguration.getZkTransfer());
        if (exists){
            return;
        }

        //创建 root
        zkClient.createPersistent(zkConfiguration.getZkTransfer()) ;
    }

    /**
     * 写入指定节点 临时目录
     *
     * @param path
     */
    public void createNode(String path) {
        boolean exists = zkClient.exists(path);
        if (exists){
            return;
        }
        zkClient.createEphemeral(path);
    }

    public void deleteNode(String path) {
        zkClient.delete(path);
    }



}
