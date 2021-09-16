package com.zj;

import com.zj.config.AppConfiguration;
import com.zj.registry.ZkRegistry;
import com.zj.registry.ZkTransferListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.concurrent.Executor;

/**
 * @author xiaozj
 */
@SpringBootApplication
@Slf4j
public class ServerMain implements CommandLineRunner {


    @Resource
    private ZkRegistry zkRegistry;

    @Resource
    private ZkTransferListener zkTransferListener;

    @Resource
    private Executor businessPool;


    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class, args);
        log.info("Start server success!!!");
    }

    /**
     * server服务器启动时会将自己的ip和port作为key注册到zookeeper的临时节点上
     * 并开启对于transfer节点的监听
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        businessPool.execute(zkRegistry);
        businessPool.execute(zkTransferListener);
    }
}
