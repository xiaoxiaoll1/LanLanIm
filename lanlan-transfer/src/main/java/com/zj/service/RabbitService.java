package com.zj.service;

import com.google.protobuf.Message;

/**
 * @author xiaozj
 */
public interface RabbitService {

    void sendAllMsg(Message msg);

    void sendOfflineMsg(Message msg);

    void sendFriendReq(Message msg);
}
