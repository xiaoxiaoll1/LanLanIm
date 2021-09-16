package com.zj.lib.service.impl;

import com.zj.constant.CommonCacheKey;
import com.zj.exception.BusinessException;
import com.zj.exception.ExceptionEnum;
import com.zj.common.redis.RedisUtils;
import com.zj.lib.service.UserStatusService;
import com.zj.web.dao.UserRelationDao;
import com.zj.web.entity.UserRelationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @author xiaozj
 */
@Service
@Slf4j
public class UserStatusServiceImpl implements UserStatusService {



    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserRelationDao userRelationDao;


    @Override
    public boolean setConnector(String username, String connectorId) {
        return redisUtils.hset(CommonCacheKey.HASH_PREFIX + username, CommonCacheKey.CONNECTOR_KEY, connectorId);

    }

    @Override
    public boolean setUserStatusOnline(String username) {
        return redisUtils.hset(CommonCacheKey.HASH_PREFIX + username, CommonCacheKey.USER_STATUS_KEY, "online");
    }

    @Override
    public boolean setUserStatusOffline(String username) {
        return redisUtils.hset(CommonCacheKey.HASH_PREFIX + username, CommonCacheKey.USER_STATUS_KEY, "offline");
    }

    @Override
    public boolean setAesKey(String username, String aesKey) {
        return redisUtils.hset(CommonCacheKey.HASH_PREFIX + username, CommonCacheKey.AES_KEY, aesKey);
    }

    @Override
    public void removeUser(String username) {
        redisUtils.del(CommonCacheKey.HASH_PREFIX + username, CommonCacheKey.TOKEN_PREFIX);
    }


    @Override
    public Set<Object> getCommonFriends(String username1, String username2) {
        if (!redisUtils.hasKey(username1) || !redisUtils.hasKey(username2)) {
            throw new BusinessException(ExceptionEnum.REDIS_NOT_FOUND.getCode(), ExceptionEnum.REDIS_NOT_FOUND.getMsg());
        }
        Set<Object> set = redisUtils.setDiff(CommonCacheKey.USER_RELATION_PREFIX + username1, CommonCacheKey.USER_RELATION_PREFIX + username2);
        return set;
    }


}
