package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.UserInfoBean;

/**
 * Created by XYSM on 2018/5/15.
 */

public interface OnManagerListener {
    void voice(UserInfoBean userBean);//管理者通话(PC端提示)

    void listener();//监听

    void listenerStop();//停止监听

    void receiveListener();//接收监听
}
