package com.netphone.netsdk;

import android.content.Context;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.netphone.gen.GroupChatMsgBeanDao;
import com.netphone.gen.ImageBeanDao;
import com.netphone.gen.UserInfoBeanDao;
import com.netphone.netsdk.Tool.Constant;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.bean.ImageBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.listener.OnChangePasswordListener;
import com.netphone.netsdk.listener.OnChangeUserInfoListener;
import com.netphone.netsdk.listener.OnGetGroupMemberListener;
import com.netphone.netsdk.listener.OnGroupChatListener;
import com.netphone.netsdk.listener.OnGroupComeInListener;
import com.netphone.netsdk.listener.OnGroupStateListener;
import com.netphone.netsdk.listener.OnLocationListener;
import com.netphone.netsdk.listener.OnLoginListener;
import com.netphone.netsdk.listener.OnReFreshListener;
import com.netphone.netsdk.listener.OnUpFileListener;
import com.netphone.netsdk.service.LocationService;
import com.netphone.netsdk.socket.TcpSocket;
import com.netphone.netsdk.utils.CmdUtils;
import com.netphone.netsdk.utils.FileUtils;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.netsdk.utils.SharedPreferenceUtil;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//            SharedPreferenceUtil.Companion.put(Constant.Auto_Login,true);
        SharedPreferenceUtil.Companion.put(Constant.username, username);
        SharedPreferenceUtil.Companion.put(Constant.password, password);
//        }else {
//            SharedPreferenceUtil.Companion.put(Constant.Auto_Login,false);
//            SharedPreferenceUtil.Companion.put(Constant.username,"");
//            SharedPreferenceUtil.Companion.put(Constant.password,"");
//        }
        byte[] login = CmdUtils.getInstance().sendLogin(username, password);
        TcpSocket.getInstance().addData(login);
    }

    public OnGroupComeInListener    groupComeInListener;
    public OnGetGroupMemberListener getGroupMemberListener;
    public OnGroupStateListener     groupStateListener;
    public OnGroupChatListener      groupChatListener;
    public OnChangePasswordListener onChangePasswordListener;
    public OnLocationListener       onLocationListener;
    public OnUpFileListener         onUpFileListener;
    public OnReFreshListener        onReFreshListener;
    public OnChangeUserInfoListener onChangeUserInfoListener;
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


    private GroupChatMsgBeanDao groupChatMsgBeanDao;
    private UserInfoBeanDao     userInfoBeanDao;

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
        return userInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(SharedPreferenceUtil.Companion.read(Constant.UserId, ""))).unique();
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
    }

    /**
     * 在线
     */
    public void onLine(Context context) {
        LTConfigure.init(context);
        LTConfigure.getInstance().startLocationService();
        final String username = SharedPreferenceUtil.Companion.read(Constant.username, "");
        final String password = SharedPreferenceUtil.Companion.read(Constant.password, "");
        LogUtil.error("LTApi", "205\tonLine()\n" + "请求登录$username" + username + "\tpassword:" + password);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(500);
                login(username, password, null);
            }
        }).start();

    }

    /**
     * 获取当前群组信息
     *
     * @return
     */
    public GroupInfoBean getCurrentGroupInfo() {
        return Constant.currentGroupInfo;
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

    public void changeUserInfo(UserInfoBean userInfoBean, OnChangeUserInfoListener onChangeUserInfoListener) {
        this.onChangeUserInfoListener = onChangeUserInfoListener;
        Gson   gson  = new Gson();
        String s     = gson.toJson(userInfoBean);
        byte[] datas = CmdUtils.getInstance().commonApi((byte) 0x00, (byte) 0x17, s);
        TcpSocket.getInstance().addData(datas);
    }


    public List<ImageBean> getReceiverImages(String userId){
        ImageBeanDao imageBeanDao = LTConfigure.getInstance().getDaoSession().getImageBeanDao();
        return imageBeanDao.queryBuilder().where(ImageBeanDao.Properties.ReceiveId.eq(userId)).list();
    }
}
