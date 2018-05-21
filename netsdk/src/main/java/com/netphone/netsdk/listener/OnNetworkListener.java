package com.netphone.netsdk.listener;


/**
 * Created Storm<p>
 * Time    2018/5/21 10:31<p>
 * Message {网络状态监听回调}
 */
public interface OnNetworkListener {
    void onNoNet();
    void onWifiNet();
    void onMobileNet();
    void onConnectFail();
    void onConnectSuccess();
}
