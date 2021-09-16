package com.zj.im.config;

import com.zj.util.CryptUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author xiaozj
 */
@Data
@ConfigurationProperties(prefix = "app-config")
@Configuration
public class AppConfiguration {



    private String userName;

    private String password;

    private String aesKey;

    private String publicKey;

    /**
     * 启动时初始化 aesKey
     */
    @PostConstruct
    public void generateAesKey() {
        aesKey = CryptUtil.getKey();
    }
}
