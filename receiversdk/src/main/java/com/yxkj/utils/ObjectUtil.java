package com.yxkj.utils;

import java.io.*;

/**
 * @author huyong
 * @since 2017/9/23
 */
public class ObjectUtil {
    /**
     * 对象转byte[]
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] object2Bytes(Object obj) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        byte[] bytes = bo.toByteArray();
        bo.close();
        oo.close();
        return bytes;
    }

    /**
     * byte[]转对象
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static Object bytes2Object(byte[] bytes) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = new ObjectInputStream(in);
        return sIn.readObject();
    }
}
