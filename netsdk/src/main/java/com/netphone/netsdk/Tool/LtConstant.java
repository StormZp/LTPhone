package com.netphone.netsdk.Tool;

import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.bean.UserListBean;

/**
 * Created Storm 
 * Time    2018/5/21 10:20
 * Message {力同lib本地配置}
 */
public class LtConstant {

    public static final String RealName    = "RealName";
    public static final String UserId      = "UserId";
    public static final String IsDizzy     = " IsDizzy";
    public static final String IsOnLine    = "IsOnLine";
    public static final String HeadIcon    = " HeadIcon";
    public static final String Description = "Description";
    public static final String Gender      = " Gender";
    public static final String ExpiredDate = "ExpiredDate";
    public static final String Auto_Login  = "autoLogin";
    public static final String Online      = "Online";
    public static final String IsShake     = "IsShake";

    public static final String username = "username";
    public static final String password = "password";

    public static final String currentGroupId = "currentGroupId";

    public static final int VOICE_DATA_HEARD = 10;//音频数据头

    public static boolean isOnline;
    public static UserInfoBean  info;
    public static UserListBean  listBean;
    public static GroupInfoBean currentGroupInfo;
}
