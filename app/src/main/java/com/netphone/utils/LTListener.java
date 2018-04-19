package com.netphone.utils;

import com.netphone.config.EventConfig;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.base.AppBean;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.listener.OnGetGroupMemberListener;
import com.netphone.netsdk.listener.OnGroupChatListener;
import com.netphone.netsdk.listener.OnGroupComeInListener;
import com.netphone.netsdk.listener.OnGroupStateListener;
import com.netphone.netsdk.listener.OnNetworkListener;
import com.netphone.netsdk.utils.EventBusUtil;

import java.util.List;

/**
 * Created by XYSM on 2018/4/18.
 */

public class LTListener {
    private static LTListener mListener;


    public static LTListener newInstance() {
        if (mListener == null) {
            mListener = new LTListener();
        }
        return mListener;
    }

    public LTListener() {
        initNetListener();
    }

    private void initNetListener() {
        LTConfigure.getInstance().setOnNetworkListener(new OnNetworkListener() {
            @Override
            public void onNoNet() {

            }

            @Override
            public void onWifiNet() {

            }

            @Override
            public void onMobileNet() {

            }

            @Override
            public void onServiceConnect() {

            }
        });
    }

    public void joinGroupListener(String id) {
        LTConfigure.getInstance().getLtApi().joinGroup(id, new OnGroupComeInListener() {
            @Override
            public void onComeInSuccess() {

            }

            @Override
            public void onComeInFail(int code, String errorMessage) {

            }
        }, new OnGetGroupMemberListener() {
            @Override
            public void onGetMemberFail() {

            }

            @Override
            public void onGetMemberSuccess(List<UserInfoBean> members) {

            }
        }, new OnGroupStateListener() {
            @Override
            public void onMenberExit(UserInfoBean member) {

            }

            @Override
            public void onMenberJoin(UserInfoBean member) {

            }

            @Override
            public void onMenberhaveMac(UserInfoBean member) {

            }

            @Override
            public void onMemberRelaxedMac(UserInfoBean member) {

            }

            @Override
            public void onSystemReLaxedMac() {

            }

            @Override
            public void onGrabWheatSuccess() {

            }

            @Override
            public void onGrabWheatFail(int code, String error) {

            }

            @Override
            public void onRelaxedMacSuccess() {

            }

            @Override
            public void onRelaxedMacFail(int code, String error) {

            }
        }, new OnGroupChatListener() {
            @Override
            public void onReceiverListener(GroupChatMsgBean bean) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.RECEIVER_WORD_GROUP,bean));
            }
        });
    }
}
