package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.GroupChatMsgBean;

/**
 * Created by XYSM on 2018/4/19.
 */

public interface OnGroupChatListener {
    void onReceiverListener(GroupChatMsgBean bean);
}
