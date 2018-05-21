package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.UserInfoBean;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:27<p>
 * Message {管理者监听<p>
 *          当当前账号是管理者账号时,会有这些方法
 * }
 */
public interface OnManagerListener {
    /**
     * 管理者通话(PC端提示)
     * @param userBean
     */
    void voice(UserInfoBean userBean);//

    /**
     * 监听
     */
    void listener();//监听

    /**
     * 停止监听
     */
    void listenerStop();//

    /**
     * 接收监听
     */
    void receiveListener();//
}
