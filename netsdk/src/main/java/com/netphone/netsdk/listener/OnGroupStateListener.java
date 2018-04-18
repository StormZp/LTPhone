package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.UserInfoBean;

/**
 * Created storm
 * Time    2018/4/18 9:43
 * Message {群里的状态}
 */

public interface OnGroupStateListener {
    void onMenberExit(UserInfoBean member);

    void onMenberJoin(UserInfoBean member);

    void onMenberhaveMac(UserInfoBean member);

    void onMemberRelaxedMac(UserInfoBean member);

    void onSystemReLaxedMac();

    void onGrabWheatSuccess();

    void onGrabWheatFail(int code, String error);

    void onRelaxedMacSuccess();

    void onRelaxedMacFail(int code, String error);


}
