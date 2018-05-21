package com.netphone.netsdk.listener;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:24<p>
 * Message {进群回调监听}
 */
public interface OnGroupComeInListener {
    void onComeInSuccess();
    void onComeInFail(int code,String errorMessage);
}
