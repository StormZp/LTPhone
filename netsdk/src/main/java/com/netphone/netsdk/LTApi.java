package com.netphone.netsdk;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.netphone.gen.CurrentGroupBeanDao;
import com.netphone.gen.FriendChatMsgBeanDao;
import com.netphone.gen.GroupChatMsgBeanDao;
import com.netphone.gen.GroupInfoBeanDao;
import com.netphone.gen.ImageBeanDao;
import com.netphone.gen.ReplyMsgBeanDao;
import com.netphone.gen.UserInfoBeanDao;
import com.netphone.netsdk.Tool.LtConstant;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.CurrentGroupBean;
import com.netphone.netsdk.bean.FriendChatMsgBean;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.bean.ImageBean;
import com.netphone.netsdk.bean.ReplyMsgBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.listener.OnBroadcastListener;
import com.netphone.netsdk.listener.OnChangePasswordListener;
import com.netphone.netsdk.listener.OnChangeUserInfoListener;
import com.netphone.netsdk.listener.OnFriendCallListener;
import com.netphone.netsdk.listener.OnGetGroupMemberListener;
import com.netphone.netsdk.listener.OnGroupChatListener;
import com.netphone.netsdk.listener.OnGroupComeInListener;
import com.netphone.netsdk.listener.OnGroupStateListener;
import com.netphone.netsdk.listener.OnLocationListener;
import com.netphone.netsdk.listener.OnLoginListener;
import com.netphone.netsdk.listener.OnManagerListener;
import com.netphone.netsdk.listener.OnReFreshListener;
import com.netphone.netsdk.listener.OnUpFileListener;
import com.netphone.netsdk.service.LocationService;
import com.netphone.netsdk.socket.TcpSocket;
import com.netphone.netsdk.socket.UdpSocket;
import com.netphone.netsdk.utils.CmdUtils;
import com.netphone.netsdk.utils.FileUtils;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.netsdk.utils.ReplyUtil;
import com.netphone.netsdk.utils.SharedPreferenceUtil;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created Storm <p>
 * Time    2018/5/21 10:39<p>
 * Message {力同 lib的api}
 */
public class LTApi {
    private static LTApi mApi;
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static LTApi getInstance() {
        if (mApi == null) {
            mApi = new LTApi();
        }
        return mApi;
    }


    /**
     * 登录
     *
     * @param username      账号
     * @param password      密码
     * @param loginListener 回调监听
     */
    public void login(String username, String password, OnLoginListener loginListener) {
        mOnLoginListener = loginListener;
//        if (isAuto){
//            SharedPreferenceUtil.Companion.put(LtConstant.Auto_Login,true);
        SharedPreferenceUtil.Companion.put(LtConstant.username, username);
        SharedPreferenceUtil.Companion.put(LtConstant.password, password);
//        }else {
//            SharedPreferenceUtil.Companion.put(LtConstant.Auto_Login,false);
//            SharedPreferenceUtil.Companion.put(LtConstant.username,"");
//            SharedPreferenceUtil.Companion.put(LtConstant.password,"");
//        }
        byte[] login = CmdUtils.getInstance().sendLogin(username, password);
        TcpSocket.getInstance().addData(login);
    }

    public OnLoginListener          mOnLoginListener;
    public OnGroupComeInListener    groupComeInListener;
    public OnGetGroupMemberListener getGroupMemberListener;
    public OnGroupStateListener     groupStateListener;
    public OnGroupChatListener      groupChatListener;
    public OnChangePasswordListener onChangePasswordListener;
    public OnLocationListener       onLocationListener;
    public OnUpFileListener         onUpFileListener;
    public OnReFreshListener        onReFreshListener;
    public OnBroadcastListener      onBroadcastListener;
    public OnChangeUserInfoListener onChangeUserInfoListener;
    public OnFriendCallListener     onFriendCallListener;
    public OnManagerListener        onManagerListener;
    public String                   groupId;

