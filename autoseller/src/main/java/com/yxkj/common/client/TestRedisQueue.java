package com.yxkj.common.client;

import com.yxkj.common.entity.CmdMsg;
import com.yxkj.common.entity.NotifyMessage;
import com.yxkj.common.utils.JedisUtil;
import com.yxkj.common.utils.ObjectUtil;

import java.io.IOException;

import static com.yxkj.common.data.Constant.JEDIS_MESSAGE_KEY;

/**
 * @author huyong
 * @since 2017/9/23
 */
public class TestRedisQueue {
  public static byte[] redisKey = "push".getBytes();

  // static {
  // try {
  // init();
  // } catch (IOException e) {
  // e.printStackTrace();
  // }
  // }

  private static void init() throws IOException {
    NotifyMessage message = new NotifyMessage();
    // message.setContent("test");
    for (int i = 0; i < 100000; i++) {
      JedisUtil.publish("push", "test " + i);
    }
  }

  public static void main(String[] args) {
    try {

      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void pop() throws Exception {
    byte[] bytes = JedisUtil.rpop(JEDIS_MESSAGE_KEY.getBytes());
    CmdMsg msg = (CmdMsg) ObjectUtil.bytes2Object(bytes);
    if (msg != null) {
      System.out.println("----" + msg.getContent());
    }
  }
}
