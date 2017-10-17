package com.yxkj.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties prop = null;

    static {
        initProperty("config.properties");
    }

    /**
     * 初始化Properties实例
     *
     * @param propertyName
     * @throws Exception
     */
    public synchronized static void initProperty(String propertyName) {
        if (prop == null) {
            prop = new Properties();
            InputStream inputstream = null;
            ClassLoader cl = PropertiesUtil.class.getClassLoader();
            if (cl != null) {
                inputstream = cl.getResourceAsStream(propertyName);
            } else {
                inputstream = ClassLoader.getSystemResourceAsStream(propertyName);
            }

            try {
                prop.load(inputstream);
                inputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取数据
     *
     * @param key 键值
     * @return
     */
    public static String getValueByKey(String key) {
        String result = "";
        try {
            result = prop.getProperty(key);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 读取数据
     *
     * @param key 键值
     * @param def 默认值
     * @return
     */
    public static String getValueByKey(String key, String def) {
        String result = "";
        try {
            result = prop.getProperty(key, def);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] s) {

        try {

            System.out.println(PropertiesUtil.getValueByKey("jedis.ip"));
            System.out.println(PropertiesUtil.getValueByKey("jedis.ip","127.0.0.1"));

        } catch (Exception e) {

            e.printStackTrace();
        }


    }
}
