package com.yxkj.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class TimeClient {
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {

        // Configure the client.
        for (int i = 0; i < 100; i++) {
        new Thread(() -> {
            TimeClient client = new TimeClient();
            client.start();
        }).start();
        }
    }

    public void start() {
        String host = "localhost";
        int port = 3331;

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new IdleStateHandler(8, 0, 0));
                            p.addLast(new LoggingHandler());
//                p.addLast("decoder", new LineBasedFrameDecoder(1024));
                            ByteBuf buf = Unpooled.copiedBuffer("$_$".getBytes());
                            p.addLast(new DelimiterBasedFrameDecoder(1024, buf));
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new NettyClientHandler(TimeClient.this));

                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (
                InterruptedException e)

        {
            e.printStackTrace();
        } finally

        {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

}