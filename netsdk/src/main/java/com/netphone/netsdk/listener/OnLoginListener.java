package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.UserInfoBean;


/**
 * Created Storm<p>
 * Time    2018/5/21 10:27<p>
 * Message {登录监听}
 */
public interface OnLoginListener {
    void onSuccess(UserInfoBean bean);
    void onFail(int code,String error);
}
