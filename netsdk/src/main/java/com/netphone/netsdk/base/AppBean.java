package com.netphone.netsdk.base;

import java.io.Serializable;

/**
 * Created by lgp on 2017/5/22.
 */

public class AppBean<T> implements Serializable {

    private int code;
    private int enumcode;
    private String msg;
    private T data;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getEnumcode() {
        return enumcode;
    }

    public void setEnumcode(int enumcode) {
        this.enumcode = enumcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public AppBean(int code,
                   int enumcode,
                   String msg,
                   T data) {
        this.data = data;
        this.code = code;
        this.enumcode = enumcode;
        this.msg = msg;
    }

    public AppBean(int code,
                   T data) {
        this.data = data;
        this.code = code;
    }

    public AppBean() {
    }
}