package com.netphone.netsdk;

import com.netphone.gen.GroupChatMsgBeanDao;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.listener.OnGetGroupMemberListener;
import com.netphone.netsdk.listener.OnGroupComeInListener;
import com.netphone.netsdk.listener.OnGroupStateListener;
import com.netphone.netsdk.listener.OnLoginListener;
import com.netphone.netsdk.socket.TcpSocket;
import com.netphone.netsdk.utils.CmdUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XYSM on 2018/4/13.
 */

public class LTApi {
    private static LTApi mApi;
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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


    public void sendGroupMessage(String id, String name, String content) {

        byte[] words = CmdUtils.getInstance().sendGroupCommonBeanApi(id, content, (byte) 0x00, (byte) 0x1C);
        TcpSocket.getInstance().addData(words);
    }


    public ArrayList<GroupChatMsgBean> getGroupChatMessage(String groupId) {
        GroupChatMsgBeanDao groupChatMsgBeanDao = LTConfigure.getInstance().getDaoSession().getGroupChatMsgBeanDao();
        List<GroupChatMsgBean> list = groupChatMsgBeanDao.queryBuilder().where(GroupChatMsgBeanDao.Properties.FromGroupId.eq(groupId)).list();
        ArrayList<GroupChatMsgBean> groupChatMsgBeans = new ArrayList<>();
        groupChatMsgBeans.addAll(list);
        return groupChatMsgBeans;
    }
}
