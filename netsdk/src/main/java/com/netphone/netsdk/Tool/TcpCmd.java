package com.netphone.netsdk.Tool;

import android.content.Context;
import android.media.AudioManager;
import android.os.SystemClock;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netphone.gen.FriendChatMsgBeanDao;
import com.netphone.gen.GroupChatMsgBeanDao;
import com.netphone.gen.GroupInfoBeanDao;
import com.netphone.gen.ImageBeanDao;
import com.netphone.gen.UserInfoBeanDao;
import com.netphone.netsdk.LTApi;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.R;
import com.netphone.netsdk.bean.BroadcastBean;
import com.netphone.netsdk.bean.FriendChatMsgBean;
import com.netphone.netsdk.bean.FriendReFreshBean;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.GroupComeBean;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.bean.GroupReFreshBean;
import com.netphone.netsdk.bean.ImageBean;
import com.netphone.netsdk.bean.PortBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.bean.UserListBean;
import com.netphone.netsdk.socket.TcpSocket;
import com.netphone.netsdk.socket.UdpSocket;
import com.netphone.netsdk.utils.ByteIntUtils;
import com.netphone.netsdk.utils.ByteUtil;
import com.netphone.netsdk.utils.CmdUtils;
import com.netphone.netsdk.utils.DataTypeChangeHelper;
import com.netphone.netsdk.utils.HttpDownloader;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.netsdk.utils.ReplyUtil;
import com.netphone.netsdk.utils.SharedPreferenceUtil;
import com.netphone.netsdk.utils.UdpUtil;
import com.test.jni.VoiceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by XYSM on 2018/4/13.
 */

public class TcpCmd {

    private InetAddress addr = null;
    private int                  port;
    private UserInfoBeanDao      mUserInfoBeanDao;
    private GroupInfoBeanDao     mGroupInfoBeanDao;
    private FriendChatMsgBeanDao mFriendChatMsgBeanDao;
    private ImageBeanDao         mImageBeanDao;
    private Gson mGson = new Gson();

    public void cmdData(byte[] pagBytes) {
        byte[] tempBytes = new byte[2];
        System.arraycopy(pagBytes, 9, tempBytes, 0, 2);
        int    bodyLength = DataTypeChangeHelper.byte2int(tempBytes);
        byte[] bodyBytes  = null;
        if (bodyLength > 0) {
            //对指令内容进行拷贝
            bodyBytes = new byte[bodyLength];
            System.arraycopy(pagBytes, 11, bodyBytes, 0, bodyLength);
        }
        cmdExplore(pagBytes, bodyBytes);
    }

