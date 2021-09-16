package com.zj.server;

import com.zj.cache.ServerCache;
import com.zj.config.AppConfiguration;
import com.zj.config.ZkConfiguration;
import com.zj.exception.BusinessException;
import com.zj.init.ServerInitializer;
import com.zj.util.ZkUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @author xiaozj
 */
@Component
@Slf4j
public class ImServer {

    @Resource
    private ZkConfiguration zkConfiguration;

    @Resource
    private ZkUtil zkUtil;

    @Resource
    private AppConfiguration appConfiguration;

    private EventLoopGroup boss = new NioEventLoopGroup();
    private EventLoopGroup work = new NioEventLoopGroup();

    @PostConstruct
    public void start() {


        try {
            startServer();
        } catch (Exception e) {
            log.error("服务启动出现问题:{}", e.getMessage());
            destroy();
        }

    }


    public void startServer() throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(appConfiguration.getNettyPort()))
                //保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ServerInitializer());

        bootstrap.bind(appConfiguration.getNettyPort()).sync().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("[connector] start successfully at port {}, waiting for clients to connect...", appConfiguration.getNettyPort());
            } else {
                throw new BusinessException("[connector] start failed");
            }
        });
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        removeNode();
        boss.shutdownGracefully().syncUninterruptibly();
        work.shutdownGracefully().syncUninterruptibly();
        log.info("Close server success!!!");
    }

    public void removeNode() {
        zkUtil.deleteNode(zkConfiguration.getZkServer() + "/" + appConfiguration.getServerName());
    }
}
