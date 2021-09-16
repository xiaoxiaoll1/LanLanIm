package com.zj.lib.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * zookeeper 节点配置
 * @author xiaozj
 */
@Component
@ConfigurationProperties(prefix = "zk-config")
@Data
public class ZkConfiguration {


    private String zkServer;


    private String zkAddress;


    private int zkConnectTimeout;

}