    private void cmdExplore(byte[] pagBytes, byte[] bodyBytes) {
        if (mUserInfoBeanDao == null) {
            mUserInfoBeanDao = LTConfigure.getInstance().getDaoSession().getUserInfoBeanDao();
        }
        if (mGroupInfoBeanDao == null) {
            mGroupInfoBeanDao = LTConfigure.getInstance().getDaoSession().getGroupInfoBeanDao();
        }
        if (mImageBeanDao == null) {
            mImageBeanDao = LTConfigure.getInstance().getDaoSession().getImageBeanDao();
        }
        String body = "";
        LogUtil.error("TcpCmd", "64\tcmdExplore()\n" + String.format("pagBytes[7] == %x && pagBytes[8] == %x", pagBytes[7], pagBytes[8]));
        switch (pagBytes[7]) {
            case 0x00://终端>>服务端指令列表
                switch (pagBytes[8]) {

                    case 0x00://登录回复

                        switch (bodyBytes[0]) {
                            case 0x00://登录成功
                                mUserInfoBeanDao.deleteAll();
                                mGroupInfoBeanDao.deleteAll();

                                byte[] jsonBytes = new byte[bodyBytes.length - 1];
                                System.arraycopy(bodyBytes, 1, jsonBytes, 0, bodyBytes.length - 1);
                                body = ByteIntUtils.utfToString(jsonBytes);
                                LogUtil.error("TcpCmd", "77\tcmdExplore()\n" + body);
                                UserInfoBean user = mGson.fromJson(body, UserInfoBean.class);
                                if (user == null) {
                                    LogUtil.error("user =null");
                                    return;
                                } else {
                                    LogUtil.error("user= " + user.toString());
                                }
                                isConnectBeat = true;
                                Constant.isOnline = true;
                                SharedPreferenceUtil.Companion.put(Constant.UserId, user.getUserId());
                                new Thread(BeatRunnable).start();
                                mUserInfoBeanDao.insertOrReplace(user);
                                if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null)
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onSuccess(user);
                                break;
                            case 0x01://登录失败
                                if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null)
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onFail(1, LTConfigure.getInstance().getContext().getResources().getString(R.string.account_or_password_mistake));
                                break;
                            case 0x02://该终端被遥弊
                                if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null)
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onFail(2, LTConfigure.getInstance().getContext().getResources().getString(R.string.terminal_malpractices));
                                break;
                            case 0x03://账号过期
                                if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null)
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onFail(3, LTConfigure.getInstance().getContext().getResources().getString(R.string.Account_expiration));
                                break;
                            case 0x08://该账号已经登录
                                if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null)
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onFail(8, LTConfigure.getInstance().getContext().getResources().getString(R.string.account_already_login));
                                break;

                        }
                        break;
                    case 0x01://上传GPS的回复
                        if (LTConfigure.getInstance().getLtApi().onLocationListener != null) {
                            if (bodyBytes[0] == 0x00) {
                                LTConfigure.getInstance().getLtApi().onLocationListener.onSendSuccess();
                            } else {
                                LTConfigure.getInstance().getLtApi().onLocationListener.onSendFail();
                            }
                            LTConfigure.getInstance().getLtApi().onLocationListener = null;
                        }
                        break;
                    case 0x02://主动发起语音通话的回复
                        LogUtil.error("TcpCmd", "137\tcmdExplore()0x02\n" + bodyBytes[0]);
                        if (LTApi.getInstance().onFriendCallListener != null)
                            switch (bodyBytes[0]) {
                                case 0x00:
                                    LTApi.getInstance().onFriendCallListener.onCallAccept();
                                    break;//Setting_call_connection
                                case 0x01:
                                    LTApi.getInstance().onFriendCallListener.onCallReject();
                                    break;//refuse_class
                                case 0x03:
                                    LTApi.getInstance().onFriendCallListener.onCallFail(0x03, LTConfigure.getInstance().getContext().getResources().getString(R.string.called_calling));
                                    LTApi.getInstance().onFriendCallListener = null;
                                    break;//called_calling
                                case 0x04:
                                    LTApi.getInstance().onFriendCallListener.onCallFail(0x03, LTConfigure.getInstance().getContext().getResources().getString(R.string.time_out_call));
                                    LTApi.getInstance().onFriendCallListener = null;
                                    break;//time_out_call
                                case 0x05:
                                    LTApi.getInstance().onFriendCallListener.onCallFail(0x03, LTConfigure.getInstance().getContext().getResources().getString(R.string.called_line_off));
                                    LTApi.getInstance().onFriendCallListener = null;
                                    break;//called_line_off
                                case 0x07:
                                    LTApi.getInstance().onFriendCallListener.onCallFail(0x03, LTConfigure.getInstance().getContext().getResources().getString(R.string.not_login));
                                    LTApi.getInstance().onFriendCallListener = null;
                                    break;//not_login
                            }
                        break;
                    case 0x03://退出通话(挂断)
                        if (LTApi.getInstance().onFriendCallListener != null) {
                            LTApi.getInstance().onFriendCallListener.onCallFail(0x06, LTConfigure.getInstance().getContext().getResources().getString(R.string.stop_call));
                            LTApi.getInstance().onFriendCallListener = null;
                        }
                        UdpSocket.Companion.getInstance().stopPlay();
                        UdpSocket.Companion.getInstance().stopRecord();

                        break;
                    case 0x04://收到加入群聊回复
                        if (LTConfigure.getInstance().getLtApi().groupComeInListener != null) {
                            if (bodyBytes.length < 5) {
                                LTConfigure.getInstance().getLtApi().groupComeInListener.onComeInFail(0x02, LTConfigure.getInstance().getContext().getResources().getString(R.string.add_fail));
                            }
                            switch (bodyBytes[0]) {
                                case 0x00:
                                    port = ByteUtil.getInt(bodyBytes, 1);//udp端口,占4位
                                    isGroupBeat = true;
                                    LogUtil.error("TcpCmd", "177\tcmdExplore()\n" + "群聊端口号:" + port);
                                    UdpSocket.Companion.getInstance().connect(port);
                                    UdpSocket.Companion.getInstance().play();

                                    if (!TextUtils.isEmpty(LTApi.getInstance().groupId)) {
                                        SharedPreferenceUtil.Companion.put(Constant.currentGroupId, LTApi.getInstance().groupId);
                                        GroupInfoBean unique = mGroupInfoBeanDao.queryBuilder().where(GroupInfoBeanDao.Properties.GroupID.eq(LTApi.getInstance().groupId)).unique();
                                        if (unique != null) {
                                            Constant.currentGroupInfo = unique;
                                        }
                                        LTConfigure.getInstance().getLtApi().groupComeInListener.onComeInSuccess();
                                    }

                                    break;
                                case 0x01:
                                    LTConfigure.getInstance().getLtApi().groupComeInListener.onComeInFail(0x02, LTConfigure.getInstance().getContext().getResources().getString(R.string.add_fail));
                                    break;
                                case 0x07:
                                    LTConfigure.getInstance().getLtApi().groupComeInListener.onComeInFail(0x02, LTConfigure.getInstance().getContext().getResources().getString(R.string.not_login));
                                    break;
                                case 0x05:
                                    LTConfigure.getInstance().getLtApi().groupComeInListener.onComeInFail(0x02, LTConfigure.getInstance().getContext().getResources().getString(R.string.grouid_id_incorrect));
                                    break;
                            }
                        }
                        break;
                    case 0x05://注册用户--已过期
                        break;
                    case 0x06://todo 主动发起视频通话
                        break;
                    case 0x07://todo 挂断视频通话
                        break;
                    case 0x08://发送文字消息
                        body = ByteIntUtils.utfToString(bodyBytes);
                        switch (bodyBytes[0]) {
                            case 0x00:
                                LogUtil.error("TcpCmd", "166\tcmdExplore()\n" + "发送成功");
                                break;
                        }
                        break;
                    case 0x09://取消呼叫
                        break;
                    case 0x0A://退出群聊

                        break;
                    case 0x0B://主动抢麦回复
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            switch (bodyBytes[0]) {
                                case 0x00:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onGrabWheatSuccess();
                                    UdpSocket.Companion.getInstance().record();
                                    break;
                                case 0x01:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onGrabWheatFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.preemption) + LTConfigure.getInstance().getContext().getResources().getString(R.string.fail));

                                    break;
                                case 0x03:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onGrabWheatFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.not_in_group));
                                    break;
                                case 0x04:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onGrabWheatFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.using_mic));
                                    break;
                                case 0x07:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onGrabWheatFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.not_login));
                                    break;
                            }
                        }
                        break;
                    case 0x0C://主动释放麦回复
                        UdpSocket.Companion.getInstance().stopRecord();
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            switch (bodyBytes[0]) {
                                case 0x00:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onRelaxedMacSuccess();

                                    break;
                                case 0x01:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onRelaxedMacFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.release) + LTConfigure.getInstance().getContext().getResources().getString(R.string.fail));

                                    break;
                                case 0x03:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onRelaxedMacFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.not_in_group));
                                    break;
                                case 0x04:
                                    LTConfigure.getInstance().getLtApi().groupStateListener.onRelaxedMacFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.not_mic_premission));
                                    break;
                            }
                        }
                        break;
                    case 0x0D://获取当前终端的通话记录
                        break;
                    case 0x0E://删除某条通话记录
                        break;
                    case 0x0F://上传多媒体内容(图片,视频,文件等)
                        if (LTConfigure.getInstance().getLtApi().onUpFileListener != null) {
                            String path = null;
                            if (bodyBytes.length > 1) {
                                byte[] pack = new byte[bodyBytes.length - 1];
                                System.arraycopy(bodyBytes, 1, pack, 0, bodyBytes.length - 1);
                                path = ByteIntUtils.utfToString(pack);
                            }

                            if (bodyBytes[0] == 0x00) {
                                LTConfigure.getInstance().getLtApi().onUpFileListener.upSuccess(path);
                            } else {
                                LTConfigure.getInstance().getLtApi().onUpFileListener.upFail();
                            }
                            LTConfigure.getInstance().getLtApi().onUpFileListener = null;
                        }
                        break;
                    case 0x10://修改密码
                        if (LTApi.getInstance().onChangePasswordListener != null) {
                            if (bodyBytes[0] == 0x00) {
                                LTApi.getInstance().onChangePasswordListener.onSuccess();
                            } else if (bodyBytes[0] == 0x01) {
                                LTApi.getInstance().onChangePasswordListener.onFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.old_password_fail));
                            } else if (bodyBytes[0] == 0x07) {
                                LTApi.getInstance().onChangePasswordListener.onFail(0x07, LTConfigure.getInstance().getContext().getResources().getString(R.string.not_login));
                            }
                        }
                        break;
                    case 0x11://遥晕
                        break;
                    case 0x12://取消遥晕
                        break;
                    case 0x13://遥弊
                        break;
                    case 0x14://取消遥弊
                        break;
                    case 0x15://获取组织架构
                        break;
                    case 0x16://获取终端用户信息
                        break;
                    case 0x17://更新终端用户信息
                        if (LTConfigure.getInstance().getLtApi().onChangeUserInfoListener != null) {
                            if (bodyBytes[0] == 0x00) {
                                LTConfigure.getInstance().getLtApi().onChangeUserInfoListener.onSuccess();
                            } else {
                                LTConfigure.getInstance().getLtApi().onChangeUserInfoListener.onFail();
                            }
                        }
                        break;
                    case 0x18://一键呼救回复
                        if (LTConfigure.getInstance().getLtApi().onLocationListener != null) {
                            if (bodyBytes[0] == 0x00) {
                                LTConfigure.getInstance().getLtApi().onLocationListener.onHelpSuccess();
                            } else {
                                LTConfigure.getInstance().getLtApi().onLocationListener.onHelpFail();
                            }
                            LTConfigure.getInstance().getLtApi().onLocationListener = null;
                        }
                        break;
                    case 0x19://停止发送广播
                        UdpSocket.Companion.getInstance().stopPlay();
                        break;
                    case 0x1A:// 设置呼叫转移
                        break;
                    case 0x1B://获取群组成员信息
                        if (LTConfigure.getInstance().getLtApi().getGroupMemberListener != null) {
                            switch (bodyBytes[0]) {
                                case 0x00:
                                    byte[] bytes = Arrays.copyOfRange(bodyBytes, 1, bodyBytes.length);
                                    String json = ByteIntUtils.utfToString(bytes);
                                    LogUtil.error("TcpCmd", "226\tcmdExplore()\n" + json);
                                    List<UserInfoBean> bean = mGson.fromJson(json, new TypeToken<List<UserInfoBean>>() {
                                    }.getType());
                                    LTConfigure.getInstance().getLtApi().getGroupMemberListener.onGetMemberSuccess(bean);
                                    break;
                                case 0x01:
                                    LTConfigure.getInstance().getLtApi().getGroupMemberListener.onGetMemberFail();
                                    break;
                            }
                        }
                        break;
                    case 0x1c://发送群聊消息的回复

                        break;
                }
                break;
            case 0x01://服务端>>终端指令列表
                switch (pagBytes[8]) {
                    case 0x00://用户  群推送 --【已作废】

                        body = ByteIntUtils.utfToString(bodyBytes);
//                            LogUtil.saveLog(LTConfigure.getInstance().getContext(), "127\tcmdExplore()\n" + body);
//                            LogUtil.error("TcpCmd", "278\tcmdExplore()\n" + body);
                        try {
                            LogUtil.error("TcpCmd", "255\tcmdExplore()\n" + body);
                            UserListBean       userListBean = mGson.fromJson(body, UserListBean.class);
                            List<UserInfoBean> userInfo     = userListBean.getUserInfo();
                            UserInfoBean       currentInfo  = LTApi.getInstance().getCurrentInfo();
                            for (int i = 0; i < userInfo.size(); i++) {
                                if (!currentInfo.getUserId().equals(userInfo.get(i).getUserId())) {
                                    mUserInfoBeanDao.insertOrReplace(userInfo.get(i));
                                }
                            }
                            for (int i = 0; i < userListBean.getGroupInfo().size(); i++) {
                                userListBean.getGroupInfo().get(i).setUserId(currentInfo.getUserId());
                            }

                            mGroupInfoBeanDao.insertOrReplaceInTx(userListBean.getGroupInfo());
//                            if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null) {
//                                LTConfigure.getInstance().getLtApi().mOnLoginListener.onComplete(userListBean);
//                                LTConfigure.getInstance().getLtApi().mOnLoginListener = null;
//                            }

                        } catch (Exception e) {
                            LogUtil.error("TcpCmd", e);
                        }

                        break;
                    case 0x01://被叫语音通话
                        body = ByteIntUtils.utfToString(bodyBytes);
                        UserInfoBean unique = mUserInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(body)).unique();
                        if (unique != null && LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onFriendVoice(unique);

                        }
                        break;
                    case 0x02://开始语音通话
                        if (LTApi.getInstance().onFriendCallListener != null) {
                            LTApi.getInstance().onFriendCallListener.onCallStart();
                        }
                        port = ByteUtil.getInt(bodyBytes, 0);//udp端口,占4位
                        UdpSocket.Companion.getInstance().connect(port);
                        UdpSocket.Companion.getInstance().play();
                        UdpSocket.Companion.getInstance().record();
                        break;
                    case 0x03://对方挂断语音通话
                        if (LTApi.getInstance().onFriendCallListener != null) {
                            LTApi.getInstance().onFriendCallListener.onCallFail(0x06, LTConfigure.getInstance().getContext().getResources().getString(R.string.stop_call));
                            LTApi.getInstance().onFriendCallListener = null;
                        }
                        UdpSocket.Companion.getInstance().stopRecord();
                        UdpSocket.Companion.getInstance().stopPlay();
                        break;
                    case 0x04:// 被叫视频通话
                        break;
                    case 0x05://
                        break;
                    case 0x06:// 开始视频通话
                        break;
                    case 0x07:// 对方挂断视频通话
                        break;
                    case 0x08://收到文字消息
                        body = ByteIntUtils.utfToString(bodyBytes);
                        LogUtil.error("TcpCmd", "342\tcmdExplore()\n" + body);

                        FriendChatMsgBean friendChatMsgBean = mGson.fromJson(body, FriendChatMsgBean.class);

                        friendChatMsgBean.setSendId(friendChatMsgBean.getReceiveId());
                        friendChatMsgBean.setUserId(Constant.info.getUserId());
                        friendChatMsgBean.setDateTime(System.currentTimeMillis());
                        if (mFriendChatMsgBeanDao == null)
                            mFriendChatMsgBeanDao = LTConfigure.getInstance().getDaoSession().getFriendChatMsgBeanDao();
                        mFriendChatMsgBeanDao.insertOrReplace(friendChatMsgBean);

                        ReplyUtil.insertMsg(friendChatMsgBean.getReceiveId(), friendChatMsgBean.getName(), friendChatMsgBean.getUserId(), friendChatMsgBean.getMsg());
                        if (LTApi.getInstance().onReFreshListener != null) {
                            LTApi.getInstance().onReFreshListener.onFriendChatMsg(friendChatMsgBean);
                        }
                        break;
                    case 0x09://当有新的终端加入到当前的活动群聊中时
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            body = ByteIntUtils.utfToString(bodyBytes);
                            UserInfoBean user = mGson.fromJson(body, UserInfoBean.class);
                            LTConfigure.getInstance().getLtApi().groupStateListener.onMenberJoin(user);
                        }

                        break;
                    case 0x0A://当现有终端退出了当前的活动群聊中时
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            body = ByteIntUtils.utfToString(bodyBytes);
                            UserInfoBean user = mGson.fromJson(body, UserInfoBean.class);
                            LTConfigure.getInstance().getLtApi().groupStateListener.onMenberExit(user);
                        }
                        break;
                    case 0x0B://在群聊中时,麦权被抢占
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            body = ByteIntUtils.utfToString(bodyBytes);
                            UserInfoBean user = mGson.fromJson(body, UserInfoBean.class);
                            LTConfigure.getInstance().getLtApi().groupStateListener.onMenberhaveMac(user);
                        }
                        break;
                    case 0x0C://麦权被释放
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            body = ByteIntUtils.utfToString(bodyBytes);
                            UserInfoBean user = mGson.fromJson(body, UserInfoBean.class);
                            LTConfigure.getInstance().getLtApi().groupStateListener.onMemberRelaxedMac(user);
                        }
                        break;
                    case 0x0D://麦权被系统回收
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            LTConfigure.getInstance().getLtApi().groupStateListener.onSystemReLaxedMac();
                        }
                        break;
                    case 0x0E://todo被遥晕
                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.dizzy();
                        }
                        break;
                    case 0x0F:// 被取消遥晕
                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.dizzyCancel();
                        }
                        break;
                    case 0x10:// 被遥弊
                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.shake();
                        }
                        break;
                    case 0x11:// 被取消遥弊
                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.shakeCancel();
                        }
                        break;
                    case 0x12:// 当前通话时,通话对象被遥弊,或遥晕

                        break;
                    case 0x13://当用户超出电子围栏范围时
                        if (LTApi.getInstance().onReFreshListener != null) {
                            LTApi.getInstance().onReFreshListener.onElectronWall();
                        }
                        break;
                    case 0x14://收到被挤下线指令
                        LTConfigure.getInstance().onDestory();
                        if (LTApi.getInstance().onReFreshListener != null) {
                            LTApi.getInstance().onReFreshListener.onSqueezeLine();
                            LTApi.getInstance().onReFreshListener = null;
                        }
                        break;
                    case 0x15:// 强制加入群聊
                    {
                        body = ByteIntUtils.utfToString(bodyBytes);
                        GroupComeBean bean = mGson.fromJson(body, GroupComeBean.class);
                        UdpSocket.Companion.getInstance().connect(bean.getPort());
                        UdpSocket.Companion.getInstance().play();

                        GroupInfoBean groupInfoBean = mGroupInfoBeanDao.queryBuilder().where(GroupInfoBeanDao.Properties.GroupID.eq(bean.getGroupId())).build().unique();

                        if (LTApi.getInstance().onReFreshListener != null) {
                            LTApi.getInstance().onReFreshListener.groupCome(groupInfoBean);
                        }
                    }


                    break;
                    case 0x16://收到多媒体内容(图片,视频,文件等)
                        body = ByteIntUtils.utfToString(bodyBytes);
                        ImageBean bean = mGson.fromJson(body, ImageBean.class);
                        bean.setDate(System.currentTimeMillis());
                        bean.setReceiveId(SharedPreferenceUtil.Companion.read(Constant.UserId, ""));
                        mImageBeanDao.insertOrReplace(bean);
                        if (LTApi.getInstance().onReFreshListener != null) {
                            LTApi.getInstance().onReFreshListener.onMultiMedia(bean);
                        }


                        break;
                    case 0x17://todo 组呼中,被系统分配了权限
                        break;
                    case 0x18://todo 被发起主叫(从调度台){
                    {
                        body = ByteIntUtils.utfToString(bodyBytes);
                        UserInfoBean userbean = mUserInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(body)).build().unique();
