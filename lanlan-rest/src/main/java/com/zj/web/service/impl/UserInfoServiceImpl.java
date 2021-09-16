package com.zj.web.service.impl;

import com.google.common.collect.Sets;
import com.zj.dto.HistoryMsgDto;
import com.zj.web.dao.HistoryMsgDao;
import com.zj.web.dao.UserInfoDao;
import com.zj.web.dao.UserRelationDao;
import com.zj.web.entity.HistoryMsgEntity;
import com.zj.web.entity.UserRelationEntity;
import com.zj.web.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserRelationDao userRelationDao;

    @Resource
    private HistoryMsgDao historyMsgDao;

    @Override
    public List<HistoryMsgDto> getHistoryMsg(String username, int limit, int pageSize) {
        List<HistoryMsgEntity> historyMsgPage = historyMsgDao.findHistoryMsgPage(username, limit, pageSize);
        return historyMsgPage.stream().map(entity -> {
            return convertToHistoryDto(entity);
        }).collect(Collectors.toList());

    }

    @Override
    public List<String> getCommonFriends(String user1, String user2) {
        List<UserRelationEntity> friends1 = userRelationDao.findFriendsByUsername(user1);
        List<UserRelationEntity> friends2 = userRelationDao.findFriendsByUsername(user2);
        Set<String> friends1Set = friends1.stream().map(UserRelationEntity::getUserName2).collect(Collectors.toSet());
        Set<String> friends2Set = friends2.stream().map(UserRelationEntity::getUserName2).collect(Collectors.toSet());
        Sets.SetView<String> commonFriends = Sets.intersection(friends1Set, friends2Set);
        return new ArrayList<>(commonFriends);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriend(String fromUser, String destUser) {
        UserRelationEntity entity = new UserRelationEntity();
        entity.setUserName1(fromUser);
        entity.setUserName2(destUser);
        UserRelationEntity entity2 = new UserRelationEntity();
        entity.setUserName1(destUser);
        entity.setUserName2(fromUser);
        userRelationDao.save(entity);
        userRelationDao.save(entity2);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(String fromUser, String destUser) {
        userRelationDao.deleteUserRelationEntityByUserName1AndAndUserName2(fromUser, destUser);
        userRelationDao.deleteUserRelationEntityByUserName1AndAndUserName2(destUser, fromUser);
    }

    @Override
    public List<String> getFriends(String username) {
        List<UserRelationEntity> friends = userRelationDao.findFriendsByUsername(username);
        return friends.stream().map(UserRelationEntity::getUserName2).collect(Collectors.toList());
    }

    private HistoryMsgDto convertToHistoryDto(HistoryMsgEntity entity) {
            HistoryMsgDto historyMsgDto = new HistoryMsgDto();
            historyMsgDto.setMsgId(entity.getMsgId());
            historyMsgDto.setContent(entity.getContent());
            historyMsgDto.setDestUsername(entity.getDestUsername());
            historyMsgDto.setMsgCreateTime(entity.getMsgCreateTime());
            historyMsgDto.setFromUsername(entity.getFromUsername());
            return historyMsgDto;
    }


}
