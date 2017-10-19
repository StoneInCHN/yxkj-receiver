package com.yxkj.common.handler;

import com.yxkj.common.data.Constant;
import com.yxkj.common.entity.CmdMsg;
import com.yxkj.common.utils.JsonUtils;
import com.yxkj.common.data.SocketClientMapper;
import com.yxkj.common.utils.JedisUtil;
import com.yxkj.common.utils.ObjectUtil;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


/**
 * @author huyong
 * @since 2017/9/19
 */
public class OperationSystemHandlerThread extends Thread {
    private Logger logger = LoggerFactory.getLogger(OperationSystemHandlerThread.class.getSimpleName());
    private static final int DEFAULT_RANGE_FOR_SLEEP = 50; // 随机休眠时间

    @Override
    public void run() {
        try {
            while (true) {
                Random r = new Random();
                long count = JedisUtil.llen(Constant.JEDIS_MESSAGE_KEY.getBytes());

                // 判断队列是否有数据
                if (count > 0) {
                    logger.debug("Redis msg size:" + count);
                    //从jedis队列弹出数据
                    byte[] bytes = JedisUtil.rpop(Constant.JEDIS_MESSAGE_KEY.getBytes());

                    if (null != bytes) {
                        CmdMsg msg = (CmdMsg) ObjectUtil.bytes2Object(bytes);
                        //后台命令处理逻辑
                        SocketChannel socketChannel = SocketClientMapper.getSocketChannel(msg.getDeviceNo());
                        if (socketChannel == null) {
                            logger.debug("设备不存在！！忽略命令：" + msg);
                            //如果设备不存在，忽略该命令
//                            JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), bytes);
                            continue;
                        }
                        logger.debug("发送命令：" + msg);
                        String cmd = JsonUtils.toJson(msg) + "$_$";
                        socketChannel.writeAndFlush(cmd);

                    }
                } else {
                    Thread.sleep(r.nextInt(DEFAULT_RANGE_FOR_SLEEP));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
