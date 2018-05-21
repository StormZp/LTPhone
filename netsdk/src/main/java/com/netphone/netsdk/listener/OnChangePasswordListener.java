package com.netphone.netsdk.listener;

/**
 * Created Storm <p>
 * Time    2018/5/21 10:22<p>
 * Message {修改密码回调}
 */
public interface OnChangePasswordListener {
    void onSuccess();

    void onFail(int code, String error);
}