    /**
     * 加入群组
     *
     * @param groupID
     * @param groupComeInListener
     * @param getGroupMemberListener
     * @param groupStateListener
     * @param groupChatListener
     */
    public void joinGroup(String groupID, OnGroupComeInListener groupComeInListener, OnGetGroupMemberListener getGroupMemberListener, OnGroupStateListener groupStateListener, OnGroupChatListener groupChatListener) {
        this.groupComeInListener = groupComeInListener;
        this.getGroupMemberListener = getGroupMemberListener;
        this.groupStateListener = groupStateListener;
        this.groupChatListener = groupChatListener;
        this.groupId = groupID;
        byte[] joinGroup = CmdUtils.getInstance().commonApi((byte) 0x00, (byte) 0x04, groupID);
        TcpSocket.getInstance().addData(joinGroup);

        //发送成员获取
        byte[] temp = CmdUtils.getInstance().commonApi((byte) 0x00, (byte) 0x1B, groupID);
        TcpSocket.getInstance().addData(temp);

        CurrentGroupBean    bean                = new CurrentGroupBean(getCurrentInfo().getUserId(), groupID);
        CurrentGroupBeanDao currentGroupBeanDao = LTConfigure.getInstance().getDaoSession().getCurrentGroupBeanDao();
        currentGroupBeanDao.insertOrReplace(bean);
        LogUtil.error("LTApi", "127\tjoinGroup()\n" + new Gson().toJson(bean));
    }

    /**
     * 退出群组(已废弃)
     *
     * @param groupID
     */
    @Deprecated
    public void exitGroup(String groupID) {
        byte[] joinGroup = CmdUtils.getInstance().commonApi2((byte) 0x00, (byte) 0x0A);
        TcpSocket.getInstance().addData(joinGroup);
    }


    private GroupChatMsgBeanDao  groupChatMsgBeanDao;
    private UserInfoBeanDao      userInfoBeanDao;
    private FriendChatMsgBeanDao friendChatMsgBeanDao;

