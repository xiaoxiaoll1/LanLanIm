package com.zj.im.cache;

import com.zj.protobuf.Chat;
import com.zj.protobuf.Internal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRecord {

    private Chat.ChatMsg msg;

    private int retryCount;

    private boolean delivered;

}
