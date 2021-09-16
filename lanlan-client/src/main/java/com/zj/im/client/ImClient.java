package com.zj.im.client;

import com.zj.exception.BusinessException;
import com.zj.im.api.UserApi;
import com.zj.im.cache.ClientCache;
import com.zj.im.config.AppConfiguration;
import com.zj.im.config.WebConfiguration;
import com.zj.im.init.ClientHandleInitializer;
import com.zj.im.service.EchoService;
import com.zj.im.web.service.RestService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * @author xiaozj
 */
@Component
@Slf4j
public class ImClient {


    private EventLoopGroup group = new NioEventLoopGroup(2, new DefaultThreadFactory("client-work"));

    private Channel channel;

    @Resource
    private UserApi userapi;

    @Resource
    private AppConfiguration appConfiguration;


    @Resource
    private ClientCache cache;



    @Resource
    private EchoService echoService;


    @Resource
    private Executor businessPool;

    @Resource
    private RestService restService;

    /**
     * 重试次数
     */
    private int errorCount;


    /**
     * 程序启动时，启动用户登录
     */
    @PostConstruct
    public void start() {

        //登录 + 获取可以使用的服务器 ip+port
        userapi.userLogin();

    }




    /**
     * 启动客户端
     *
     * @throws Exception
     */
    public void startClient() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ClientHandleInitializer());

        ChannelFuture future = null;
        try {
            future = bootstrap.connect((String) cache.getCache("ip"), Integer.parseInt((String) cache.getCache("port"))).sync();
        } catch (InterruptedException e) {
            log.error("重连遇到错误{}", e.getMessage());
        }
        if (future != null && future.isSuccess()) {
            echoService.echo("Start im client success!");
            log.info("启动 im client 成功");
        }
        else {
            reLogin();
        }
        channel = future.channel();
    }

    public void reconnect(ChannelHandlerContext ctx) {
        channel = ctx.channel();
        if (channel != null && channel.isActive()) {
            return;
        }
        businessPool.execute(() -> {
                startClient();
        });

    }

    public void reLogin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", appConfiguration.getUserName());
        /**
         * 这里offline请求不要带token，因为如果token过期了，则永远无法离线了
         */
        log.info("重新登录中。。。。");
        restService.offline(map);
        userapi.userLoginWithToken();
    }

    /**
     * 关闭
     */
    public void close() {
        if (channel != null){
            channel.close();
        }
    }

    /**
     * 关闭系统
     */
    public void shutdown() throws ExecutionException {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", appConfiguration.getUserName());
        map.put("token", (String) cache.getCache("token"));
        log.info("系统关闭中。。。。");
        restService.offline(map);
        close();
        System.exit(0);
    }

}
