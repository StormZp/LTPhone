package com.netphone.netsdk.listener;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:23<p>
 * Message {好友来电回调}
 */
public interface OnFriendCallListener {
    void onCallAccept();

    void onCallReject();

    void onCallStart();

    void onCallFail(int state, String message);

}
