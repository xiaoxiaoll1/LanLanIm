package com.zj.lib.service.impl;



import com.zj.constant.CommonCacheKey;
import com.zj.common.redis.RedisUtils;
import com.zj.lib.service.TokenService;
import com.zj.web.entity.UserInfoEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaozj
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.expiration}")
    private Integer expiration;

    @Resource
    private RedisUtils redisUtils;

    private static SecretKey secretKey;


    static {
        //采用HS256算法
        secretKey = MacProvider.generateKey(SignatureAlgorithm.HS256);

    }

    @Override
    public String getToken(String username) {
        String key = CommonCacheKey.TOKEN_PREFIX + username;
        String tokenInRedis = (String) redisUtils.get(key);
        log.info("TokenRedisService.getToken:key={},value={}", key, tokenInRedis);
        return tokenInRedis;
    }

    @Override
    public String setToken(UserInfoEntity userInfoEntity) {
        Date exp = DateUtils.addSeconds(new Date(), expiration);
        Map<String, Object> map = new HashMap<>(2);
        map.put("username", userInfoEntity.getUserName());
        map.put("password", userInfoEntity.getPassword());

        //采用HS256算法
        JwtBuilder builder = Jwts.builder()
                .setClaims(map)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secretKey);
        String token = builder.compact();
        redisUtils.hset(CommonCacheKey.HASH_PREFIX + userInfoEntity.getUserName(), CommonCacheKey.TOKEN_PREFIX, token, expiration);
        log.info("TokenRedisService.setToken:value={}",  token);
        return token;
    }

    @Override
    public Claims getClaimsFromToken(String token){
        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(secretKey);
        return jwtParser.parseClaimsJws(token).getBody();
    }
}
