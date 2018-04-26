package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.BroadcastBean;
import com.netphone.netsdk.bean.FriendChatMsgBean;
import com.netphone.netsdk.bean.ImageBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.bean.UserListBean;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnReFreshListener {
    void onReFresh(UserListBean userListBean);
    void onWordBroadcast(BroadcastBean msgBean);
    void onSqueezeLine();
    void onElectronWall();
    void onMultiMedia(ImageBean bean);
    void onFriendChatMsg(FriendChatMsgBean bean);
    void onBroadcastCome(int state);//0 接收 1 发送
    void onFriendVoice(UserInfoBean userBean);//好友通话请求
}
