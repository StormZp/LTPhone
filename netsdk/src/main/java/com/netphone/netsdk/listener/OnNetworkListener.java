package com.netphone.netsdk.listener;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnNetworkListener {
    void onNoNet();
    void onWifiNet();
    void onMobileNet();
    void onConnectFail();
    void onConnectSuccess();
}
