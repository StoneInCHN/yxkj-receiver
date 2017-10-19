package com.yxkj.common.entity;

import com.yxkj.common.commonenum.CommonEnum;

import java.io.Serializable;
import java.util.Map;

/**
 * @author huyong
 * @since 2017/9/26
 */
public class CmdMsg implements Serializable {
  private static final long serialVersionUID = -5098028238072646730L;
  private Long id;

  /**
   * 消息内容
   */
  private Map<String, String> content;

  /**
   * 货柜号
   */
  private String address;

  /**
   * 货柜类型 1：弹簧柜 2：格子柜
   */
  private int addressType;
  /**
   * 货道号
   */
  private int box;

  /**
   * 设备号
   */
  private String deviceNo;

  /**
   * 消息类型 0.出货指令 1.APP更新指令 2.广告更新指令 3.声音设置指令
   */
  private CommonEnum.CmdType type;

  public Map<String, String> getContent() {
    return content;
  }

  public String getContentString() {
    StringBuilder contentStr = new StringBuilder();
    for (String k : content.keySet()) {
      contentStr.append(k);
      contentStr.append(content.get(k));
      contentStr.append(";");
    }
    return contentStr.toString();
  }

  public void setContent(Map<String, String> content) {
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDeviceNo() {
    return deviceNo;
  }

  public void setDeviceNo(String deviceNo) {
    this.deviceNo = deviceNo;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getBox() {
    return box;
  }

  public void setBox(int box) {
    this.box = box;
  }

  public int getAddressType() {
    return addressType;
  }

  public void setAddressType(int addressType) {
    this.addressType = addressType;
  }

  public CommonEnum.CmdType getType() {
    return type;
  }

  public void setType(CommonEnum.CmdType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "CmdMsg{" + "id=" + id + ", content='" + content + '\'' + ", address='" + address + '\''
        + ", addressType=" + addressType + ", box=" + box + ", deviceNo='" + deviceNo + '\''
        + ", type=" + type + '}';
  }
}
