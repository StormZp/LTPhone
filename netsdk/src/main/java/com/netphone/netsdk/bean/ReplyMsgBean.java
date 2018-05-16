package com.netphone.netsdk.bean;


import com.netphone.gen.DaoSession;
import com.netphone.gen.ReplyMsgBeanDao;
import com.netphone.gen.UserInfoBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

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


    private String userId;//当前用户id
    @NotNull
    private String receiveID;//消息列表的用户id
    private String receiveName;//消息列表的用户名
    private int    unread;//未读数
    private String lastMsg;//最后一条
    private long lastTime;//最后一条的时间

    @ToOne(joinProperty = "userId")
    private           UserInfoBean    user;
    @ToOne(joinProperty = "receiveID")
    private           UserInfoBean    receiver;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1482789800)
    private transient ReplyMsgBeanDao myDao;
    @Generated(hash = 309302885)
    public ReplyMsgBean(Long id, String userId, @NotNull String receiveID, String receiveName,
            int unread, String lastMsg, long lastTime) {
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
    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 14347509)
    public UserInfoBean getUser() {
        String __key = this.userId;
        if (user__resolvedKey == null || user__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 672230781)
    public void setUser(UserInfoBean user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getUserId();
            user__resolvedKey = userId;
        }
    }
    @Generated(hash = 2043777119)
    private transient String receiver__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 105969797)
    public UserInfoBean getReceiver() {
        String __key = this.receiveID;
        if (receiver__resolvedKey == null || receiver__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean receiverNew = targetDao.load(__key);
            synchronized (this) {
                receiver = receiverNew;
                receiver__resolvedKey = __key;
            }
        }
        return receiver;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 949687934)
    public void setReceiver(@NotNull UserInfoBean receiver) {
        if (receiver == null) {
            throw new DaoException(
                    "To-one property 'receiveID' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.receiver = receiver;
            receiveID = receiver.getUserId();
            receiver__resolvedKey = receiveID;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1698962568)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReplyMsgBeanDao() : null;
    }
    


}
