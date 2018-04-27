package com.netphone.netsdk.listener;

/**
 * Created by XYSM on 2018/4/26.
 */

public interface OnFriendCallListener {
    void onCallAccept();

    void onCallReject();

    void onCallStart();

    void onCallFail(int state, String message);

}
