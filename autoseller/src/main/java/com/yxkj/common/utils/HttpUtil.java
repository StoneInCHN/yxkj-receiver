package com.yxkj.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

  private static final CloseableHttpClient httpclient = HttpClients.createDefault();

  /**
   * 发送HttpGet请求
   * 
   * @param url
   * @return
   */
  public static String sendGet(String url) {

    HttpGet httpget = new HttpGet(url);
    CloseableHttpResponse response = null;
    try {
      response = httpclient.execute(httpget);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    String result = null;
    try {
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        result = EntityUtils.toString(entity);
      }
    } catch (ParseException | IOException e) {
      e.printStackTrace();
    } finally {
      try {
        response.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  /**
   * 发送HttpPost请求，参数为map
   * 
   * @param url
   * @param map
   * @return
   */
  public static String sendPost(String url, Map<String, String> map) {
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    }


    ObjectMapper mapper = new ObjectMapper(); // 转换器
    CloseableHttpResponse response = null;


    try {
      String json = mapper.writeValueAsString(map); // 将对象转换成json
      StringEntity entity = new StringEntity(json.toString(), "utf-8");// 解决中文乱码问题
      entity.setContentType("application/json");
      HttpPost httppost = new HttpPost(url);
      httppost.setEntity(entity);
      response = httpclient.execute(httppost);
    } catch (IOException e) {
      e.printStackTrace();
    }
    HttpEntity entity1 = response.getEntity();
    String result = null;
    try {
      result = EntityUtils.toString(entity1);
    } catch (ParseException | IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 发送不带参数的HttpPost请求
   * 
   * @param url
   * @return
   */
  public static String sendPost(String url) {
    HttpPost httppost = new HttpPost(url);
    CloseableHttpResponse response = null;
    try {
      response = httpclient.execute(httppost);
    } catch (IOException e) {
      e.printStackTrace();
    }
    HttpEntity entity = response.getEntity();
    String result = null;
    try {
      result = EntityUtils.toString(entity);
    } catch (ParseException | IOException e) {
      e.printStackTrace();
    }
    return result;
  }



  public static void main(String[] args) {
    // 发送 GET 请求
    // String s=HttpUtil.sendGet("http://localhost:8082/cmd/getCurrentVolume", "");
    // System.out.println(s);

    // 发送 POST 请求
    Map<String, String> params = new HashMap<>();
    params.put("deviceNo", "863010031227460");
    params.put("connectStatus", "true");
    String response = HttpUtil.sendPost(
        PropertiesUtil.getValueByKey("server.url") + "/cmd/connectionStatusUpdate", params);
    System.out.println(response);
  }
}
