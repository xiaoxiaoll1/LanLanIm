package com.zj.lib.service.impl;

import com.zj.common.redis.RedisUtils;
import com.zj.lib.service.DataAnalyzeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaozj
 */
@Slf4j
@Service
public class DataAnalyzeServiceImpl implements DataAnalyzeService {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public long activeUsersDuringTime(String startDay, String endDay) {
        return 0;
    }

    @Override
    public boolean userSignIn(long userId, String signDay) {
        return redisUtils.setBit(signDay, userId, true);
    }

    @Override
    public long userActiveDays(int userId, String startDay, String endDay) {
        return 0;
    }
}
