package com.yxkj.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author huyong
 * @since 2017/9/13
 */
public class NettyClientHandler extends SimpleChannelInboundHandler {

    /**
     * Creates a client-side handler.
     */
    public NettyClientHandler() {

    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String cmd = "1;001;test$_$";
        ctx.writeAndFlush(Unpooled.wrappedBuffer(cmd.getBytes()));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
