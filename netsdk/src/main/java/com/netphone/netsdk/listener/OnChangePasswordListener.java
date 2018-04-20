package com.netphone.netsdk.listener;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnChangePasswordListener {
    void onSuccess();

    void onFail(int code, String error);
}
