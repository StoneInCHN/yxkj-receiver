package com.yxkj.client;

import com.yxkj.entity.NotifyMessage;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import com.yxkj.handler.CustomHeartbeatHandler;
import com.yxkj.utils.JsonUtils;

/**
 * @author huyong
 * @since 2017/9/13
 */
public class NettyClientHandler extends CustomHeartbeatHandler {

    private static int count = 1;
    TimeClient timeClient;

    public NettyClientHandler(TimeClient timeClient) {
        super("Test Client");
        this.timeClient = timeClient;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        count++;
        NotifyMessage msg = new NotifyMessage();
        msg.setContent(String.valueOf(count));
        msg.setMsgType(NotifyMessage.MsgType.REGISTER);
        String cmd = JsonUtils.toJson(msg) + "$_$";
        ctx.writeAndFlush(Unpooled.wrappedBuffer(cmd.getBytes()));

    }

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, String msg) {
        System.out.println("receive " + msg);
    }

    /**
     * 未收到消息时，发送心跳检查包
     *
     * @param ctx
     */
    @Override
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        sendPingMsg(ctx);
    }

    /**
     * 连接断开后启动重连机制
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        timeClient.start();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
