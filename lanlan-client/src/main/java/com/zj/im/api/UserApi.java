package com.zj.im.api;

import com.alibaba.fastjson.JSON;
import com.zj.bo.LoginResult;
import com.zj.constant.BusinessConstant;
import com.zj.dto.AgreeAddFriendsDto;
import com.zj.dto.FriendReqDto;
import com.zj.dto.HistoryMsgDto;
import com.zj.dto.LoginDto;
import com.zj.exception.BusinessException;

import com.zj.im.cache.ClientCache;
import com.zj.im.client.ImClient;
import com.zj.im.config.AppConfiguration;
import com.zj.im.config.WebConfiguration;
import com.zj.im.web.service.RestService;
import com.zj.protobuf.Ack;
import com.zj.protobuf.Internal;
import com.zj.response.ResultDataDto;
import com.zj.util.CryptUtil;
import com.zj.util.IdWorker;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author xiaozj
 */
@Service
@Slf4j
public class UserApi {

    @Resource
    private AppConfiguration appConfiguration;

    @Resource
    private WebConfiguration webConfiguration;

    @Resource
    private ClientCache cache;

    @Resource
    private RestService restService;

    @Resource
    private ImClient imClient;


    /**
     * 登录+路由服务器
     * 参考HTTPS的加密流程，但是没有证书
     * 登陆时，像服务器传递username，password和aesKey，用服务器端的公钥加密
     * @return 路由服务器信息
     * @throws Exception
     */
    public void userLogin() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(appConfiguration.getUserName());
        loginDto.setPassword(appConfiguration.getPassword());
        loginDto.setAseKey(appConfiguration.getAesKey());
        String json = JSON.toJSONString(loginDto);
        LoginResult data = null;
        try {
            byte[] encrypt = CryptUtil.encrypt(json.getBytes(), appConfiguration.getPublicKey());

             data = restService.login(Base64.getEncoder().encodeToString(encrypt));
        } catch (Exception e) {
            // 这里异常很大，必须停止登录流程
            log.error("登录失败，失败原因是{}", e.getMessage());
            return;
        }

        /**
         * 收到服务器传回来的ip和port后就连接服务器
         * 缓存好友列表，token，ip，port
         */
        parseOfflineMsg(data.getOfflineMsg());
        cache.addCache("token", data.getToken());
        cache.addCache("ip", data.getIp());
        cache.addCache("port", String.valueOf(data.getServerPort()));
        cache.addCache("friends", data.getFriendSet());
        imClient.startClient();

