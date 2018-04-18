package com.netphone.netsdk;

import com.netphone.netsdk.listener.OnGetGroupMemberListener;
import com.netphone.netsdk.listener.OnGroupComeInListener;
import com.netphone.netsdk.listener.OnGroupStateListener;
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

    public OnGroupComeInListener groupComeInListener;
    public OnGetGroupMemberListener getGroupMemberListener;
    public OnGroupStateListener groupStateListener;
    public String groupId;

    public void joinGroup(String groupID, OnGroupComeInListener groupComeInListener, OnGetGroupMemberListener getGroupMemberListener, OnGroupStateListener groupStateListener) {
        this.groupComeInListener = groupComeInListener;
        this.getGroupMemberListener = getGroupMemberListener;
        this.groupStateListener = groupStateListener;
        this.groupId = groupID;
        byte[] joinGroup = CmdUtils.getInstance().commonApi((byte) 0x00, (byte) 0x04, groupID);
        TcpSocket.getInstance().addData(joinGroup);
    }
}
