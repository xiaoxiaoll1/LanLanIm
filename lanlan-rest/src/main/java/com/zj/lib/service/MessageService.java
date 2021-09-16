package com.zj.lib.service;

import com.googlecode.protobuf.format.JsonFormat;
import com.zj.dto.FriendReqDto;
import com.zj.dto.HistoryMsgDto;
import com.zj.protobuf.Chat;
import com.zj.web.dao.HistoryMsgDao;

import java.util.List;

/**
 * @author xiaozj
 */
public interface MessageService {

    void addOfflineMsg(HistoryMsgDto historyMsgDto);

    List<HistoryMsgDto> getOfflineMsg(String username);

    void saveMsgToDb(Chat.ChatMsg msg);

    List<HistoryMsgDto> getMsgFromDb(String username, int limit, int pageSize);

    void addFriendReq(FriendReqDto friendReqDto);

    List<FriendReqDto> getFriendReq(String username);
}
