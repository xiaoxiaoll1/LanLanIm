package com.zj.registry;

import com.zj.cache.ServerContext;
import com.zj.config.AppConfiguration;
import com.zj.config.ZkConfiguration;
import com.zj.server.ImConnector;
import com.zj.util.RouteInfoParseUtil;
import com.zj.util.ZkUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Zk注册器
 * @author xiaozj
 */
@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ZkRegistry implements Runnable {


    @Resource
    private ZkUtil zkUtil;

    @Resource
    private ZkConfiguration zkConfiguration;

    @Resource
    private AppConfiguration appConfiguration;

    @Resource
    private ServerContext serverContext;



    @Override
    public void run() {

        //创建父节点
        zkUtil.createServerRootNode();

        String path = zkConfiguration.getZkServer() + "/" + appConfiguration.getServerName();
        zkUtil.createNode(path);
        log.info("Registry zookeeper success, msg=[{}]", path);



    }
}