package com.netphone.netsdk.Tool;

import com.google.gson.Gson;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.R;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.bean.UserListBean;
import com.netphone.netsdk.utils.ByteIntUtils;
import com.netphone.netsdk.utils.DataTypeChangeHelper;
import com.netphone.netsdk.utils.LogUtil;

/**
 * Created by XYSM on 2018/4/13.
 */

public class TcpCmd {
    public void cmdData(byte[] pagBytes) {
        byte[] tempBytes = new byte[2];
        System.arraycopy(pagBytes, 9, tempBytes, 0, 2);
        int bodyLength = DataTypeChangeHelper.byte2int(tempBytes);
        byte[] bodyBytes = null;
        if (bodyLength > 0) {
            //对指令内容进行拷贝
            bodyBytes = new byte[bodyLength];
            System.arraycopy(pagBytes, 11, bodyBytes, 0, bodyLength);
        }
        cmdExplore(pagBytes, bodyBytes);
    }

    private void cmdExplore(byte[] pagBytes, byte[] bodyBytes) {
        LogUtil.error(String.format("pagBytes[7] == %x && pagBytes[8] == %x", pagBytes[7], pagBytes[8]));
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
                                    LogUtil.error("TAG\n" + body.length());
                                    Gson gson = new Gson();
                                    UserInfoBean user = gson.fromJson(body, UserInfoBean.class);
                                    if (user == null) {
                                        LogUtil.error("user =null");
                                        return;
                                    } else {
                                        LogUtil.error("user= " + user.toString());
                                    }
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
                        break;
                    case 0x02://主动发起语音通话的回复
                        break;
                    case 0x03://退出通话(挂断)
                        break;
                    case 0x04://收到加入群聊回复
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
                        break;
                    case 0x0C://主动释放麦回复
                        break;
                    case 0x0D://获取当前终端的通话记录
                        break;
                    case 0x0E://删除某条通话记录
                        break;
                    case 0x0F://上传多媒体内容(图片,视频,文件等)
                        break;
                    case 0x10://修改密码
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
                        break;
                    case 0x18://一键呼救回复
                        break;
                    case 0x19://停止发送广播
                        break;
                    case 0x1A://设置呼叫转移
                        break;
                    case 0x1B://获取群组成员信息
                        break;
                }
                break;
            case 0x01://服务端>>终端指令列表
                switch (pagBytes[8]) {
                    case 0x00://推送用户列表信息,由于socket写在服务里原因，在主页没能正常停止服务，会导致服务一直开启，socket写入数据
                        String body = ByteIntUtils.utfToString(bodyBytes);
                        Gson gson = new Gson();
//                        LogUtil.saveLog(LTConfigure.getInstance().getContext(), "127\tcmdExplore()\n" +body);
                        try {
                            LogUtil.error("TcpCmd", "130\tcmdExplore()\n" + body.length());
                            UserListBean userListBean = gson.fromJson(body, UserListBean.class);
                            LTConfigure.getInstance().getLtApi().mOnLoginListener.onComplete(userListBean);
                        } catch (Exception e) {

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
                        break;
                    case 0x0A://当现有终端退出了当前的活动群聊中时
                        break;
                    case 0x0B://在群聊中时,麦权被抢占
                        break;
                    case 0x0C://麦权被释放
                        break;
                    case 0x0D://麦权被系统回收
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

                }
                break;
            case 0x02://服务端>>终端(其他)
                switch (pagBytes[8]) {
                    case 0x00://收到服务端心跳包回复
                        break;
                    case 0x01://指令不正确
                        break;
                    case 0x02://未登陆
                        break;
                    case 0x03://CRC校验错误
                        break;
                        case 0x04://被遥弊
                        break;
                    case 0x05://未知错误
                        break;
                }
                break;
        }
    }
}
