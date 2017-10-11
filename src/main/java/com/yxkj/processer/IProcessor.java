package com.yxkj.processer;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author huyong
 * @since 2017/9/19
 */
public interface IProcessor {
    void process(ChannelHandlerContext ctx, String msg);
}
