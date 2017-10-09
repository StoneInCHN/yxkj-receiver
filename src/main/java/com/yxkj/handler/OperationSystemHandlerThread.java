package com.yxkj.handler;

import com.yxkj.beans.CmdMsg;
import com.yxkj.data.Constant;
import com.yxkj.data.SocketClientMapper;
import com.yxkj.utils.JedisUtil;
import com.yxkj.utils.ObjectUtil;
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
                            logger.debug("No such deviceNo");
                            JedisUtil.rpush(Constant.JEDIS_MESSAGE_KEY.getBytes(), bytes);
                            continue;
                        }
                        socketChannel.writeAndFlush(msg.getCmdStr());

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