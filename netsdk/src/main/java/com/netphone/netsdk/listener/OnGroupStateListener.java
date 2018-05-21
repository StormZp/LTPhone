package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.UserInfoBean;

/**
 * Created storm<p>
 * Time    2018/4/18 9:43<p>
 * Message {群里的状态}
 */

public interface OnGroupStateListener {
    /**
     * 成员退出
     * @param member
     */
    void onMenberExit(UserInfoBean member);

    /**
     * 成员加入
     *
     * @param member
     */
    void onMenberJoin(UserInfoBean member);

    /**
     * 成员获取麦克风
     * @param member
     */
    void onMenberhaveMac(UserInfoBean member);

    /**
     * 成员释放麦克风
     * @param member
     */
    void onMemberRelaxedMac(UserInfoBean member);

    /**
     * 系统释放麦克风
     * @param member
     */
    void onSystemReLaxedMac();

    /**
     * 抢麦克风成功
     * @param member
     */
    void onGrabWheatSuccess();

    /**
     * 抢麦失败
     * @param code
     * @param error
     */
    void onGrabWheatFail(int code, String error);
    /**
     * 释放麦克风成功
     */
    void onRelaxedMacSuccess();

    /**
     * 成员释放麦克风失败
     * @param code
     * @param error
     */
    void onRelaxedMacFail(int code, String error);


}
