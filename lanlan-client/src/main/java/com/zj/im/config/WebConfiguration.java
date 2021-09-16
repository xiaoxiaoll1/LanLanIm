package com.zj.im.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaozj
 */
@ConfigurationProperties(prefix = "rest.config")
@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebConfiguration {

    private String loginUrl;

    private String registerUrl;

    private String offlineUrl;

    private String loginWithTokenUrl;

    private String addFriendUrl;

    private String deleteFriendUrl;

    private String commonFriendUrl;
}
