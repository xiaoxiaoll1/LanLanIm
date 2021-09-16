package com.zj.lib.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.protobuf.format.JsonFormat;
import com.zj.common.redis.RedisUtils;
import com.zj.constant.CommonCacheKey;

import com.zj.dto.FriendReqDto;
import com.zj.dto.HistoryMsgDto;
import com.zj.lib.service.MessageService;
import com.zj.protobuf.Chat;
import com.zj.web.dao.HistoryMsgDao;
import com.zj.web.entity.HistoryMsgEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author xiaozj
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HistoryMsgDao historyMsgDao;




    @Override
    public void addOfflineMsg(HistoryMsgDto historyMsgDto) {
        redisUtils.rPush(CommonCacheKey.OFFLINE_PREFIX + historyMsgDto.getDestUsername(), JSON.toJSONString(historyMsgDto));
    }

    /**
     * 普通消息的时序问题并没有一个完美的解决方案，特别是由于网络原因引起的乱序
     * 注意消息乱序问题，离线消息不能乱序
     * @param username
     * @return
     */
    @Override
    public List<HistoryMsgDto> getOfflineMsg(String username) {
        TreeSet<HistoryMsgDto> set = new TreeSet<>((a, b) -> {
            return Long.compare(a.getMsgId(), b.getMsgId());
        });
        HashSet<Long> idSet = new HashSet<>();
        String json = (String) redisUtils.lPop(CommonCacheKey.OFFLINE_PREFIX + username);
        while (StringUtils.isNotBlank(json)) {
            HistoryMsgDto historyMsgDto = JSONObject.parseObject(json, HistoryMsgDto.class);
            if (!idSet.contains(historyMsgDto.getMsgId())) {
                set.add(historyMsgDto);
            }
            idSet.add(historyMsgDto.getMsgId());
        }
        return new ArrayList<>(set);

    }

    /**
     * 该方法需保证幂等性
     * @param msg
     */
    @Override
    public void saveMsgToDb(Chat.ChatMsg msg) {
        HistoryMsgEntity entity = new HistoryMsgEntity();
        entity.setContent(msg.getMsgBody());
        entity.setDestUsername(msg.getDestUser());
        entity.setMsgId(msg.getId());
        entity.setFromUsername(msg.getFromUser());
        entity.setMsgCreateTime(msg.getCreateTime());
        historyMsgDao.save(entity);
    }

    @Override
    public List<HistoryMsgDto> getMsgFromDb(String username, int limit, int pageSize) {
        return getMsgFromDbPage(username, limit, pageSize);
    }

    @Override
    public void addFriendReq(FriendReqDto friendReqDto) {
        redisUtils.rPush(CommonCacheKey.FRIEND_REQ_PREFIX + friendReqDto.getDestUsername(), JSON.toJSONString(friendReqDto));
    }

    @Override
    public List<FriendReqDto> getFriendReq(String username) {
        TreeSet<FriendReqDto> set = new TreeSet<>((a, b) -> {
            return Long.compare(a.getMsgCreateTime(), b.getMsgCreateTime());
        });
        String json = (String) redisUtils.lPop(CommonCacheKey.FRIEND_REQ_PREFIX + username);
        while (StringUtils.isNotBlank(json)) {
            FriendReqDto friendReqDto = JSONObject.parseObject(json, FriendReqDto.class);
            set.add(friendReqDto);
        }
        return new ArrayList<>(set);
    }


    private List<HistoryMsgDto> getMsgFromDbPage(String username, int limit, int pageSize) {
        List<HistoryMsgEntity> historyMsgPage = historyMsgDao.findHistoryMsgPage(username, limit, pageSize);
        return historyMsgPage.stream().map(entity -> {
            HistoryMsgDto historyMsgDto = new HistoryMsgDto();
            historyMsgDto.setMsgId(entity.getMsgId());
            historyMsgDto.setFromUsername(entity.getFromUsername());
            historyMsgDto.setMsgCreateTime(entity.getMsgCreateTime());
            historyMsgDto.setContent(entity.getContent());
            historyMsgDto.setDestUsername(entity.getDestUsername());
            return historyMsgDto;
        }).collect(Collectors.toList());
    }

}
