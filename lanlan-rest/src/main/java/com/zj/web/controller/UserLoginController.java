package com.zj.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.zj.bo.LoginResult;
import com.zj.exception.BusinessException;
import com.zj.dto.LoginDto;
import com.zj.dto.RegisterDto;
import com.zj.response.ResultDataDto;

import com.zj.web.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

/**
 * 登录controller层
 * @author xiaozj
 */
@RestController
@RequestMapping("/userLogin")
public class UserLoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public ResultDataDto<LoginResult> login(@RequestBody String body) {
        ResultDataDto<LoginResult> resultDataDto = new ResultDataDto<>();
        LoginResult loginResult = null;
        try {
            LoginDto loginDto = JSONObject.parseObject(body, LoginDto.class);
            loginResult = loginService.login(loginDto);
        } catch (BusinessException e) {
            resultDataDto.setCode(e.getErrorCode());
            resultDataDto.setMessage(e.getErrorMessage());
            return resultDataDto;
        }
        return resultDataDto.successResult(loginResult);
    }

    @PostMapping("/register")
    public ResultDataDto register(@RequestBody String body) {

        ResultDataDto resultDataDto = new ResultDataDto();
        try {
            RegisterDto registerDto = JSONObject.parseObject(body, RegisterDto.class);
            loginService.register(registerDto);
        } catch (BusinessException e) {
            resultDataDto.setCode(e.getErrorCode());
            resultDataDto.setMessage(e.getErrorMessage());
            return resultDataDto;
        }
        return resultDataDto.successResult("");
    }

    @GetMapping("/loginWithToken")
    public ResultDataDto<LoginResult> loginWithToken(String username) {
        ResultDataDto<LoginResult> resultDataDto = new ResultDataDto<>();
        LoginResult loginResult = null;
        try {
            loginResult = loginService.loginWithToken(username);
        } catch (BusinessException e) {
            resultDataDto.setCode(e.getErrorCode());
            resultDataDto.setMessage(e.getErrorMessage());
            return resultDataDto;
        }
        return resultDataDto.successResult(loginResult);

    }
}
