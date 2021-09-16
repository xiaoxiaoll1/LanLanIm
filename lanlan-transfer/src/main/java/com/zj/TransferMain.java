package com.zj;

/**
 * @author xiaozj
 */

import com.zj.zookeeper.ZkRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @author xiaozj
 */
@SpringBootApplication
@Slf4j
public class TransferMain implements CommandLineRunner {


    @Resource
    private ZkRegistry zkRegistry;


    @Resource
    private Executor businessPool;


    public static void main(String[] args) {
        SpringApplication.run(TransferMain.class, args);
        log.info("Start transfer success!!!");
    }

    @Override
    public void run(String... args) throws Exception {
        // 将自己注册到zookeeper
        businessPool.execute(zkRegistry);
    }
}
