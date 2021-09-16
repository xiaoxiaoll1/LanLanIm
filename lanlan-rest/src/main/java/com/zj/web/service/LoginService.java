package com.zj.web.service;

import com.googlecode.protobuf.format.JsonFormat;
import com.zj.bo.LoginResult;
import com.zj.dto.LoginDto;
import com.zj.dto.RegisterDto;


/**
 * 登录业务层
 * @author xiaozj
 */
public interface LoginService {

    /**
     * 注册新用户
     * @return
     */
    void register(RegisterDto registerDto);

    /**
     * 登录
     * @return
     */
    LoginResult login(LoginDto loginDto);


    /**
     * 以token登录
     */
    LoginResult loginWithToken(String username);

}
