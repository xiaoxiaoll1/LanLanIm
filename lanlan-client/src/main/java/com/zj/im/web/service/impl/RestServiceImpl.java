package com.zj.im.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zj.bo.LoginResult;
import com.zj.constant.BusinessConstant;
import com.zj.dto.HistoryMsgDto;
import com.zj.exception.BusinessException;
import com.zj.im.config.WebConfiguration;
import com.zj.im.web.service.RestService;
import com.zj.response.ResultDataDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaozj
 */
@Service
@Slf4j
public class RestServiceImpl implements RestService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WebConfiguration webConfiguration;


    @Override
    public void offline(Map<String, String> map) {
        // TODO 修改为get请求,请求头含有username和token
        ResultDataDto resultDataDto = restTemplate.getForObject(webConfiguration.getOfflineUrl(), ResultDataDto.class, map);
        checkResultData(resultDataDto);
    }

    @Override
    public ResultDataDto register(String data) {
        return (ResultDataDto) restTemplate.postForObject(webConfiguration.getRegisterUrl(), data, ResultDataDto.class);
    }


    @Override
    public LoginResult login(String data) {
        ResultDataDto resultDataDto = restTemplate.postForObject(webConfiguration.getLoginUrl(), data, ResultDataDto.class);
        checkResultData(resultDataDto);
        String loginResultJson = JSON.toJSONString(resultDataDto.getData());
        LoginResult loginResult = JSONObject.parseObject(loginResultJson, LoginResult.class);
        return loginResult;
    }


    @Override
    public LoginResult loginWithToken(Map<String, String> map) {
        ResultDataDto resultDataDto = restTemplate.getForObject(webConfiguration.getLoginWithTokenUrl(), ResultDataDto.class, map);
        checkResultData(resultDataDto);
        String loginResultJson = JSON.toJSONString(resultDataDto.getData());
        LoginResult loginResult = JSONObject.parseObject(loginResultJson, LoginResult.class);
        return loginResult;
    }


    @Override
    public Set<String> commonFriends(Map<String, String> map) {
        ResultDataDto resultDataDto = restTemplate.getForObject(webConfiguration.getCommonFriendUrl(), ResultDataDto.class, map);
        checkResultData(resultDataDto);
        String loginResultJson = JSON.toJSONString(resultDataDto.getData());
        LoginResult loginResult = JSONObject.parseObject(loginResultJson, LoginResult.class);
        return null;
    }

    @Override
    public Set<String> getFriendSet(Map<String, String> map) {
        return null;
    }

    @Override
    public void addFriend(String data) {
        ResultDataDto resultDataDto = restTemplate.postForObject(webConfiguration.getAddFriendUrl(), data, ResultDataDto.class);
        checkResultData(resultDataDto);
    }

    @Override
    public void deleteFriend(Map<String, String> map) {
        ResultDataDto resultDataDto = restTemplate.getForObject(webConfiguration.getDeleteFriendUrl(), ResultDataDto.class, map);
        checkResultData(resultDataDto);
    }

    @Override
    public List<HistoryMsgDto> getHistoryMsg(Map<String, String> map) {
        return null;
    }

    private void checkResultData(ResultDataDto resultDataDto) {
        if (resultDataDto.getCode() != BusinessConstant.SUCCESS_CODE) {
            log.error("登录失败,{}", resultDataDto.getMessage());
            throw new BusinessException(resultDataDto.getCode(), resultDataDto.getMessage());
        }
    }
}
