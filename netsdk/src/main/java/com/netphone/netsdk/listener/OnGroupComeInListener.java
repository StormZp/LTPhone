package com.netphone.netsdk.listener;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnGroupComeInListener {
    void onComeInSuccess();
    void onComeInFail(int code,String errorMessage);
}
