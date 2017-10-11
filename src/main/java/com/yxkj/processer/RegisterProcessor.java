package com.yxkj.processer;

import com.yxkj.data.SocketClientMapper;
import com.yxkj.server.AutoSellerServer;
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
    public void process(ChannelHandlerContext ctx, String msg) {

        logger.debug("channelRead: " + ctx.channel().remoteAddress() + " Say:" + msg);


        //msg 格式={f1;d0001;cxxxx};
        //f是否为第一条消息，1为是，0为不是
        //d表示设备号
        //c表示消息内容
        //判断如果是第一条消息，保存socketChannel到map中
        String[] msgs = msg.split(";");
        String uuid = ctx.channel().id().asLongText();
        if (msgs.length != 3) {
            logger.debug("message format error! msg:" + msg);
            return;
        }
        if (msgs[0].equals("1")) {
            SocketClientMapper.addSocketChannel(uuid, msgs[1], (SocketChannel) ctx.channel());
        } else {
            // TODO: 2017/9/27 接受中控消息的处理逻辑
            //        DefaultPromise<String> promise = new DefaultPromise<>(ctx.executor());
//        queueStack.offer(new Request(msg, promise));
//        promise.addProcessor(new PromiseNotifier<String, DefaultPromise<String>>() {
//            @Override
//            public void operationComplete(DefaultPromise<String> future) throws Exception {
//                logger.debug("operationComplete: " + future.get());
//                if (future.isSuccess()) {
//                    ctx.writeAndFlush(Unpooled.wrappedBuffer((future.get() + "\n").getBytes()));
//                }
//            }
//        });
        }
    }
}
