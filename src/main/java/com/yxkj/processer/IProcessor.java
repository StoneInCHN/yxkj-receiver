package com.yxkj.processer;

import com.yxkj.beans.NotifyMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author huyong
 * @since 2017/9/19
 */
public interface IProcessor {
    void process(ChannelHandlerContext ctx, NotifyMessage msg);

    boolean validateProcessor(NotifyMessage msg);
}
