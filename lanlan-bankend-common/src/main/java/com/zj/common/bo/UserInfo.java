package com.zj.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaozj
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String username;

    private String aesKey;

    private String serverAddress;

}
