package com.zj.transfer;

import com.zj.config.AppConfiguration;
import com.zj.config.ZkConfiguration;
import com.zj.exception.BusinessException;
import com.zj.init.TransferInitializer;
import com.zj.zookeeper.ZkUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @author xiaozj
 */
@Component
@Slf4j
public class ImTransfer {

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
                .childHandler(new TransferInitializer());
        log.info("transfer正在启动中");
        bootstrap.bind(appConfiguration.getNettyPort()).sync().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("transfer启动成功, 等待server连接到{}端口...", appConfiguration.getNettyPort());
            } else {
                throw new BusinessException("transfer启动失败");
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
        zkUtil.deleteNode(zkConfiguration.getZkTransfer() + "/" + appConfiguration.getTransferName());
    }
}