//                        if (userbean != null && LTConfigure.getInstance().getLtApi().onReFreshListener != null) {// void onManagerVoice(UserInfoBean userBean);//管理者通话(PC端提示)
//                            LTConfigure.getInstance().getLtApi().onReFreshListener.onManagerVoice(userbean);
//                        }
                    }
                    break;
                    case 0x19:// 服务端发起监听(被监听)
                    {
                        port = ByteUtil.getInt(bodyBytes, 0);//udp端口,占4位
                        UdpSocket.Companion.getInstance().connect(port);
                        UdpSocket.Companion.getInstance().record();
                    }

                    break;
                    case 0x1A:// 服务端停止监听(被监听)
                        UdpSocket.Companion.getInstance().stopRecord();
                        break;
                    case 0x1D://todo 正在监听终端(主动),是否d同步播放
                        break;
                    case 0x1E:// 监听(主动)已被中断
                        UdpSocket.Companion.getInstance().stopPlay();
                        break;
                    case 0x1F://从服务台发起广播(发起方) 收到端口号
//                        port = ByteUtil.getInt(bodyBytes, 0);//udp 端口
                        if (LTApi.getInstance().onBroadcastListener != null) {
                            LTApi.getInstance().onBroadcastListener.onSend();
                        }
                        break;
                    case 0x20://广播到达最大时长(发起方) 中断广播
                        if (LTApi.getInstance().onBroadcastListener != null) {
                            LTApi.getInstance().onBroadcastListener.onStop();
                        }
                        break;
                    case 0x21://开始接收广播内容
                        //                        port = ByteUtil.getInt(bodyBytes, 0);//udp 端口
                        if (LTApi.getInstance().onBroadcastListener != null) {
                            LTApi.getInstance().onBroadcastListener.onReceiver();
                        }
                        break;
                    case 0x22://停止接收广播内容
                        if (LTApi.getInstance().onBroadcastListener != null) {
                            LTApi.getInstance().onBroadcastListener.onStop();
                        }
                        break;
                    case 0x23:// 开始播放监听内容
                    {
                        body = ByteIntUtils.utfToString(bodyBytes);
                        Gson     gson = new Gson();
                        PortBean info = gson.fromJson(body, PortBean.class);

                        UdpSocket.Companion.getInstance().connect(Integer.parseInt(info.getPort()));
                        UdpSocket.Companion.getInstance().play();

                        if (LTApi.getInstance().onManagerListener != null) {
                            LTApi.getInstance().onManagerListener.listener();
                        }

                        setSound();
                    }
                    break;
                    case 0x24:// 停止播放监听内容
                        UdpSocket.Companion.getInstance().stopPlay();
                        if (LTApi.getInstance().onManagerListener != null) {
                            LTApi.getInstance().onManagerListener.listenerStop();
                        }
                        break;
                    case 0x25:// 播放监听文件
                    {
                        body = ByteIntUtils.utfToString(bodyBytes);
                        String path = TcpConfig.URL + body;

                        setSound();


                        //创建文件
                        String save = LTConfigure.mContext.getExternalFilesDir("").getAbsolutePath() + "/temp";
                        File   file = new File(save);
                        if (file.exists()) {
                            file.delete();
                        }
                        int temp = new HttpDownloader().downloadFiles(path, LTConfigure.mContext.getExternalFilesDir("").getAbsolutePath(), "temp");
                        if (LTApi.getInstance().onManagerListener != null) {
                            LTApi.getInstance().onManagerListener.receiveListener();
                        }
                        if (temp == 0) {
                            try {
                                FileInputStream stream = new FileInputStream(file);
                                byte[]          bytes  = new byte[VoiceUtil.BUFFER_SIZE + 10];
                                byte[]          pack;
                                VoiceUtil.getInstance().initPlayer();
                                while (stream.read(bytes) != -1) {
                                    pack = UdpUtil.udpDataUncode(bytes);
                                    VoiceUtil.getInstance().setPlayData(pack);
                                }
                            } catch (IOException e) {
                                LogUtil.error("641\tcmdExplore()\n", e);
//                                e.printStackTrace();
                            }
                            VoiceUtil.getInstance().stopPlayer();
                        }
                    }
                    break;
                    case 0x26://收到群聊消息
                        body = ByteIntUtils.utfToString(bodyBytes);
                        LogUtil.error("TcpCmd", "356\tcmdExplore()\n" + body);
                        GroupChatMsgBean msg = mGson.fromJson(body, GroupChatMsgBean.class);
                        msg.setDateTime(System.currentTimeMillis());
                        msg.setReceiveId(SharedPreferenceUtil.Companion.read(Constant.UserId, ""));

                        GroupChatMsgBeanDao groupInfoBeanDao = LTConfigure.getInstance().getDaoSession().getGroupChatMsgBeanDao();
                        groupInfoBeanDao.insertOrReplace(msg);

                        if (LTApi.getInstance().groupChatListener != null) {
                            LTApi.getInstance().groupChatListener.onReceiverListener(msg);
                        }
                        break;
                    case 0x27://收到文字广播
                        body = ByteIntUtils.utfToString(bodyBytes);
                        LogUtil.error("TcpCmd", "435\tcmdExplore()\n" + body);
                        BroadcastBean msg2 = mGson.fromJson(body, BroadcastBean.class);

                        if (LTApi.getInstance().onReFreshListener != null) {
                            LTApi.getInstance().onReFreshListener.onWordBroadcast(msg2);
                        }
                        break;
                    case 0x28://群组成员变更
                        body = ByteIntUtils.utfToString(bodyBytes);
                        LogUtil.error("TcpCmd", "568\tcmdExplore()\n" + body);
                        GroupInfoBean group = mGson.fromJson(body, GroupInfoBean.class);
                        mGroupInfoBeanDao.insertOrReplace(group);
