package com.netphone.netsdk.listener;

/**
 * Created Storm <p>
 * Time    2018/5/21 10:22<p>
 * Message {异常回调监听}
 */
public interface OnErrorListener {
    void onOrderError();
    void onNotLogin();
    void onCRCError();
    void onError();
}
