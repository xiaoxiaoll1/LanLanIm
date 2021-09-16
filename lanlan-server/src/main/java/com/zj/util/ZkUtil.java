package com.zj.util;


import com.google.common.collect.Sets;
import com.zj.bo.RouteInfo;
import com.zj.cache.ServerContext;
import com.zj.config.ZkConfiguration;
import com.zj.server.ImConnector;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

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


    @Resource
    private ServerContext serverContext;

    @Resource
    private ImConnector imConnector;

    /**
     * 创建父级节点
     */
    public void createServerRootNode(){
        boolean exists = zkClient.exists(zkConfiguration.getZkServer());
        if (exists){
            return;
        }

        //创建 root
        zkClient.createPersistent(zkConfiguration.getZkServer()) ;
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


    /**
     * 获取所有transfer
     */
    public List<String> getTransferList() {
        return zkClient.getChildren(zkConfiguration.getZkTransfer());
    }


    /**
     * 监听事件
     * 一旦transfer节点发生变化，先获取现在活跃的transfer节点，生成一个set，利用guava的工具库求set的两个差集，
     * 本地的ctx set移除少掉的部分，连接多出来的transfer
     * @param path
     */
    public void subscribeTransferEvent(String path) {
        zkClient.subscribeChildChanges(path, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChildren) throws Exception {
                log.info("Clear and update local cache parentPath=[{}],currentChildren=[{}]", parentPath,currentChildren.toString());
                Set<String> transferSet = serverContext.getTransferSet();
                CopyOnWriteArraySet<String> currentSet = new CopyOnWriteArraySet<>(currentChildren);
                Sets.SetView<String> addDiff = Sets.difference(currentSet, transferSet);
                Sets.SetView<String> lessDiff = Sets.difference(transferSet, currentSet);
                addDiff.stream().forEach(address -> {
                    RouteInfo parse = RouteInfoParseUtil.parse(address);
                    imConnector.connectToTransfer(parse);
                });
                transferSet.removeAll(lessDiff);
                transferSet.addAll(addDiff);
            }
        });
    }
}
