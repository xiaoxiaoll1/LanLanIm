package com.zj.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.zj.bo.LoginResult;
import com.zj.dto.AgreeAddFriendsDto;
import com.zj.dto.LoginDto;
import com.zj.dto.RegisterDto;
import com.zj.exception.BusinessException;
import com.zj.response.ResultDataDto;
import com.zj.web.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 登录controller层
 * @author xiaozj
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {


    @Resource
    private UserInfoService userInfoService;


    @PostMapping("/addFriend")
    public ResultDataDto<String> addFriend(String body) {
        ResultDataDto<String> resultDataDto = new ResultDataDto<>();
        try {
            AgreeAddFriendsDto agreeAddFriendsDto = JSONObject.parseObject(body, AgreeAddFriendsDto.class);
            agreeAddFriendsDto.getFriends().stream().forEach(friend -> {
                userInfoService.addFriend(agreeAddFriendsDto.getUsername(), friend);
            });
        } catch (BusinessException e) {
            resultDataDto.setCode(e.getErrorCode());
            resultDataDto.setMessage(e.getErrorMessage());
            return resultDataDto;
        }
        return resultDataDto.successResult("");
    }

    @GetMapping("/deleteFriend")
    public ResultDataDto register(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "deleteFriendName") String deleteFriendName) {

        ResultDataDto resultDataDto = new ResultDataDto();
        try {
            userInfoService.deleteFriend(username, deleteFriendName);
        } catch (BusinessException e) {
            resultDataDto.setCode(e.getErrorCode());
            resultDataDto.setMessage(e.getErrorMessage());
            return resultDataDto;
        }
        return resultDataDto.successResult("");
    }

//    @GetMapping("/getCommonFriends")
//    public ResultDataDto<LoginResult> loginWithToken(String username) {
//        ResultDataDto<LoginResult> resultDataDto = new ResultDataDto<>();
//        LoginResult loginResult = null;
//        try {
//            loginResult = loginService.loginWithToken(username);
//        } catch (BusinessException e) {
//            resultDataDto.setCode(e.getErrorCode());
//            resultDataDto.setMessage(e.getErrorMessage());
//            return resultDataDto;
//        }
//        return resultDataDto.successResult(loginResult);
//
//    }
}
