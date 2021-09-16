package com.zj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author xiaozj
 */
@Component
@ConfigurationProperties(prefix = "zk-config")
@Data
public class ZkConfiguration {

    private String zkAddress;

    private String zkTransfer;

    private int zkConnectTimeout;
}
