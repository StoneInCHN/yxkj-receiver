package com.yxkj.common.client;

import com.yxkj.common.commonenum.CommonEnum;
import com.yxkj.common.commonenum.Constant;
import com.yxkj.common.entity.CmdMsg;
import com.yxkj.common.utils.ObjectUtil;
import com.yxkj.common.utils.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yxkj.common.commonenum.CommonEnum.CmdType.APP_UPDATE;

/**
 * 服务器发送上位机指令sdk client
 */
@SuppressWarnings("unused")
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
    cmdMsg.setType(CommonEnum.CmdType.AD_UPDATE);
    cmdMsg.setContent(map);
    cmdMsg.setDeviceNo(deviceNo);
    cmdMsg.setId(recordId);
    JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));

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
    cmdMsg.setType(CommonEnum.CmdType.VOLUME);
    cmdMsg.setDeviceNo(deviceNo);
    cmdMsg.setId(recordId);
    JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));
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
        JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    return cmdMsgList;
  }

  /**
   * 通知类型消息
   *
   * @param deviceNo 设备号
   * @return
   * @throws IOException
   */
  public CmdMsg appUpdateCmd(String deviceNo, Long recordId, String url) throws IOException {
    logger.debug("deviceNo: %s;url:%s", deviceNo, url);
    CmdMsg cmdMsg = new CmdMsg();
    cmdMsg.setType(APP_UPDATE);
    Map<String, String> mapContent = new HashMap<>();
    mapContent.put(APP_UPDATE.name(), url);
    cmdMsg.setContent(mapContent);
    cmdMsg.setDeviceNo(deviceNo);
    cmdMsg.setId(recordId);
    JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));
    return cmdMsg;
  }

  /**
   * 通知类型消息
   *
   * @param deviceNo 设备号
   * @return
   * @throws IOException
   */
  public CmdMsg notificationCmd(String deviceNo, Long recordId, CommonEnum.CmdType cmdType)
      throws IOException {
    logger.debug("deviceNo: %s;cmdType:%s", deviceNo, cmdType.toString());
    CmdMsg cmdMsg = new CmdMsg();
    cmdMsg.setType(cmdType);
    cmdMsg.setDeviceNo(deviceNo);
    cmdMsg.setId(recordId);
    JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));
    return cmdMsg;
  }

  /**
   * 出货测试
   * 
   * @param deviceNo 设备号
   * @param recordId 记录Id
   * @param address 货柜号
   * @param addressType 货柜类型
   * @param channelSn 货道号
   * @return
   * @throws IOException
   */
  public CmdMsg sellOutTest(String deviceNo, Long recordId, String address, int addressType,
      String channelSn) throws IOException {
    logger.debug("deviceNo: %s;address:%s,addressType:%s,channelSn:%s", deviceNo, address,
        addressType, channelSn);
    CmdMsg cmdMsg = new CmdMsg();
    cmdMsg.setAddress(address);
    cmdMsg.setAddressType(addressType);
    cmdMsg.setType(CommonEnum.CmdType.SELL_OUT_TEST);
    cmdMsg.setBox(Integer.parseInt(channelSn.substring(1, channelSn.length())));
    cmdMsg.setDeviceNo(deviceNo);
    Map<String, String> contentMap = new HashMap<>();
    contentMap.put("sellOutTest", "true");
    cmdMsg.setContent(contentMap);
    cmdMsg.setId(recordId);
    JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));
    return cmdMsg;
  }

  public CmdMsg reboot(String deviceNo, Long recordId) throws IOException {
    logger.debug("deviceNo: %s;address:%s,addressType:%s,channelSn:%s", deviceNo);
    CmdMsg cmdMsg = new CmdMsg();
    cmdMsg.setType(CommonEnum.CmdType.RE_BOOT);
    cmdMsg.setDeviceNo(deviceNo);
    cmdMsg.setId(recordId);
    JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), ObjectUtil.object2Bytes(cmdMsg));
    return cmdMsg;
  }
}
