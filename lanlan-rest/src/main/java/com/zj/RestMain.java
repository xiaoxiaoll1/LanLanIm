package com.zj;

import com.zj.lib.zookeeper.kit.ServerListListener;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @author xiaozj
 */
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
@EnableRabbit
public class RestMain implements CommandLineRunner {

    @Resource
    private ServerListListener serverListListener;

    @Resource
    private Executor businessPool;

    public static void main(String[] args) {
        SpringApplication.run(RestMain.class, args);
    }

    @Override
    public void run(String... args) {

        businessPool.execute(serverListListener);
    }
}
