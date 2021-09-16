package com.zj.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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

    private String zkTransfer;

    private int zkConnectTimeout;
}
