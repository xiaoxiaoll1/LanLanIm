package com.zj.bo;

import com.zj.dto.FriendReqDto;
import com.zj.dto.HistoryMsgDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

/**
 * 登录结果
 * @author xiaozj
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {


    private String token;

    private String username;

    private String ip;

    private Integer serverPort;

    /**
     * 存储离线消息
     */
    private List<HistoryMsgDto> offlineMsg;

    /**
     * 序列化后的
     */
    private List<FriendReqDto> friendRequests;

    private Set<String> friendSet;

}
