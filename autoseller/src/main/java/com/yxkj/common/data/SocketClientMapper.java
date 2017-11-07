package com.yxkj.common.data;

import com.yxkj.common.utils.HttpUtil;
import com.yxkj.common.utils.PropertiesUtil;
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
  private static Map<String, SocketChannel> map = new HashMap<>();

  private static Map<String, String> uuidMap = new HashMap<>();

  public static void addSocketChannel(String uuid, String deviceNo, SocketChannel gateway_channel) {
//    Map<String, String> params = new HashMap<>();
//    params.put("deviceNo", deviceNo);
//    params.put("connectStatus", "true");
//    HttpUtil.sendPost(PropertiesUtil.getValueByKey("server.url") + "/cmd/connectionStatusUpdate",
//        params);

    map.put(deviceNo, gateway_channel);
    uuidMap.put(uuid, deviceNo);
  }

  public static Map<String, SocketChannel> getChannels() {
    return map;
  }

  public static SocketChannel getSocketChannel(String deviceNo) {
    return map.get(deviceNo);
  }

  public static void removeSocketChannel(String uuid) {
    String deviceNo = uuidMap.get(uuid);
    map.remove(deviceNo);
    uuidMap.remove(uuid);
    // Map<String, String> params = new HashMap<>();
    // params.put("deviceNo", deviceNo);
    // params.put("connectStatus", "false");
    // HttpUtil.sendPost(PropertiesUtil.getValueByKey("server.url") + "/cmd/connectionStatusUpdate",
    // params);
  }
}
