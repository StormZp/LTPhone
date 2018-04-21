package com.netphone.netsdk.bean;

import java.io.Serializable;

/**
 * Created by XYSM on 2018/4/21.
 */
public class BroadcastBean implements Serializable{
    private String       Message;//广播专用
    private String       FromUserId;//广播专用

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getFromUserId() {
        return FromUserId;
    }

    public void setFromUserId(String fromUserId) {
        FromUserId = fromUserId;
    }
}