//                        if (LTApi.getInstance().onReFreshListener != null) {
//                            LTApi.getInstance().onReFreshListener.onGroupReFresh(group);
//                        }
                        break;
                    case 0x29://群组麦权变更
                        body = ByteIntUtils.utfToString(bodyBytes);
                        LogUtil.error("TcpCmd", "568\tcmdExplore()\n" + body);
                        GroupInfoBean group2 = mGson.fromJson(body, GroupInfoBean.class);
                        LogUtil.error("TcpCmd", "568\tcmdExplore()\n" + new Gson().toJson(group2));

                        if (LTApi.getInstance().onReFreshListener != null) {
                            LTApi.getInstance().onReFreshListener.onGroupReFresh(group2);
                        }
                        break;
                    case 0x30:// 用户列表
//                        switch (bodyBytes[0]) {
//                            case 0x00:
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        LogUtil.error("TcpCmd", "592\tcmdExplore()\n" + body);
                        FriendReFreshBean friendReFreshBean = mGson.fromJson(body, FriendReFreshBean.class);

                        LogUtil.error("TcpCmd", "595\tcmdExplore()\n" + "总包数" + friendReFreshBean.getCount() + "第" + friendReFreshBean.getIndex() + "包 内容" + friendReFreshBean.getList().size());
                        mUserInfoBeanDao.insertOrReplaceInTx(friendReFreshBean.getList());

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onReFriendsFresh(friendReFreshBean.getList());
                        }
                    }
                    //                                break;
