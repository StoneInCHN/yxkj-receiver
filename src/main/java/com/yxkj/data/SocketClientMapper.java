package com.yxkj.data;

import io.netty.channel.socket.SocketChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存socket 管道map，
 *
 * @author huyong
 * @since 2017/9/19
 */
public class SocketClientMapper {
    private static Map<String, SocketChannel> map = new HashMap<String, SocketChannel>();

    public static void addSocketChannel(String id, SocketChannel gateway_channel) {
        map.put(id, gateway_channel);
    }

    public static Map<String, SocketChannel> getChannels() {
        return map;
    }

    public static SocketChannel getSocketChannel(String id) {
        return map.get(id);
    }

    public static void removeSocketChannel(String id) {
        map.remove(id);
    }
}
