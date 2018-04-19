package com.netphone.netsdk.listener;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnLocationListener {
    void onError();

    void onSendSuccess();

    void onSendFail();

    void onHelpSuccess();

    void onHelpFail();

}
