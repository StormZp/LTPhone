package com.netphone.config;

/**
 * Created by Storm on 2018/2/23.
 */

public class EventConfig {
    public static final int RECEIVER_WORD_GROUP = 1;//收到群聊文字消息
    public static final int COME_IN_SUCCESS     = 2;//加入群聊成功
    public static final int COME_IN_FAIL        = 3;//加入群聊失败
    public static final int GET_MEMBER_FAIL     = 4;//获取群成员失败
    public static final int GET_MEMBER_SUCCESS  = 5;//获取群成员成功
    public static final int MENBER_EXIT         = 6;//群成员退出
    public static final int MENBER_JOIN         = 7;//群成员加入
    public static final int MENBER_HAVE_MAC     = 8;//群成员拥有麦
    public static final int MEMBER_RELAXE_DMAC  = 9;//群成员释放麦
    public static final int SYSTEM_RELAXED_MAC  = 10;//系统释放麦
    public static final int GRAB_WHEAT_SUCCESS  = 11;//抢麦成功
    public static final int GRAB_WHEAT_FAIL     = 12;//抢麦失败
    public static final int RELAXED_MAC_SUCCESS = 13;//释放麦成功
    public static final int RELAXED_MAC_FAIL    = 14;//释放麦失败
    public static final int SQUEEZE_OFF_LINE    = 15;//被抢登录
    public static final int FRIEND_SEND_MSG     = 16;//好友发送消息
    public static final int REFRESH_FRIEND      = 17;//刷新好友列表
    public static final int BROADCAST_OVER      = 18;//广播结束
}