//                        }
                    break;
                    case 0x3A:// 群组列表
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        GroupReFreshBean groupReFreshBean = mGson.fromJson(body, GroupReFreshBean.class);

                        UserInfoBean currentInfo = LTApi.getInstance().getCurrentInfo();
                        LogUtil.error("TcpCmd", "609\tcmdExplore()\n" + "总包数" + groupReFreshBean.getCount() + "第" + groupReFreshBean.getIndex() + "包\n内容" + mGson.toJson(groupReFreshBean.getList()));
                        for (int i = 0; i < groupReFreshBean.getList().size(); i++) {
                            groupReFreshBean.getList().get(i).setUserId(currentInfo.getUserId());
                        }
                        mGroupInfoBeanDao.insertOrReplaceInTx(groupReFreshBean.getList());

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onReGroupsFresh(groupReFreshBean.getList());
                        }
                    }
                    break;
                    case 0x3B:// 用户上线
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        UserInfoBean users = mGson.fromJson(body, UserInfoBean.class);
                        LogUtil.error("TcpCmd", "757\tcmdExplore()\n" + body);

                        UserInfoBean unique1 = mUserInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(users.getUserId())).build().unique();
                        unique1.setIsOnLine("1");
                        mUserInfoBeanDao.insertOrReplace(unique1);

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onFriendsReFresh(unique1);
                        }

                    }
                    break;
                    case 0x3C:// 用户离线
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);

                        UserInfoBean users = mGson.fromJson(body, UserInfoBean.class);

                        UserInfoBean unique1 = mUserInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(users.getUserId())).build().unique();
                        unique1.setIsOnLine("0");
                        mUserInfoBeanDao.insertOrReplace(unique1);

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onFriendsReFresh(unique1);
                        }
                    }
                    break;
                    case 0x3D:// 企业添加新用户
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        UserInfoBean users = mGson.fromJson(body, UserInfoBean.class);
                        mUserInfoBeanDao.insertOrReplace(users);

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onFriendsReFresh(users);
                        }

                    }
                    break;
                    case 0x3E:// 企业删除用户
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        UserInfoBean users = mGson.fromJson(body, UserInfoBean.class);
                        mUserInfoBeanDao.delete(users);

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onFriendsDel(users);
                        }

                    }
                    break;
                    case 0x3F:// 好友信息被更新
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        UserInfoBean users = mGson.fromJson(body, UserInfoBean.class);

