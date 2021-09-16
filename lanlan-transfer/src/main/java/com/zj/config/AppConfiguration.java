package com.zj.config;

/**
 * @author xiaozj
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xiaozj
 */
@Component
@ConfigurationProperties(prefix = "app-config")
@Data
public class AppConfiguration {

    private String transferName;

    private Integer nettyPort;


    @PostConstruct
    private void initialize() throws UnknownHostException {
        //获得本机IP
        String ip = InetAddress.getLocalHost().getHostAddress();
        this.transferName = ip + ":" + nettyPort;
        System.out.println("启动端口为" + nettyPort);
    }

}
