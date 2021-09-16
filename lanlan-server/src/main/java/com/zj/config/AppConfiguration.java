package com.zj.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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

    private String serverName;

    private Integer nettyPort;



    @PostConstruct
    private void initialize() throws UnknownHostException {
        //获得本机IP
        String ip = InetAddress.getLocalHost().getHostAddress();
        this.serverName = ip + ":" + nettyPort;
    }
}
