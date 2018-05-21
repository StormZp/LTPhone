package com.netphone.netsdk.listener;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:32<p>
 * Message {上传回调}
 */
public interface OnUpFileListener {
    void upSuccess(String path);

    void upFail();
}
