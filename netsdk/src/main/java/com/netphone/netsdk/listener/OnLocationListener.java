package com.netphone.netsdk.listener;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:27<p>
 * Message {位置发送监听}
 */
public interface OnLocationListener {
    void onError();

    void onSendSuccess();

    void onSendFail();

    void onHelpSuccess();

    void onHelpFail();

}
