package com.netphone.netsdk.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created Storm
 * Time    2018/4/21 16:12
 * Message {消息列表}
 */
@Entity
public class ReplyMsgBean {


    static final long serialVersionUID = 46L;
    @Id(autoincrement = true)
    private Long id;


    private String       userId;//当前用户id
    @NotNull
    private String       receiveID;//消息列表的用户id
    private String       receiveName;//消息列表的用户名
    private int          unread;//未读数
    private String       lastMsg;//最后一条
    private long         lastTime;//最后一条的时间
    @Transient
    private UserInfoBean user;
    @Transient
    private UserInfoBean receiver;
    @Generated(hash = 309302885)
    public ReplyMsgBean(Long id, String userId, @NotNull String receiveID,
            String receiveName, int unread, String lastMsg, long lastTime) {
        this.id = id;
        this.userId = userId;
        this.receiveID = receiveID;
        this.receiveName = receiveName;
        this.unread = unread;
        this.lastMsg = lastMsg;
        this.lastTime = lastTime;
    }
    @Generated(hash = 297682635)
    public ReplyMsgBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getReceiveID() {
        return this.receiveID;
    }
    public void setReceiveID(String receiveID) {
        this.receiveID = receiveID;
    }
    public String getReceiveName() {
        return this.receiveName;
    }
    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }
    public int getUnread() {
        return this.unread;
    }
    public void setUnread(int unread) {
        this.unread = unread;
    }
    public String getLastMsg() {
        return this.lastMsg;
    }
    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }
    public long getLastTime() {
        return this.lastTime;
    }
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public UserInfoBean getReceiver() {
        return receiver;
    }

    public void setReceiver(UserInfoBean receiver) {
        this.receiver = receiver;
    }
}
