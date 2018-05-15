package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.BroadcastBean;
import com.netphone.netsdk.bean.FriendChatMsgBean;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.bean.ImageBean;
import com.netphone.netsdk.bean.UserInfoBean;

import java.util.List;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnReFreshListener {

    void onReFriendsFresh(List<UserInfoBean> userListBean);//好友列表推送

    void onReGroupsFresh(List<GroupInfoBean> groupListBean);//群列表推送

    void onFriendsReFresh(UserInfoBean bean);//好友刷新

    void onFriendsDel(UserInfoBean bean);//删除好友

    void onGroupReFresh(GroupInfoBean bean);//群刷新

    void onGroupDel(GroupInfoBean bean);//群删除

    void onWordBroadcast(BroadcastBean msgBean);

    void onSqueezeLine();

    void onElectronWall();

    void onMultiMedia(ImageBean bean);

    void onFriendChatMsg(FriendChatMsgBean bean);

    void onBroadcastCome(int state);//0 接收 1 发送

    void onFriendVoice(UserInfoBean userBean);//好友通话请求


    void groupCome(GroupInfoBean comeBean);//进入群聊

    void dizzy();//摇晕

    void dizzyCancel();//取消摇晕

    void shake();//取消摇晕

    void shakeCancel();//取消摇晕


}
