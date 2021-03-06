package com.zj.web.service.impl;


import com.zj.bo.LoginResult;
import com.zj.bo.RouteInfo;
import com.zj.dto.FriendReqDto;
import com.zj.dto.HistoryMsgDto;
import com.zj.exception.BusinessException;
import com.zj.lib.cache.ZkServerListCache;
import com.zj.lib.route.RouteHandle;
import com.zj.lib.service.DataAnalyzeService;
import com.zj.lib.service.MessageService;
import com.zj.lib.service.TokenService;
import com.zj.lib.service.UserStatusService;
import com.zj.lib.zookeeper.service.AvailableService;
import com.zj.util.DateUtils;
import com.zj.util.RouteInfoParseUtil;
import com.zj.web.constant.LoginResponseCode;
import com.zj.web.constant.UserConstant;
import com.zj.web.dao.UserInfoDao;

import com.zj.dto.LoginDto;
import com.zj.dto.RegisterDto;
import com.zj.web.entity.UserInfoEntity;
import com.zj.web.service.LoginService;
import com.zj.util.CryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author xiaozj
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private TokenService tokenService;

    @Resource
    private UserStatusService userStatusService;

    @Resource
    private DataAnalyzeService dataAnalyzeService;

    @Resource
    private AvailableService availableService;

    @Resource
    private RouteHandle routeHandle ;

    @Resource
    private ZkServerListCache zkServerListCache;

    @Resource
    private MessageService messageService;




    @Transactional(rollbackOn = Exception.class)
    @Override
    public void register(RegisterDto registerDto) {
        if (!StringUtils.hasText(registerDto.getUsername()) || !StringUtils.hasText(registerDto.getPassword())) {
            throw new BusinessException(LoginResponseCode.MISS_INFORMATION, LoginResponseCode.MISS_INFORMATION_DESC);
        }
        String username = registerDto.getUsername();
        String password = registerDto.getPassword();
        UserInfoEntity user = new UserInfoEntity();

        boolean exist = existUsername(username);
        boolean registerSuccess = false;
        if (exist) {
            log.info("The user already exists! username={}", username);
            throw new BusinessException(LoginResponseCode.USER_REPEAT, LoginResponseCode.USER_REPEAT_DESC);
        } else {
            String encodedPassword = CryptUtil.md5(password).toLowerCase();
            user.setPassword(encodedPassword);
            user.setUserName(username);
            boolean setSuccess = checkUsername(username);

            if (setSuccess) {
                userInfoDao.save(user);
                registerSuccess = true;
            }
            }
            if (!registerSuccess) {
                throw new BusinessException(LoginResponseCode.USER_REGISTER_FAILED, LoginResponseCode.USER_REGISTER_FAILED_DESC);
            }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoginResult login(LoginDto loginDto) {
        LoginResult loginResult = new LoginResult();
        if (!StringUtils.hasText(loginDto.getUsername()) || !StringUtils.hasText(loginDto.getPassword())
                || !StringUtils.hasText(loginDto.getAseKey())) {
            throw new BusinessException(LoginResponseCode.MISS_INFORMATION, LoginResponseCode.MISS_INFORMATION_DESC);
        }
        /**
         * ???????????????????????????????????????jwt??????token??????hash??????????????????redis????????????hash???name???username?????????
         * ????????????aesKey????????????????????????hash?????????????????????????????????server??????????????????server???????????????????????????ip???port???
         * ????????????????????????????????????????????????????????????????????????redis?????????????????????server??????????????????
         * ?????????redis?????????????????????????????????????????????????????????????????????redis???list???
         * ??????????????????????????????????????????
         * ????????????????????????????????????????????????????????????
         */
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        String encodedPassword = CryptUtil.md5(password).toLowerCase();
        UserInfoEntity userDb = userInfoDao.findByUserName(username);
        String passwordInDB = userDb.getPassword();
        if (!encodedPassword.equals(passwordInDB)) {
            throw new BusinessException(LoginResponseCode.USER_CENTER_ERROR, LoginResponseCode.USER_CENTER_ERROR_DESC);
        }
        String token = tokenService.setToken(userDb);
        RouteInfo routeInfo;
        try {
            dataAnalyzeService.userSignIn(userDb.getId(), DateUtils.getCurrentTime("yyyy-MM-dd"));
            userStatusService.setAesKey(username, loginDto.getAseKey());
            // check server available
            String server = routeHandle.routeServer(zkServerListCache.getServerList(), loginDto.getUsername());
            log.info("userName=[{}] route server info=[{}]", loginDto.getUsername(), server);

            routeInfo = RouteInfoParseUtil.parse(server);
            availableService.checkServerAvailable(routeInfo);
            userStatusService.setConnector(username, server);


        } catch (BusinessException e) {
            log.error("??????????????????:{}", e.getErrorMessage());
            throw e;
        }

        List<HistoryMsgDto> offlineMsg = messageService.getOfflineMsg(username);;
        List<FriendReqDto> friendReqs = messageService.getFriendReq(username);

        userStatusService.setUserStatusOnline(username);

        loginResult.setToken(token);
        loginResult.setUsername(username);
        loginResult.setIp(routeInfo.getIp());
        loginResult.setServerPort(routeInfo.getServerPort());
        loginResult.setFriendRequests(friendReqs);
        loginResult.setOfflineMsg(offlineMsg);
        return loginResult;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public LoginResult loginWithToken(String username) {
        userStatusService.setUserStatusOffline(username);
        RouteInfo routeInfo;

        String server = routeHandle.routeServer(zkServerListCache.getServerList(), username);
        log.info("userName=[{}] route server info=[{}]", username, server);

        routeInfo = RouteInfoParseUtil.parse(server);
        availableService.checkServerAvailable(routeInfo);
        userStatusService.setConnector(username, server);
        userStatusService.setUserStatusOnline(username);
        List<HistoryMsgDto> offlineMsg = messageService.getOfflineMsg(username);;
        List<FriendReqDto> friendReqs = messageService.getFriendReq(username);
        LoginResult loginResult = new LoginResult();
        loginResult.setUsername(username);
        loginResult.setIp(routeInfo.getIp());
        loginResult.setServerPort(routeInfo.getServerPort());
        loginResult.setOfflineMsg(offlineMsg);
        loginResult.setFriendRequests(friendReqs);
        return loginResult;
    }


    private boolean existUsername(String username) {
        UserInfoEntity user = userInfoDao.findByUserName(username);
        return user != null;
    }

    private boolean checkUsername(String username) {
        if (username.length() < UserConstant.MIN_USERNAME_LENGTH || username.length() > UserConstant.MAX_USERNAME_LENGTH) {
            return false;
        }
        return true;
    }

}
