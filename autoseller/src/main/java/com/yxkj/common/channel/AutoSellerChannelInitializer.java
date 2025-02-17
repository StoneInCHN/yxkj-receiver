package com.yxkj.common.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import com.yxkj.common.handler.AutoSellerHandler;

/**
 * channel初始化类，可以定义管道解析规则
 *
 * @author huyong
 * @since 2017/9/13
 */
public class AutoSellerChannelInitializer<C extends Channel> extends ChannelInitializer<C> {

    protected void initChannel(C ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new IdleStateHandler(0, 0, 10));
        p.addLast(new LoggingHandler());
//        p.addLast("decoder", new LineBasedFrameDecoder(1024));
        ByteBuf buf = Unpooled.copiedBuffer("$_$".getBytes());
        p.addLast(new DelimiterBasedFrameDecoder(1024, buf));
        p.addLast(new StringDecoder());
        p.addLast(new StringEncoder());
        p.addLast("myHandler", new AutoSellerHandler());

    }
}
