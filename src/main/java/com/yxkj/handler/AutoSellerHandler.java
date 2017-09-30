package com.yxkj.handler;

import com.yxkj.data.SocketClientMapper;
import com.yxkj.server.AutoSellerServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Server handler
 *
 * @author huyong
 * @since 2017/9/13
 */
public class AutoSellerHandler extends SimpleChannelInboundHandler<String> {
    private Logger logger = LoggerFactory.getLogger(AutoSellerServer.class.getSimpleName());
//    private LinkedBlockingQueue<Request> queueStack = null;

//    public AutoSellerHandler() {
//        queueStack = new LinkedBlockingQueue<>();
//        new OperationSystemHandlerThread(queueStack).start();
//    }

    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.debug("channelRead: " + ctx.channel().remoteAddress() + " Say:" + msg);


        //msg 格式={f1;d0001;cxxxx};
        //f是否为第一条消息，1为是，0为不是
        //d表示设备号
        //c表示消息内容
        //判断如果是第一条消息，保存socketChannel到map中
        String[] msgs = msg.split(";");
        if (msgs == null || msgs.length != 3) {
            logger.debug("message format error! msg:" + msg);
            return;
        }
        if (msgs[0].equals("1")) {
            SocketClientMapper.addSocketChannel(msgs[1], (SocketChannel) ctx.channel());
        }else {
            // TODO: 2017/9/27 接受中控消息的处理逻辑
            //        DefaultPromise<String> promise = new DefaultPromise<>(ctx.executor());
//        queueStack.offer(new Request(msg, promise));
//        promise.addListener(new PromiseNotifier<String, DefaultPromise<String>>() {
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


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String uuid = ctx.channel().id().asLongText();
        NioSocketChannel socketChannel = (NioSocketChannel) ctx.channel();
        SocketClientMapper.addSocketChannel(uuid, socketChannel);
        logger.debug("channelActive: uuid=" + uuid);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        logger.error("客户端异常端开");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("channelInactive");
        String uuid = ctx.channel().id().asLongText();
        SocketClientMapper.removeSocketChannel(uuid);
    }
}
