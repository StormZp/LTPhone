package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.GroupChatMsgBean;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:23<p>
 * Message {群聊接收回调}
 */
public interface OnGroupChatListener {
    void onReceiverListener(GroupChatMsgBean bean);
}
