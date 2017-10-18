package com.yxkj.client;

import com.yxkj.commonenum.CommonEnum;
import com.yxkj.entity.CmdMsg;
import com.yxkj.utils.JedisUtil;
import com.yxkj.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yxkj.commonenum.Constant.JEDIS_MESSAGE_KEY;

/**
 * 服务器发送上位机指令sdk client
 */
public class ReceiverClient {

  private Logger logger = LoggerFactory.getLogger(ReceiverClient.class.getSimpleName());

  public ReceiverClient(String redisIp, int redisPort, int redisTimeout, String redisPassword) {
    JedisPoolConfig config = new JedisPoolConfig();
    // config.setMaxActive(5000);
    config.setMaxIdle(256);
    // config.setMaxWait(5000L);
    config.setTestOnBorrow(true);
    config.setTestOnReturn(true);
    config.setTestWhileIdle(true);
    config.setMinEvictableIdleTimeMillis(60000L);
    config.setTimeBetweenEvictionRunsMillis(3000L);
    config.setNumTestsPerEvictionRun(-1);
    JedisUtil.init(config, redisIp, redisPort, redisTimeout, redisPassword);
  }

  /**
   * 更新广告
   *
   * @param deviceNo 设备号
   * @param map 广告位键值对
   * @return
   * @throws IOException
   */
  public CmdMsg updateAdv(String deviceNo, Map<String, String> map, Long recordId)
      throws IOException {

    logger.debug("deviceNo: %s;Content:%s", deviceNo);
    CmdMsg cmdMsg = new CmdMsg();
    cmdMsg.setType(CommonEnum.CmdType.AD_UPDATE.ordinal());
    cmdMsg.setContent(map);
    cmdMsg.setDeviceNo(deviceNo);
    cmdMsg.setId(recordId);
    JedisUtil.rpush(JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));

    return cmdMsg;
  }

  /**
   * 设置声音
   *
   * @param deviceNo 设备号
   * @param volume 声音大小 0-100
   * @return
   * @throws IOException
   */
  public CmdMsg updateAudioVolume(String deviceNo, float volume, Long recordId) throws IOException {
    logger.debug("deviceNo: %s;volume:%f", deviceNo, volume);
    Map<String, String> contentMap = new HashMap<>();
    contentMap.put("volume", String.valueOf(volume));
    CmdMsg cmdMsg = new CmdMsg();
    cmdMsg.setContent(contentMap);
    cmdMsg.setType(CommonEnum.CmdType.VOLUME.ordinal());
    cmdMsg.setDeviceNo(deviceNo);
    cmdMsg.setId(recordId);
    JedisUtil.rpush(JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));
    return cmdMsg;
  }

  /**
   * 出货指令
   *
   * @param cmdMsgList
   * @return
   * @throws IOException
   */
  public List<CmdMsg> sendCmdList(List<CmdMsg> cmdMsgList) throws IOException {
    cmdMsgList.forEach(cmdMsg -> {
      try {
        JedisUtil.rpush(JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    return cmdMsgList;
  }
}
