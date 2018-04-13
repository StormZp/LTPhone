package com.netphone.netsdk;

import com.netphone.netsdk.listener.OnLoginListener;
import com.netphone.netsdk.socket.TcpSocket;
import com.netphone.netsdk.utils.CmdUtils;

/**
 * Created by XYSM on 2018/4/13.
 */

public class LTApi {
    private static LTApi mApi;

    public static LTApi newInstance() {
        if (mApi == null) {
            mApi = new LTApi();
        }
        return mApi;
    }

    public OnLoginListener mOnLoginListener;

    public void login(String username, String password, OnLoginListener loginListener) {
        mOnLoginListener = loginListener;
        byte[] login = CmdUtils.getInstance().sendLogin(username, password);
        TcpSocket.getInstance().addData(login);
    }
}
