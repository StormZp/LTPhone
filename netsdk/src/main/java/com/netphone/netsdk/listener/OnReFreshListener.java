package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.ImageBean;
import com.netphone.netsdk.bean.UserListBean;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnReFreshListener {
    void onReFresh(UserListBean userListBean);
    void onWordBroadcast(GroupChatMsgBean chatMsgBean);
    void onSqueezeLine();
    void onElectronWall();
    void onMultiMedia(ImageBean bean);
}
