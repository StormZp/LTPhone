package com.netphone.netsdk.Tool;

import android.os.SystemClock;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netphone.gen.GroupChatMsgBeanDao;
import com.netphone.gen.GroupInfoBeanDao;
import com.netphone.gen.UserInfoBeanDao;
import com.netphone.netsdk.LTApi;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.R;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.bean.UserListBean;
import com.netphone.netsdk.socket.TcpSocket;
import com.netphone.netsdk.utils.ByteIntUtils;
import com.netphone.netsdk.utils.ByteUtil;
import com.netphone.netsdk.utils.CmdUtils;
import com.netphone.netsdk.utils.DataTypeChangeHelper;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.netsdk.utils.SharedPreferenceUtil;

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
    private int              port;
    private UserInfoBeanDao  mUserInfoBeanDao;
    private GroupInfoBeanDao mGroupInfoBeanDao;


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
        LogUtil.error("TcpCmd", "64\tcmdExplore()\n" + String.format("pagBytes[7] == %x && pagBytes[8] == %x", pagBytes[7], pagBytes[8]));
        switch (pagBytes[7]) {
            case 0x00://终端>>服务端指令列表
                switch (pagBytes[8]) {

                    case 0x00://登录回复
                        if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null)
                            switch (bodyBytes[0]) {
                                case 0x00://登录成功
                                    byte[] jsonBytes = new byte[bodyBytes.length - 1];
                                    System.arraycopy(bodyBytes, 1, jsonBytes, 0, bodyBytes.length - 1);
                                    String body = ByteIntUtils.utfToString(jsonBytes);
                                    LogUtil.error("TcpCmd", "77\tcmdExplore()\n" + body);
                                    Gson gson = new Gson();
                                    UserInfoBean user = gson.fromJson(body, UserInfoBean.class);
                                    if (user == null) {
                                        LogUtil.error("user =null");
                                        return;
                                    } else {
                                        LogUtil.error("user= " + user.toString());
                                    }
                                    isConnectBeat = true;
                                    SharedPreferenceUtil.Companion.put(Constant.UserId, user.getUserId());
                                    startBeat.start();
                                    mUserInfoBeanDao.insertOrReplace(user);
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onSuccess(user);
                                    break;
                                case 0x01://登录失败
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onFail(1, LTConfigure.getInstance().getContext().getResources().getString(R.string.account_or_password_mistake));
                                    break;
                                case 0x02://该终端被遥弊
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onFail(2, LTConfigure.getInstance().getContext().getResources().getString(R.string.terminal_malpractices));
                                    break;
                                case 0x03://账号过期
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onFail(3, LTConfigure.getInstance().getContext().getResources().getString(R.string.Account_expiration));
                                    break;
                                case 0x08://该账号已经登录
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
                        break;
                    case 0x03://退出通话(挂断)
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
                                    if (!TextUtils.isEmpty(LTApi.newInstance().groupId)) {
                                        SharedPreferenceUtil.Companion.put(Constant.currentGroupId, LTApi.newInstance().groupId);
                                        GroupInfoBean unique = mGroupInfoBeanDao.queryBuilder().where(GroupInfoBeanDao.Properties.GroupID.eq(LTApi.newInstance().groupId)).unique();
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
                    case 0x05://注册用户
                        break;
                    case 0x06://主动发起视频通话的回复
                        break;
                    case 0x07://挂断视频通话
                        break;
                    case 0x08://发送文字消息
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
                            if (bodyBytes[0] == 0x00) {
                                LTConfigure.getInstance().getLtApi().onUpFileListener.upSuccess();
                            } else {
                                LTConfigure.getInstance().getLtApi().onUpFileListener.upFail();
                            }
                            LTConfigure.getInstance().getLtApi().onUpFileListener = null;
                        }
                        break;
                    case 0x10://修改密码
                        if (LTApi.newInstance().onChangePasswordListener != null) {
                            if (bodyBytes[0] == 0x00) {
                                LTApi.newInstance().onChangePasswordListener.onSuccess();
                            } else if (bodyBytes[0] == 0x01) {
                                LTApi.newInstance().onChangePasswordListener.onFail(0x01, LTConfigure.getInstance().getContext().getResources().getString(R.string.old_password_fail));
                            } else if (bodyBytes[0] == 0x07) {
                                LTApi.newInstance().onChangePasswordListener.onFail(0x07, LTConfigure.getInstance().getContext().getResources().getString(R.string.not_login));
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
                        break;
                    case 0x1A://设置呼叫转移
                        break;
                    case 0x1B://获取群组成员信息
                        if (LTConfigure.getInstance().getLtApi().getGroupMemberListener != null) {
                            switch (bodyBytes[0]) {
                                case 0x00:
                                    byte[] bytes = Arrays.copyOfRange(bodyBytes, 1, bodyBytes.length);
                                    String json = ByteIntUtils.utfToString(bytes);
                                    LogUtil.error("TcpCmd", "226\tcmdExplore()\n" + json);
                                    List<UserInfoBean> bean = new Gson().fromJson(json, new TypeToken<List<UserInfoBean>>() {
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
                    case 0x00://推送用户列表信息,由于socket写在服务里原因，在主页没能正常停止服务，会导致服务一直开启，socket写入数据
                        if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null) {
                            String body = ByteIntUtils.utfToString(bodyBytes);
                            Gson   gson = new Gson();
//                            LogUtil.saveLog(LTConfigure.getInstance().getContext(), "127\tcmdExplore()\n" + body);
//                            LogUtil.error("TcpCmd", "278\tcmdExplore()\n" + body);
                            try {
                                LogUtil.error("TcpCmd", "255\tcmdExplore()\n" + body);
                                UserListBean       userListBean = gson.fromJson(body, UserListBean.class);
                                List<UserInfoBean> userInfo     = userListBean.getUserInfo();
                                UserInfoBean       currentInfo  = LTApi.newInstance().getCurrentInfo();
                                for (int i = 0; i < userInfo.size(); i++) {
                                    if (!currentInfo.getUserId().equals(userInfo.get(i).getUserId())) {
                                        mUserInfoBeanDao.insertOrReplace(userInfo.get(i));
                                    }
                                }

                                mGroupInfoBeanDao.insertOrReplaceInTx(userListBean.getGroupInfo());
                                if (LTConfigure.getInstance().getLtApi().mOnLoginListener != null) {
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener.onComplete(userListBean);
                                    LTConfigure.getInstance().getLtApi().mOnLoginListener = null;
                                }
                                if (LTConfigure.getInstance().getLtApi().onReFreshListener != null) {
                                    LTConfigure.getInstance().getLtApi().onReFreshListener.onReFresh(userListBean);
                                }
                            } catch (Exception e) {
                                LogUtil.error("TcpCmd", e);
                            }
                        }

                        break;
                    case 0x01://被叫语音通话
                        break;
                    case 0x02://开始语音通话
                        break;
                    case 0x03://对方挂断语音通话
                        break;
                    case 0x04://被叫视频通话
                        break;
                    case 0x05://
                        break;
                    case 0x06://开始视频通话
                        break;
                    case 0x07://对方挂断视频通话
                        break;
                    case 0x08://收到文字消息
                        break;
                    case 0x09://当有新的终端加入到当前的活动群聊中时
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            String       body = ByteIntUtils.utfToString(bodyBytes);
                            Gson         gson = new Gson();
                            UserInfoBean user = gson.fromJson(body, UserInfoBean.class);
                            LTConfigure.getInstance().getLtApi().groupStateListener.onMenberJoin(user);
                        }

                        break;
                    case 0x0A://当现有终端退出了当前的活动群聊中时
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            String       body = ByteIntUtils.utfToString(bodyBytes);
                            Gson         gson = new Gson();
                            UserInfoBean user = gson.fromJson(body, UserInfoBean.class);
                            LTConfigure.getInstance().getLtApi().groupStateListener.onMenberExit(user);
                        }
                        break;
                    case 0x0B://在群聊中时,麦权被抢占
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            String       body = ByteIntUtils.utfToString(bodyBytes);
                            Gson         gson = new Gson();
                            UserInfoBean user = gson.fromJson(body, UserInfoBean.class);
                            LTConfigure.getInstance().getLtApi().groupStateListener.onMenberhaveMac(user);
                        }
                        break;
                    case 0x0C://麦权被释放
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            String       body = ByteIntUtils.utfToString(bodyBytes);
                            Gson         gson = new Gson();
                            UserInfoBean user = gson.fromJson(body, UserInfoBean.class);
                            LTConfigure.getInstance().getLtApi().groupStateListener.onMemberRelaxedMac(user);
                        }
                        break;
                    case 0x0D://麦权被系统回收
                        if (LTConfigure.getInstance().getLtApi().groupStateListener != null) {
                            LTConfigure.getInstance().getLtApi().groupStateListener.onSystemReLaxedMac();
                        }
                        break;
                    case 0x0E://被遥晕
                        break;
                    case 0x0F://被取消遥晕
                        break;
                    case 0x10://被遥弊
                        break;
                    case 0x11://被取消遥弊
                        break;
                    case 0x12://当前通话时,通话对象被遥弊,或遥晕
                        break;
                    case 0x13://当用户超出电子围栏范围时
                        break;
                    case 0x14://收到被挤下线指令
                        LTConfigure.getInstance().onDestory();
                        if (LTApi.newInstance().onReFreshListener != null) {
                            LTApi.newInstance().onReFreshListener.onSqueezeLine();
                            LTApi.newInstance().onReFreshListener = null;
                        }


                        break;
                    case 0x15://强制加入群聊
                        break;
                    case 0x16://收到多媒体内容(图片,视频,文件等)
                        break;
                    case 0x17://收到多媒体内容(图片,视频,文件等)
                        break;
                    case 0x18://被发起主叫(从调度台)
                        break;
                    case 0x19://服务端发起监听(被监听)
                        break;
                    case 0x1A://服务端停止监听(被监听)
                        break;
                    case 0x1D://正在监听终端(主动),是否d同步播放
                        break;
                    case 0x1E://监听(主动)已被中断
                        break;
                    case 0x1F://从服务台发起广播(发起方)
                        break;
                    case 0x20://广播到达最大时长(发起方) 中断广播
                        break;
                    case 0x21://开始接收广播内容
                        break;
                    case 0x22://停止接收广播内容
                        break;
                    case 0x23://开始播放监听内容
                        break;
                    case 0x24://停止播放监听内容
                        break;
                    case 0x25://播放监听文件
                        break;
                    case 0x26://收到群聊消息
                        String body = ByteIntUtils.utfToString(bodyBytes);
                        LogUtil.error("TcpCmd", "356\tcmdExplore()\n" + body);
                        Gson gson = new Gson();
                        GroupChatMsgBean msg = gson.fromJson(body, GroupChatMsgBean.class);
                        msg.setDateTime(System.currentTimeMillis());
                        msg.setReceiveId(SharedPreferenceUtil.Companion.read(Constant.UserId, ""));

                        GroupChatMsgBeanDao groupInfoBeanDao = LTConfigure.getInstance().getDaoSession().getGroupChatMsgBeanDao();
                        groupInfoBeanDao.insertOrReplace(msg);

                        if (LTApi.newInstance().groupChatListener != null) {
                            LTApi.newInstance().groupChatListener.onReceiverListener(msg);
                        }
                        break;
                    case 0x27://收到群聊消息
                        String body2 = ByteIntUtils.utfToString(bodyBytes);
                        LogUtil.error("TcpCmd", "435\tcmdExplore()\n" + body2);
                        GroupChatMsgBean msg2 = new Gson().fromJson(body2, GroupChatMsgBean.class);
                        if (LTApi.newInstance().onReFreshListener != null) {
                            LTApi.newInstance().onReFreshListener.onWordBroadcast(msg2);
                        }
                        break;
                }
                break;
            case 0x02://服务端>>终端(其他)
                if (LTConfigure.getInstance().mOnErrorListener != null)
                    switch (pagBytes[8]) {
                        case 0x00://收到服务端心跳包回复
                            conncetBeatCount = 0;
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

    public static boolean isGroupBeat   = false;//群聊心跳包
    public static boolean isConnectBeat = false;//联网心跳包

    private int conncetBeatCount;//联网心跳包发送次数,超过3次不归0,即认定socket断了，需要重连。

    Thread startBeat = new Thread(new Runnable() {
        @Override
        public void run() {
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
//                        LogUtil.error("TcpCmd", "364\tonTick()\n" + "群聊心跳包");
                    } catch (IOException e) {
                    }
                }
                if (isConnectBeat) {
                    conncetBeatCount++;
                    byte[] heart = CmdUtils.getInstance().sendHeratPackage();
                    TcpSocket.getInstance().addData(heart);//定时发送心跳包
                }
                SystemClock.sleep(10 * 1000);
            }
        }
    });


}
