package com.zj.common.redis;

import com.zj.common.bo.UserInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaozj
 */

public interface RedisService {


    UserInfo getUserInfoFromCache();



}
