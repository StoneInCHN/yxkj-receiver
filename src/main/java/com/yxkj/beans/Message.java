package com.yxkj.beans;

import java.io.Serializable;

/**
 * 与中控通信的消息类
 * @author huyong
 * @since 2017/9/23
 */
public class Message implements Serializable {
    private Long id;

    /**
     * 消息内容
     */
    private String content;


}
