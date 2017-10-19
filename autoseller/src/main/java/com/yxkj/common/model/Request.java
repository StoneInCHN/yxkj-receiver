package com.yxkj.common.model;

import io.netty.util.concurrent.DefaultPromise;

/**
 * Request消息对象
 *
 * @author huyong
 * @since 2017/9/19
 */
public class Request {
    private String msg;
    private DefaultPromise promise;

    public Request(String msg, DefaultPromise promise) {
        this.msg = msg;
        this.promise = promise;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DefaultPromise getPromise() {
        return promise;
    }

    public void setPromise(DefaultPromise promise) {
        this.promise = promise;
    }
}
