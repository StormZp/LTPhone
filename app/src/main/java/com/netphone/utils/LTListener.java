package com.netphone.utils;

import com.netphone.config.Constant;
import com.netphone.config.EventConfig;
import com.netphone.netsdk.LTApi;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.base.AppBean;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.listener.OnGetGroupMemberListener;
import com.netphone.netsdk.listener.OnGroupChatListener;
import com.netphone.netsdk.listener.OnGroupComeInListener;
import com.netphone.netsdk.listener.OnGroupStateListener;
import com.netphone.netsdk.listener.OnLocationListener;
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
                EventBusUtil.sendEvent(new AppBean(EventConfig.COME_IN_SUCCESS, null));
            }

            @Override
            public void onComeInFail(int code, String errorMessage) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.COME_IN_FAIL, errorMessage));
            }
        }, new OnGetGroupMemberListener() {
            @Override
            public void onGetMemberFail() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.GET_MEMBER_FAIL, null));
            }

            @Override
            public void onGetMemberSuccess(List<UserInfoBean> members) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.GET_MEMBER_SUCCESS, members));
                Constant.groupsMemberInfo = members;
            }
        }, new OnGroupStateListener() {
            @Override
            public void onMenberExit(UserInfoBean member) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MENBER_EXIT, member));
            }

            @Override
            public void onMenberJoin(UserInfoBean member) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MENBER_JOIN, member));
            }

            @Override
            public void onMenberhaveMac(UserInfoBean member) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MENBER_HAVE_MAC, member));
            }

            @Override
            public void onMemberRelaxedMac(UserInfoBean member) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MEMBER_RELAXE_DMAC, member));
            }

            @Override
            public void onSystemReLaxedMac() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.SYSTEM_RELAXED_MAC, null));
            }

            @Override
            public void onGrabWheatSuccess() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.GRAB_WHEAT_SUCCESS, null));
            }

            @Override
            public void onGrabWheatFail(int code, String error) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.GRAB_WHEAT_FAIL, error));
            }

            @Override
            public void onRelaxedMacSuccess() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.RELAXED_MAC_SUCCESS, null));
            }

            @Override
            public void onRelaxedMacFail(int code, String error) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.RELAXED_MAC_FAIL, error));
            }
        }, new OnGroupChatListener() {
            @Override
            public void onReceiverListener(GroupChatMsgBean bean) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.RECEIVER_WORD_GROUP, bean));
            }
        });
    }

    public void sendLocation(int type, OnLocationListener onLocationListener) {
        if (type == 0) {

            LTApi.newInstance().sendLocation(onLocationListener);
        } else {

            LTApi.newInstance().help(onLocationListener);
        }
    }
}
