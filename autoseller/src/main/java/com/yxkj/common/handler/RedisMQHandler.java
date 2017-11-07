package com.yxkj.common.handler;

import com.yxkj.common.data.SocketClientMapper;
import com.yxkj.common.entity.CmdMsg;
import com.yxkj.common.utils.*;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryJedisPubSub;

import java.util.HashMap;
import java.util.Map;

public class RedisMQHandler extends BinaryJedisPubSub {
  private Logger logger = LoggerFactory.getLogger(RedisMQHandler.class.getSimpleName());

  @Override
  public void onMessage(byte[] channel, byte[] message) {

    if (null != message) {
      CmdMsg msg = null;
      try {
        msg = (CmdMsg) ObjectUtil.bytes2Object(message);
      } catch (Exception e) {
        e.printStackTrace();
      }
      // 后台命令处理逻辑
      SocketChannel socketChannel = SocketClientMapper.getSocketChannel(msg.getDeviceNo());
      if (socketChannel == null) {
        logger.debug("设备不存在！！忽略命令：" + msg);
        // 如果设备不存在，忽略该命令
        // JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), bytes);
        Map<String, String> params = new HashMap<>();
        params.put("deviceNo", msg.getDeviceNo());
        params.put("isSuccess", "false");
        params.put("extMsg", "DEVICE_EXCEPTION");
        HttpUtil.sendPost(PropertiesUtil.getValueByKey("server.url") + "/cmd/finishCmdStatus",
            params);
      }
      logger.debug("发送命令：" + msg);
      String cmd = JsonUtils.toJson(msg) + "$_$";
      socketChannel.writeAndFlush(cmd);
    }
  }

  @Override
  public void onPMessage(byte[] bytes, byte[] bytes1, byte[] bytes2) {

  }

  @Override
  public void onSubscribe(byte[] bytes, int i) {
			logger.debug("开始订阅" );
  }

  @Override
  public void onUnsubscribe(byte[] bytes, int i) {

  }

  @Override
  public void onPUnsubscribe(byte[] bytes, int i) {

  }

  @Override
  public void onPSubscribe(byte[] bytes, int i) {

  }
}