//                        UserInfoBean unique1 = mUserInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(users.getUserId())).build().unique();
//                        unique1.setIsOnLine("1");
                        mUserInfoBeanDao.insertOrReplace(users);

                        LogUtil.error("TcpCmd", "680\tcmdExplore()\n" + body);
                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onFriendsReFresh(users);
                        }
                    }
                    break;
                    case 0x40://  现有群组被移除
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        GroupInfoBean users = mGson.fromJson(body, GroupInfoBean.class);
                        mGroupInfoBeanDao.delete(users);

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onGroupDel(users);
                        }
                    }
                    break;
                    case 0x41://  现有群组信息更新
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        GroupInfoBean users = mGson.fromJson(body, GroupInfoBean.class);
                        mGroupInfoBeanDao.insertOrReplace(users);

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onGroupReFresh(users);
                        }
                    }
                    break;
                    case 0x42://  加入了新的群组
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        GroupInfoBean users = mGson.fromJson(body, GroupInfoBean.class);

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onGroupReFresh(users);
                        }

                    }
                    break;
                    case 0x43:// 被移出现有群组
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);
                        GroupInfoBean users = mGson.fromJson(body, GroupInfoBean.class);

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onGroupDel(users);
                        }
                    }
                    break;
                    case 0x47:// 企业成员在线状态变化
                    {
                        byte[] jsonBytes = new byte[bodyBytes.length];
                        System.arraycopy(bodyBytes, 0, jsonBytes, 0, bodyBytes.length);
                        body = ByteIntUtils.utfToString(jsonBytes);

                        List<UserInfoBean> users = mGson.fromJson(body, new TypeToken<List<UserInfoBean>>() {
                        }.getType());
                        for (int i = 0; i < users.size(); i++) {
                            UserInfoBean unique1 = mUserInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(users.get(i).getUserId())).build().unique();
                            unique1.setIsOnLine(users.get(i).getIsOnLine());
                            mUserInfoBeanDao.insertOrReplace(unique1);
                        }

                        if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                            LTConfigure.getInstance().getLtApi().onReFreshListener.onReFriendsFresh(users);
                        }

                    }
                    break;
                }
                break;
            case 0x02://服务端>>终端(其他)
                if (LTConfigure.getInstance().mOnErrorListener != null)
                    switch (pagBytes[8]) {
                        case 0x00://收到服务端心跳包回复
                            LTConfigure.getInstance().mOnErrorListener.onError();
                            break;
                        case 0x01://指令不正确
                            LTConfigure.getInstance().mOnErrorListener.onOrderError();
                            break;
                        case 0x02://未登陆
                            LTConfigure.getInstance().mOnErrorListener.onNotLogin();
                            break;
                        case 0x03://CRC校验错误
                            LTConfigure.getInstance().mOnErrorListener.onCRCError();
                            break;
                        case 0x04://被遥弊
                            break;
                        case 0x05://未知错误
                            LTConfigure.getInstance().mOnErrorListener.onError();
                            break;
                    }
                break;
        }

    }

    private void setSound() {
        AudioManager service = (AudioManager) LTConfigure.mContext.getSystemService(Context.AUDIO_SERVICE);
        service.setSpeakerphoneOn(true);
        service.setMode(AudioManager.STREAM_SYSTEM);
    }


    public static boolean isGroupBeat   = false;//群聊心跳包
    public static boolean isConnectBeat = false;//联网心跳包


    Thread startBeat = new Thread(new Runnable() {
        @Override
        public void run() {

        }
    });


    Runnable BeatRunnable = new Runnable() {
        @Override
        public synchronized void run() {
            if (addr == null) {
                try {
                    addr = InetAddress.getByName(TcpConfig.HOST);
                } catch (UnknownHostException e) {
                }
            }
            while (isConnectBeat || isGroupBeat) {
                if (isGroupBeat) {
                    byte[]         enmy   = CmdUtils.getInstance().sendGroupBeat();
                    DatagramPacket packet = new DatagramPacket(enmy, 0, enmy.length, addr, port);
                    try {
                        TcpSocket.getInstance().getClient().send(packet);
                        LogUtil.error("TcpCmd", "364\tonTick()\n" + "群聊心跳包addr:" + addr + "\tport:" + port);
                    } catch (IOException e) {
                        LogUtil.error("563\trun()\n", e);
                    }
                }
                if (isConnectBeat) {
                    byte[] heart = CmdUtils.getInstance().sendHeratPackage();
                    TcpSocket.getInstance().addData(heart);//定时发送心跳包
                }

                SystemClock.sleep(10 * 1000);
            }
        }
    };

    //强制加入群聊
    private void skipToGroup(byte[] bodyBytes) {
        LogUtil.error("TcpCmd", "837\tskipToGroup()\n" + "强制加入群聊");
        String        json     = ByteIntUtils.utfToString(bodyBytes);
        Gson          gson     = new Gson();
        GroupComeBean chatBean = gson.fromJson(json, GroupComeBean.class);
        if (chatBean == null)
            return;
        port = chatBean.getPort();
//        sendBroadcast(new Intent(BroadcastUtils.SKIP_TO_GROUP).putExtra("groupid", chatBean.getGroupId()));
    }

}
