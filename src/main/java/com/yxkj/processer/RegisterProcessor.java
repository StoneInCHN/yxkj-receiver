package com.yxkj.processer;

import com.yxkj.data.SocketClientMapper;
import com.yxkj.entity.NotifyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huyong
 * @since 2017/9/23
 */
public class RegisterProcessor implements IProcessor {
    private Logger logger = LoggerFactory.getLogger(RegisterProcessor.class.getSimpleName());

    @Override
    public void process(ChannelHandlerContext ctx, NotifyMessage msg) {
        logger.debug("channelRead: " + ctx.channel().remoteAddress() + " Say:" + msg);

        String uuid = ctx.channel().id().asLongText();
        SocketClientMapper.addSocketChannel(uuid, msg.getContent(), (SocketChannel) ctx.channel());
    }

    public boolean validateProcessor(NotifyMessage msg) {
        return msg.getMsgType().equals(NotifyMessage.MsgType.REGISTER);
    }
}
