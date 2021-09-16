package com.zj.lib.service;


import com.zj.web.entity.UserInfoEntity;
import io.jsonwebtoken.Claims;

/**
 * @author xiaozj
 */

public interface TokenService {


    /**
     * 获取token
     * @param username
     * @return
     */
     String getToken(String username);

    /**
     * 设置token
     * @param userInfoEntity
     * @return
     */
     String setToken(UserInfoEntity userInfoEntity);


    /**
     * 解析token内容
     * @param token
     * @return
     */
    Claims getClaimsFromToken(String token);

}
