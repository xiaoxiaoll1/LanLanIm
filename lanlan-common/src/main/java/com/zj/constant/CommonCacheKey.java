package com.zj.constant;

import java.io.Serializable;

/**
 * @author xiaozj
 */
public class CommonCacheKey implements Serializable {

    public static final String TOKEN_PREFIX = "token@";

    public static final String AES_KEY = "aes_key";

    public static final String USER_STATUS_KEY = "user_status_key";

    public static final String CONNECTOR_KEY = "connector_key";

    public static final String HASH_PREFIX = "hash@";

    public static final String USER_RELATION_PREFIX = "user_relation@";

    public static final String OFFLINE_PREFIX = "offline@";

    public static final String FRIEND_REQ_PREFIX = "friendReq@";
}
