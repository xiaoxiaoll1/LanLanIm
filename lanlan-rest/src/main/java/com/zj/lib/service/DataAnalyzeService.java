package com.zj.lib.service;

/**
 * 统计用户流量等数据
 * @author xiaozj
 */
public interface DataAnalyzeService {

    /**
     * 统计一段时间内活跃用户的数量
     * @param startDay
     * @param endDay
     * @return
     */
    long activeUsersDuringTime(String startDay, String endDay);

    /**
     * 记录用户登录
     * @param userId
     * @param signDay
     * @return
     */
    boolean userSignIn(long userId, String signDay);

    /**
     * 统计某个用户一段时间内的活跃天数
     * @param userId
     * @param startDay
     * @param endDay
     * @return
     */
    long userActiveDays(int userId, String startDay, String endDay);

}
