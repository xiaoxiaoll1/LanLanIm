package com.zj.lib.zookeeper.kit;

import com.zj.lib.zookeeper.config.ZkConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/12/23 00:35
 * @since JDK 1.8
 */
@Component
public class ServerListListener implements Runnable{

    @Resource
    private ZKit zkit;

    @Resource
    private ZkConfiguration zkConfiguration ;



    @Override
    public void run() {
        //注册监听服务
        zkit.subscribeServerEvent(zkConfiguration.getZkServer());
    }
}
