package com.netphone.netsdk.Tool;

/**
 * Created by Storm on 2018/2/23.
 */

public class EventConfig {
    public static final  int TCP_CONNECT_STATUS_SUCCESS = 0;
    public static final  int TCP_CONNECT_STATUS_FAIL = 1;
    public static final  int CMD_ORDER = 2;
    public static final  int SOCKET_LOGIN = 3;//登录
    public static final  int LOGIN_REPLY = 4;//登录回复
    public static final  int USER_PUSH = 5;//用户推送
    public static final  int HEART_PACK = 6;//心跳包
    public static final  int UPDATE_LOCAL_PORT = 7;//更新本地端口
    public static final  int RESET_TIME_ACTION = 8;//socket重连
    public static final  int CALL_VOICE_REPLY = 9;//主动发起语音通话的回复
    public static final  int CALL_VIDEO_REPLY = 10;//主动发起视频通话的回复
    public static final  int RECEIVER_WORD = 11;//收到文字消息
    public static final  int NET_WORK_CHANGE = 12;//收到网络变化广播
    public static final  int GROUP_INFO = 13;//群成员资料
    public static final  int LOCATION_ACTION = 14;//gps位置发送
    public static final  int UPLOAD_GPS = 15;//gps位置更新
    public static final  int HELP_ACTION = 16;//一键求救
    public static final  int EDIT_PW = 17;//修改密码
    public static final  int GET_PERSONAL_INFO_REPLY = 18;//获取个人基本信息回复
    public static final  int GET_PERSONAL_HEAD_REPLY = 19;//上传头像回复
    public static final  int LINE_OFF = 20;//离线
    public static final  int START_VOICE_CHAT = 21;//开始通话
    public static final  int STOP_VOICE_CHAT = 22;//停止通话
    public static final  int I_STOP_VOICE_CHAT = 35;//我方停止通话
    public static final  int GROUP_COMEIN_REPLY = 23;//收到加入群聊回复
    public static final  int NEW_DEVICE_JOIN_GROUP = 24;//收到新设备加入群聊
    public static final  int GET_GRAB_WHEAT_GROUP = 25;//抢麦回复
    public static final  int GET_REALASE_WHEAT_GROUP = 26;//释放麦回复
    public static final  int LEAVE_GROUP = 27;//离开群聊
    public static final  int DEVICE_LEAVE_GROUP = 28;//其他人离开群聊
    public static final  int GRAB_WHEAT_GROUP = 29;//麦权被抢占
    public static final  int REALASE_WHEAT_GROUP =30;//麦权被释放
    public static final  int SYSTEM_REALASE_WHEAT_GROUP = 31;//在群聊中拥有麦权时,麦权被系统中断
    public static final  int TO_REALASE_WHEAT_GROUP = 32;//手动释放麦
    public static final  int FORCE_COMEIN_GROUP = 33;//强制进入群聊
    public static final  int BEGIN_VOICE_CHAT = 34;//开始通话
    public static final  int CANCEL_VOICE = 35;//取消音频
}
