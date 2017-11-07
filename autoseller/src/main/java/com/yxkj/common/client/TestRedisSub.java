package com.yxkj.common.client;

import com.yxkj.common.commonenum.Constant;
import com.yxkj.common.handler.RedisMQHandler;
import com.yxkj.common.utils.JedisUtil;

public class TestRedisSub {
  public static void main(String[] args) {
    new Thread(new Runnable() {
      @Override
      public void run() {
        JedisUtil.subscribe(new RedisMQHandler(), Constant.JEDIS_MESSAGE_KEY.getBytes());
      }
    }).start();
  }
}
