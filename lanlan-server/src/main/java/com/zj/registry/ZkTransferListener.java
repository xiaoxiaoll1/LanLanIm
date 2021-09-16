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
 * @author xiaozj
 */
@Slf4j
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ZkTransferListener implements Runnable {

    @Resource
    private ZkUtil zkUtil;

    @Resource
    private ZkConfiguration zkConfiguration;

    @Resource
    private AppConfiguration appConfiguration;

    @Resource
    private ServerContext serverContext;

    @Resource
    private ImConnector imConnector;

    @Override
    public void run() {

        /**
         * 获取所有的transfer服务器的列表，连接所有的transfer，并将对应的ctx存入一个线程安全的set
         */
        List<String> transferList = zkUtil.getTransferList();
        log.info("获取初始transferList,{}", transferList);
        transferList.forEach(transfer -> {
            imConnector.connectToTransfer(RouteInfoParseUtil.parse(transfer));
            serverContext.addTransfer(transfer);
        });
        // 服务端监听transfer的变化，发生变化则更新ctx
        zkUtil.subscribeTransferEvent(zkConfiguration.getZkTransfer());
    }
}
