package com.netphone.netsdk.listener;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnErrorListener {
    void onOrderError();
    void onNotLogin(String error);
    void onCRCError();
    void onError();
}
