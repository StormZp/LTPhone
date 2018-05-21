package com.netphone.netsdk.listener;

/**
 * Created Storm <p>
 * Time    2018/5/21 10:22 <p>
 * Message {广播接收监听}
 */
public interface OnBroadcastListener {
    void onSend();

    void onStop();

    void onReceiver();
}
