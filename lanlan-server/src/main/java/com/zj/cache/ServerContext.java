package com.zj.cache;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author xiaozj
 */
@Component
public class ServerContext {

    private CopyOnWriteArraySet<ChannelHandlerContext> ctxSet = new CopyOnWriteArraySet<>();

    public void addCtx(ChannelHandlerContext ctx) {
        ctxSet.add(ctx);
    }

    public Set<ChannelHandlerContext> getCtxSet() {
        return ctxSet;
    }

    public void removeCtx(ChannelHandlerContext ctx) {
        ctxSet.remove(ctx);
    }


    private CopyOnWriteArraySet<String> transferSet = new CopyOnWriteArraySet<>();

    public void addTransfer(String transfer) {
        transferSet.add(transfer);
    }

    public Set<String> getTransferSet() {
        return this.transferSet;
    }

    public void removeTransfer(String transfer) {
        transferSet.remove(transfer);
    }
}