    /**
     * 发送群聊信息
     *
     * @param id
     * @param content
     */
    public void sendGroupMessage(String id, String content) {
        byte[] words = CmdUtils.getInstance().sendGroupCommonBeanApi(id, content, (byte) 0x00, (byte) 0x1C);
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 发送群聊语音
     */
    public void sendGroupVoice() {
        byte[] words = CmdUtils.getInstance().commonApi2((byte) 0x00, (byte) 0x0B);
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 给好友电话
     *
     * @param id
     */
    public void friendCall(String id, OnFriendCallListener onFriendCallListener) {
        this.onFriendCallListener = onFriendCallListener;
        if (!TextUtils.isEmpty(id)) {
            byte[] words = CmdUtils.getInstance().sendCallRequest(id);
            TcpSocket.getInstance().addData(words);
        }
    }

    /**
     * 拒绝好友电话
     */
    public void CallRefuse() {
        byte[] words = CmdUtils.getInstance().handleCall(0);
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 取消通话
     */
    public void turnOffCall() {
        byte[] words = CmdUtils.getInstance().turnOffCall();
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 接收好友电话
     */
    public void CallAccept() {
        byte[] words = CmdUtils.getInstance().handleCall(1);
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 停止发送群聊语音
     */
    public void stopGroupVoice() {
        byte[] words = CmdUtils.getInstance().commonApi2((byte) 0x00, (byte) 0x0C);
        TcpSocket.getInstance().addData(words);
    }


    /**
     * 停止发送广播
     */
    public void stopBroadcastSend() {
        byte[] words = CmdUtils.getInstance().sendCancelForce();
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 发送单聊信息
     *
     * @param id
     * @param content
     */
    public void sendFriendMessage(String id, String content) {
        FriendChatMsgBean bean = new FriendChatMsgBean();
        bean.setReceiveId(id);
        bean.setMsg(content);
        bean.setSendId(LtConstant.info.getUserId());
        bean.setSendId(LtConstant.info.getUserId());
        bean.setUserId(LtConstant.info.getUserId());
        bean.setDateTime(System.currentTimeMillis());

        if (friendChatMsgBeanDao == null)
            friendChatMsgBeanDao = LTConfigure.getInstance().getDaoSession().getFriendChatMsgBeanDao();
        friendChatMsgBeanDao.insertOrReplace(bean);

        UserInfoBean userInfo = getUserInfo(id);
        ReplyUtil.insertMsg(id, userInfo.getRealName(), bean.getUserId(), bean.getMsg(), true);

        if (LTApi.getInstance().onReFreshListener != null) {
            LTApi.getInstance().onReFreshListener.onFriendChatMsg(bean);
        }
        byte[] words = CmdUtils.getInstance().sendFriendCommonBeanApi(id, content, (byte) 0x00, (byte) 0x08);
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 好友聊天
     *
     * @param id
     */
    public void joinFriendChat(String id) {
        if (LtConstant.info != null)
            ReplyUtil.read(id, LtConstant.info.getUserId());

    }

    /**
     * 获取会话列表
     *
     * @return
     */
    public ArrayList<ReplyMsgBean> getSessionList() {
        ArrayList<ReplyMsgBean> replyMsgBeans = new ArrayList<>();
        if (LtConstant.info != null)
            replyMsgBeans.addAll(ReplyUtil.getList(LtConstant.info.getUserId()));

        for (int i = 0; i < replyMsgBeans.size(); i++) {
            replyMsgBeans.get(i).setReceiver(getUserInfo(replyMsgBeans.get(i).getReceiveID()));
        }
        LogUtil.error("LTApi", "274\tgetSessionList()\n" + new Gson().toJson(replyMsgBeans));
        return replyMsgBeans;
    }

    /**
     * 获取好友列表
     *
     * @return
     */
    public ArrayList<UserInfoBean> getFriendsList() {
        ArrayList<UserInfoBean> userInfoBeans = new ArrayList<>();
        userInfoBeans.addAll(userInfoBeanDao.loadAll());
        return userInfoBeans;
    }

    /**
     * 获取好友聊天记录
     *
     * @param receiveId 好友id
     * @return
     */
    public ArrayList<FriendChatMsgBean> getFriendChatMessage(String receiveId) {
        if (friendChatMsgBeanDao == null)
            friendChatMsgBeanDao = LTConfigure.getInstance().getDaoSession().getFriendChatMsgBeanDao();
        List<FriendChatMsgBean>      list              = friendChatMsgBeanDao.queryBuilder().where(FriendChatMsgBeanDao.Properties.ReceiveId.eq(receiveId)).list();
        ArrayList<FriendChatMsgBean> groupChatMsgBeans = new ArrayList<>();
        groupChatMsgBeans.addAll(list);
        return groupChatMsgBeans;
    }

    /**
     * 获取群聊聊天记录
     *
     * @param groupId
     * @return
     */
    public ArrayList<GroupChatMsgBean> getGroupChatMessage(String groupId) {
        if (groupChatMsgBeanDao == null)
            groupChatMsgBeanDao = LTConfigure.getInstance().getDaoSession().getGroupChatMsgBeanDao();
        List<GroupChatMsgBean>      list              = groupChatMsgBeanDao.queryBuilder().where(GroupChatMsgBeanDao.Properties.FromGroupId.eq(groupId)).list();
        ArrayList<GroupChatMsgBean> groupChatMsgBeans = new ArrayList<>();
        groupChatMsgBeans.addAll(list);
        return groupChatMsgBeans;
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    public UserInfoBean getCurrentInfo() {
        if (userInfoBeanDao == null)
            userInfoBeanDao = LTConfigure.getInstance().getDaoSession().getUserInfoBeanDao();
        return userInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(SharedPreferenceUtil.Companion.read(LtConstant.UserId, ""))).unique();
    }

    /**
     * 获取某个用户的信息
     *
     * @param userId
     * @return
     */
    public UserInfoBean getUserInfo(String userId) {
        if (!TextUtils.isEmpty(userId))
            try {
                UserInfoBean unique = userInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(userId)).build().unique();
                if (unique != null) {
                    return unique;
                } else {
                    return new UserInfoBean();
                }
            } catch (Exception e) {
                LogUtil.error("323\tgetUserInfo()\n" + userId, e);
            }
        return null;
    }


    /**
     * 发送用户经纬度
     *
     * @param onLocationListener
     */
    public void sendLocation(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
        if (LocationService.longitude == 0.0 && LocationService.latitude == 0.0) {
            this.onLocationListener.onSendFail();
            this.onLocationListener = null;
        }
        byte[] words = CmdUtils.getInstance().uploadGPS(String.valueOf(LocationService.longitude), String.valueOf(LocationService.latitude));
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 一键呼救功能
     *
     * @param onLocationListener 呼救回调
     */
    public void help(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
        if (LocationService.longitude == 0.0 && LocationService.latitude == 0.0) {
            this.onLocationListener.onHelpFail();
            this.onLocationListener = null;
        }
        byte[] words = CmdUtils.getInstance().uploadhelp(String.valueOf(LocationService.longitude), String.valueOf(LocationService.latitude));
        TcpSocket.getInstance().addData(words);
    }

    /**
     * 离线
     */
    public void offLine() {
        LTConfigure.getInstance().onDestory();
        LtConstant.isOnline = false;
    }

    /**
     * 在线
     */
    public void onLine(Context context) {
        LTConfigure.init(context);
        LTConfigure.getInstance().startLocationService();
        final String username = SharedPreferenceUtil.Companion.read(LtConstant.username, "");
        final String password = SharedPreferenceUtil.Companion.read(LtConstant.password, "");
        LogUtil.error("LTApi", "205\tonLine()\n" + "请求登录$username" + username + "\tpassword:" + password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                login(username, password, null);
            }
        }).start();
        LtConstant.isOnline = true;
    }

    /**
     * 获取当前群组信息
     *
     * @return
     */
    public GroupInfoBean getCurrentGroupInfo() {
        if (getCurrentInfo() != null) {
            CurrentGroupBeanDao currentGroupBeanDao = LTConfigure.getInstance().getDaoSession().getCurrentGroupBeanDao();
            CurrentGroupBean    unique              = currentGroupBeanDao.queryBuilder().where(CurrentGroupBeanDao.Properties.UserId.eq(getCurrentInfo().getUserId())).build().unique();
            if (unique == null)
                return null;
            return unique.getGroupBean();
        }
        return null;
    }

    /**
     * 上传图片
     *
     * @param filePath         本地文件路径
     * @param onUpFileListener 回调监听
     */
    public void upImage(String filePath, OnUpFileListener onUpFileListener) {
        this.onUpFileListener = onUpFileListener;
        Tiny.getInstance().source(filePath).asFile().withOptions(new Tiny.FileCompressOptions()).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                if (isSuccess) {

                    String AttachmentName      = outfile.substring(outfile.lastIndexOf("/") + 1, outfile.lastIndexOf("."));
                    String AttachmentExtention = outfile.substring(outfile.lastIndexOf("."));
                    uploadFile(outfile, 1, AttachmentName, AttachmentExtention);
                }
            }
        });
    }


    /**
     * 上传文件
     *
     * @param outfile
     * @param onUpFileListener
     */
    public void upFile(String outfile, OnUpFileListener onUpFileListener) {
        this.onUpFileListener = onUpFileListener;
        String AttachmentName      = outfile.substring(outfile.lastIndexOf("/") + 1, outfile.lastIndexOf("."));
        String AttachmentExtention = outfile.substring(outfile.lastIndexOf("."));
        uploadFile(outfile, 99, AttachmentName, AttachmentExtention);
    }

    /**
     * 上传
     *
     * @param filePath
     * @param type
     * @param AttachmentName
     * @param AttachmentExtention
     */
    private void uploadFile(String filePath, int type, String AttachmentName, String AttachmentExtention) {
        File   file      = new File(filePath);
        byte[] fileArray = FileUtils.getBytesFromFile(file);
        if (fileArray == null) {
            return;
        }
        int                 count     = (fileArray.length / (1024 * 63)) + 1;
        int                 lastSize  = fileArray.length - (count - 1) * 1024 * 63;
        byte[]              itemBytes = new byte[1024 * 63];
        String              UUID      = java.util.UUID.randomUUID().toString();//唯一标识
        Map<String, Object> map       = new HashMap<String, Object>();
        for (int i = 0; i < count; i++) {
            if (i < count - 1)
                System.arraycopy(fileArray, i * 1024 * 63, itemBytes, 0, 1024 * 63);
            else {
                if (lastSize > 0) {
                    itemBytes = new byte[lastSize];
                    System.arraycopy(fileArray, i * 1024 * 63, itemBytes, 0, lastSize);
                } else {
                    //不够63K种情况
                    itemBytes = new byte[fileArray.length];
                    System.arraycopy(fileArray, i * 1024 * 63, itemBytes, 0, fileArray.length);
                }
            }
            if (i == 0) {
                map.put("Index", i + 1);
                map.put("GUID", UUID);
                map.put("AttachmentType", type);
                map.put("AttachmentName", AttachmentName);
                map.put("AttachmentExtention", AttachmentExtention);
                map.put("Count", count);
            } else {
                map.put("Index", i + 1);
                map.put("GUID", UUID);
                map.put("Count", count);
            }
            byte[] datas = CmdUtils.getInstance().uploadFile(itemBytes, (byte) 0x00, (byte) 0x0f, map);
            TcpSocket.getInstance().addData(datas);
        }
    }

    /**
     * 设置刷新好友列表监听
     *
     * @param onReFreshListener
     */
    public void setOnReFreshListener(OnReFreshListener onReFreshListener) {
        this.onReFreshListener = onReFreshListener;
    }

    /**
     * 设置广播监听
     *
     * @param onBroadcastListener
     */
    public void setOnBroadcastListener(OnBroadcastListener onBroadcastListener) {
        this.onBroadcastListener = onBroadcastListener;
    }

    /**
     * 设置管理者监听
     *
     * @param onManagerListener
     */
    public void setOnManagerListener(OnManagerListener onManagerListener) {
        this.onManagerListener = onManagerListener;
    }

    /**
     * 修改密码
     *
     * @param oldPassword              旧密码
     * @param newPassword              新密码
     * @param onChangePasswordListener
     */
    public void changePassword(String oldPassword, String newPassword, OnChangePasswordListener onChangePasswordListener) {
        this.onChangePasswordListener = onChangePasswordListener;
        byte[] datas = CmdUtils.getInstance().editPW(oldPassword, newPassword);
        TcpSocket.getInstance().addData(datas);
    }

    /**
     * 修改用户信息
     *
     * @param userInfoBean             用户信息
     * @param onChangeUserInfoListener 监听用户信息修改
     */
    public void changeUserInfo(UserInfoBean userInfoBean, OnChangeUserInfoListener onChangeUserInfoListener) {
        this.onChangeUserInfoListener = onChangeUserInfoListener;
        Gson   gson  = new Gson();
        String s     = gson.toJson(userInfoBean);
        byte[] datas = CmdUtils.getInstance().commonApi((byte) 0x00, (byte) 0x17, s);
        TcpSocket.getInstance().addData(datas);
    }


    /**
     * 获取接收图片
     *
     * @param userId
     * @return
     */
    public List<ImageBean> getReceiverImages(String userId) {
        ImageBeanDao imageBeanDao = LTConfigure.getInstance().getDaoSession().getImageBeanDao();
        return imageBeanDao.queryBuilder().where(ImageBeanDao.Properties.ReceiveId.eq(userId)).list();
    }

    /**
     * 退出登录app
     */
    public void exitLogin() {
    }

    /**
     * 根据关键字获取会话列表
     *
     * @param key
     * @return
     */
    public ArrayList<ReplyMsgBean> SearchSession(String key) {
        ArrayList<ReplyMsgBean> replyMsgBeans   = new ArrayList<>();
        ReplyMsgBeanDao         replyMsgBeanDao = LTConfigure.getInstance().getDaoSession().getReplyMsgBeanDao();
        replyMsgBeans.addAll(replyMsgBeanDao.queryBuilder().where(ReplyMsgBeanDao.Properties.UserId.eq(LtConstant.info.getUserId()), ReplyMsgBeanDao.Properties.ReceiveName.like("%" + key + "%")).orderDesc(ReplyMsgBeanDao.Properties.LastTime).list());
        return replyMsgBeans;
    }

    /**
     * 根据关键字查询好友
     *
     * @param key
     * @return
     */
    public ArrayList<UserInfoBean> SearchFriend(String key) {
        ArrayList<UserInfoBean> replyMsgBeans = new ArrayList<>();
        replyMsgBeans.addAll(userInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.RealName.like("%" + key + "%")).list());
        Collections.sort(replyMsgBeans); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        return replyMsgBeans;
    }

    /**
     * 根据关键字查询群聊
     *
     * @param key
     * @return
     */
    public ArrayList<GroupInfoBean> searchGroup(String key) {
        ArrayList<GroupInfoBean> replyMsgBeans = new ArrayList<>();
        replyMsgBeans.addAll(LTConfigure.getInstance().getDaoSession().getGroupInfoBeanDao().queryBuilder().where(GroupInfoBeanDao.Properties.GroupName.like("%" + key + "%")).list());
        return replyMsgBeans;
    }

/**
     * 获取所有群聊
     *
     * @return
     */
    public ArrayList<GroupInfoBean> getAllGroup() {
        ArrayList<GroupInfoBean> replyMsgBeans = new ArrayList<>();
        replyMsgBeans.addAll(LTConfigure.getInstance().getDaoSession().getGroupInfoBeanDao().loadAll());
        return replyMsgBeans;
    }


    /**
     * 停止播放
     */
    public void voicePlayStop() {
        UdpSocket.Companion.getInstance().stopPlay();
        UdpSocket.Companion.getInstance().closeUdp();
    }

    /**
     * 获取当前账号是否在线
     *
     * @return
     */
    public boolean getIsOnline() {
        return LtConstant.isOnline;
    }

    /**
     * 设置是否在线
     *
     * @param b
     */
    public void setOnline(boolean b) {
        LtConstant.isOnline = b;
//        SharedPreferenceUtil.Companion.read(LtConstant.Auto_Login, b);
    }

    /**
     * 设置是否自动登录
     *
     * @param auto
     * @return
     */
    public void setAuto(boolean auto) {
        SharedPreferenceUtil.Companion.insert(LtConstant.Auto_Login, auto);
    }

    /**
     * 获取是否自动登录
     *
     * @param auto
     * @return
     */
    public boolean getAuto(boolean auto) {
        return SharedPreferenceUtil.Companion.read(LtConstant.Auto_Login, auto);
    }


    /**
     * 设置当前账号的名字
     *
     * @return
     */
    @Deprecated
    public void setUserName(String name) {
        SharedPreferenceUtil.Companion.insert(LtConstant.username, name);
    }

    /**
     * 获取当前账号的名字
     *
     * @return
     */
    public String getUserName() {
        return SharedPreferenceUtil.Companion.read(LtConstant.username, "");
    }


    /**
     * 获取当前账号的密码
     *
     * @return
     */
    public String getPassword() {
        return SharedPreferenceUtil.Companion.read(LtConstant.password, "");
    }


    /**
     * 返回url连接
     * @return
     */
    public String getUrl() {
        return TcpConfig.URL;
    }

}