        /**
         * 选择接受好友请求的话就发生rest请求，并通过此时的连接回应ack消息给新好友
         * 消息分为四种：
         * 1.聊天消息
         * 2.内部消息：包括刚连上服务器时发送的greet消息，好友请求
         * 3.ack消息，包括已读消息，已送达消息，好友接受或拒绝消息
         * 4.心跳消息
         */
        List<FriendReqDto> friendRequests = data.getFriendRequests();
        if (addFriendSuccess(friendRequests)) {
            ChannelHandlerContext ctx = (ChannelHandlerContext) cache.getCache("ctx");
            friendRequests.stream().forEach(friendReqDto -> {
                String fromUsername = friendReqDto.getFromUsername();
                Ack.AckMsg ackMsg = replyToFriendReq(fromUsername, true);
                ctx.writeAndFlush(ackMsg);
            });
        }
    }

    /**
     * TODO 可能因为token过期而失败，失败时需重新登录
     * @throws Exception
     */
    public void userLoginWithToken() {
        imClient.close();
        Map<String, String> params = new HashMap<>();
        LoginResult data = null;
        params.put("token", (String) cache.getCache("token"));
        params.put("username", appConfiguration.getUserName());
        try {
             data = restService.loginWithToken(params);
        } catch (Exception e) {
            log.error("采取token登录方式失败，转用正常的登录流程");
            userLogin();
            return;
        }


        parseOfflineMsg(data.getOfflineMsg());
        cache.addCache("ip", data.getIp());
        cache.addCache("port", String.valueOf(data.getServerPort()));
        cache.addCache("friends", data.getFriendSet());
        imClient.startClient();
        List<FriendReqDto> friendRequests = data.getFriendRequests();
        if (addFriendSuccess(friendRequests)) {
            ChannelHandlerContext ctx = (ChannelHandlerContext) cache.getCache("ctx");
            friendRequests.stream().forEach(friendReqDto -> {
                String fromUsername = friendReqDto.getFromUsername();
                Ack.AckMsg ackMsg = replyToFriendReq(fromUsername, true);
                ctx.writeAndFlush(ackMsg);
            });

        }
    }



    public void userLoginInit() {
        Internal.InternalMsg greet = Internal.InternalMsg.newBuilder()
                .setId(IdWorker.genId())
                .setFrom(Internal.InternalMsg.Module.CLIENT)
                .setDest(Internal.InternalMsg.Module.SERVER)
                .setCreateTime(System.currentTimeMillis())
                .setVersion(1)
                .setMsgType(Internal.InternalMsg.MsgType.GREET)
                .setFromUser(appConfiguration.getUserName())
                .build();


        try {
            ChannelHandlerContext ctx = (ChannelHandlerContext) cache.getCache("ctx");
            // 注意ctx.write()和channel.write()的区别
            ChannelFuture channelFuture = ctx.writeAndFlush(greet);
            channelFuture.sync();
        } catch (Exception e) {
            log.error("发生错误，未能成功收到服务器ack");
            throw new BusinessException(e.getMessage());
        }
    }

    public Ack.AckMsg replyToFriendReq(String username, boolean flag) {
        Ack.AckMsg.Builder builder = Ack.AckMsg.newBuilder()
                .setId(IdWorker.genId())
                .setFromUser(appConfiguration.getUserName())
                .setDestUser(username)
                .setCreateTime(System.currentTimeMillis())
                .setVersion(1);

        if (flag) {
            builder.setMsgType(Ack.AckMsg.MsgType.FRIENDAGREE);
        } else {
            builder.setMsgType(Ack.AckMsg.MsgType.FRIENDDELETE);
        }
        return builder.build();

    }

    public Internal.InternalMsg addFriendRequest(String friendName) {
        Internal.InternalMsg addFriendReq = Internal.InternalMsg.newBuilder()
                .setId(IdWorker.genId())
                .setFrom(Internal.InternalMsg.Module.CLIENT)
                .setDest(Internal.InternalMsg.Module.SERVER)
                .setCreateTime(System.currentTimeMillis())
                .setVersion(1)
                .setMsgType(Internal.InternalMsg.MsgType.FRIENDReq)
                .setFromUser(appConfiguration.getUserName())
                .setDestUser(friendName)
                .build();
        return addFriendReq;
    }

    public boolean addFriendSuccess(List<FriendReqDto> friendReqs) {
        Set<String> oldFriends = (Set<String>) cache.getCache("friends");
        List<String> friends = friendReqs.stream().map(friendReqDto -> {
            oldFriends.add(friendReqDto.getFromUsername());
            return friendReqDto.getFromUsername();
        }).collect(Collectors.toList());
        AgreeAddFriendsDto agreeAddFriendsDto = new AgreeAddFriendsDto(appConfiguration.getUserName(), friends);
        try {
            restService.addFriend(JSON.toJSONString(agreeAddFriendsDto));
        } catch (Exception e) {
            return false;
        }
        cache.addCache("friends", oldFriends);
        return true;
    }

    private void parseOfflineMsg(List<HistoryMsgDto> messages) {
        messages.stream().forEach(System.out::println);
    }

    public void getHistoryMsg(int limit, int pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("token", (String) cache.getCache("token"));
        params.put("username", appConfiguration.getUserName());
        params.put("limit", limit + "");
        params.put("pageSize", pageSize + "");

        List<HistoryMsgDto> historyMsg = restService.getHistoryMsg(params);
        parseOfflineMsg(historyMsg);
    }

    public void deleteFriend(String deleteFriendName) {
        Set<String> oldFriends = (Set<String>) cache.getCache("friends");
        Map<String, String> params = new HashMap<>();
        params.put("token", (String) cache.getCache("token"));
        params.put("username", appConfiguration.getUserName());
        params.put("deleteFriendName", deleteFriendName);
        try {
            restService.deleteFriend(params);
            /**
             * 考虑到因为网络原因，rest服务端其实已经执行了请求，返回超时，这时就不会发送取消朋友的请求了
             */
        } catch (Exception e) {
            log.error("删除朋友失败{}", e.getMessage());
            return;
        }
        oldFriends.remove(deleteFriendName);
        cache.addCache("friends", oldFriends);
        replyToFriendReq(deleteFriendName, false);

    }



}
