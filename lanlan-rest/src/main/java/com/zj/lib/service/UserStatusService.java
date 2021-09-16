package com.zj.lib.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiaozj
 */
public interface UserStatusService {


    /**
     * 设置连接器id
     * @param username
     * @param connectorId
     * @return
     */
    boolean setConnector(String username, String connectorId);

    /**
     * 设置用户状态上线
     * @param username
     * @return
     */
    boolean setUserStatusOnline(String username);

    /**
     * 设置aesKey
     * @param username
     * @param aesKey
     * @return
     */
    boolean setAesKey(String username, String aesKey);

    /**
     * 移除用户状态
     * @param username
     * @return
     */
    void removeUser(String username);



    boolean setUserStatusOffline(String username);

    /**
     * 查询共同好友
     * @param username1
     * @param username2
     * @return
     */
    Set<Object> getCommonFriends(String username1, String username2);

}
