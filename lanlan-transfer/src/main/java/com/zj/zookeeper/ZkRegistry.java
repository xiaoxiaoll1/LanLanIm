package com.zj.zookeeper;


import com.zj.config.AppConfiguration;
import com.zj.config.ZkConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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



    @Override
    public void run() {

        //创建父节点
        zkUtil.createServerRootNode();

        String path = zkConfiguration.getZkTransfer() + "/" + appConfiguration.getTransferName();
        zkUtil.createNode(path);
        log.info("Registry zookeeper success, msg=[{}]", path);

    }
}