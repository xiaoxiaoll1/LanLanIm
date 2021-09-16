package com.zj.im.web.service;

import com.zj.bo.LoginResult;
import com.zj.dto.HistoryMsgDto;
import com.zj.response.ResultDataDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaozj
 */
public interface RestService {

    LoginResult login(String data);

    void offline(Map<String, String> map);

    ResultDataDto register(String data);

    LoginResult loginWithToken(Map<String, String> map);


    Set<String> commonFriends(Map<String, String> map);

    Set<String> getFriendSet(Map<String, String> map);

    void addFriend(String data);

    void deleteFriend(Map<String, String> map);

    List<HistoryMsgDto> getHistoryMsg(Map<String, String> map);



}
