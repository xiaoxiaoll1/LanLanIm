package com.zj.server;

import com.zj.bo.RouteInfo;
import com.zj.cache.ServerContext;
import com.zj.exception.BusinessException;
import com.zj.init.ConnectorInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaozj
 */
@Component
@Slf4j
public class ImConnector {

    @Resource
    private ServerContext serverContext;

    /**
     * 启动客户端
     *
     * @throws Exception
     */
    public void connectToTransfer(RouteInfo routeInfo) {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            ChannelFuture f = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ConnectorInitializer()).connect(routeInfo.getIp(), routeInfo.getServerPort())
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            log.info("服务器连接 transfer 成功");
                        } else {
                            throw new BusinessException("[connector] connect to transfer failed! transfer url: " + routeInfo.getIp() + ":" + routeInfo.getServerPort());
                        }
                    });

            try {
                f.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new BusinessException("[connector] connect to transfer failed! transfer url: " + routeInfo.getIp() + ":" + routeInfo.getServerPort());
            }

    }

    @PreDestroy
    public void destroy() {
        serverContext.getCtxSet().stream().forEach(ctx -> {
            ctx.channel().close();
        });
        log.info("Close server success!!!");
    }
}
