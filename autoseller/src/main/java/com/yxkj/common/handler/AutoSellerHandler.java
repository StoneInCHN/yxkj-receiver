package com.yxkj.common.handler;

import com.yxkj.common.processer.RegisterProcessor;
import com.yxkj.common.server.AutoSellerServer;
import com.yxkj.common.utils.JsonUtils;
import com.yxkj.common.data.SocketClientMapper;
import com.yxkj.common.entity.NotifyMessage;
import com.yxkj.common.processer.ProcessorWatcher;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Server handler
 *
 * @author huyong
 * @since 2017/9/13
 */
public class AutoSellerHandler extends CustomHeartbeatHandler {
    private Logger logger = LoggerFactory.getLogger(AutoSellerServer.class.getSimpleName());
    ProcessorWatcher watcher;

    public AutoSellerHandler() {
        super("AutoSellerServer");

        watcher = new ProcessorWatcher();

        watcher.addProcessor(new RegisterProcessor());
    }


    @Override
    protected void handleData(ChannelHandlerContext ctx, String msg) {
        watcher.process(ctx, JsonUtils.toObject(msg, NotifyMessage.class));
    }

    @Override
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        super.handleAllIdle(ctx);
        logger.error("---client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it---");
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("客户端异常端开");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("---channelInactive --- " + ctx.channel().remoteAddress().toString());
        String uuid = ctx.channel().id().asLongText();
        SocketClientMapper.removeSocketChannel(uuid);
    }
}
