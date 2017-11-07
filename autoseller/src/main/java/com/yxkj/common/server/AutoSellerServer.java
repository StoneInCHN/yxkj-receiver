package com.yxkj.common.server;

import com.yxkj.common.channel.AutoSellerChannelInitializer;
import com.yxkj.common.commonenum.Constant;
import com.yxkj.common.handler.OperationSystemHandlerThread;
import com.yxkj.common.handler.RedisMQHandler;
import com.yxkj.common.utils.JedisUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Server 启动类
 *
 * @author huyong
 * @since 2017/9/13
 */
public class AutoSellerServer {

    Logger logger = LoggerFactory.getLogger(AutoSellerServer.class.getSimpleName());

    public void startServer(int port) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            serverBootstrap.group(group);
            serverBootstrap.option(ChannelOption.AUTO_READ, true);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress(port));
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new AutoSellerChannelInitializer<SocketChannel>());
//            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                protected void initChannel(SocketChannel ch) throws Exception {
//                    ChannelPipeline p = ch.pipeline();
//                    p.addLast(new IdleStateHandler(5, 5, 5, TimeUnit.SECONDS));
//                    p.addLast(new LoggingHandler());
//                    p.addLast("decoder", new LineBasedFrameDecoder(1024));
//                    p.addLast(new StringDecoder());
//                    p.addLast(new StringEncoder());
//                    p.addLast(new AutoSellerHandler());
//                }
//            });

            ChannelFuture f = serverBootstrap.bind().sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            System.out.println(AutoSellerServer.class.getName() + " started and listen on " + f.channel().localAddress());
            logger.debug("Server started and listen on " + f.channel().localAddress());


//            OperationSystemHandlerThread thread = new OperationSystemHandlerThread();
            JedisUtil.subscribe(new RedisMQHandler(), Constant.JEDIS_MESSAGE_KEY.getBytes());
//            thread.start();
            f.channel().closeFuture().sync();// 应用程序会一直等待，直到channel关闭
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();//关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }

    public static void main(String[] args) {
        try {
            new AutoSellerServer().startServer(3331);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
