package com.zj.web.service;

import com.zj.dto.HistoryMsgDto;

import java.util.List;

/**
 * @author xiaozj
 */
public interface UserInfoService {

    List<HistoryMsgDto> getHistoryMsg(String username, int limit, int pageSize);

    List<String> getCommonFriends(String user1, String user2);

    void addFriend(String fromUser, String destUser);

    void deleteFriend(String fromUser, String destUser);

    List<String> getFriends(String username);
}
