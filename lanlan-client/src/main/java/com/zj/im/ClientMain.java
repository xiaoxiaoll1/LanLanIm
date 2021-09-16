package com.zj.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xiaozj
 */
@SpringBootApplication
@EnableScheduling
public class ClientMain {

    public static void main(String[] args) {
        SpringApplication.run(ClientMain.class, args);
    }
}
