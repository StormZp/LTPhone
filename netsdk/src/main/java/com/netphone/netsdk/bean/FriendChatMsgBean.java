package com.netphone.netsdk.bean;

import com.netphone.gen.DaoSession;
import com.netphone.gen.FriendChatMsgBeanDao;
import com.netphone.gen.UserInfoBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:40<p>
 * Message {好友聊天单条bean}
 */
@Entity
public class FriendChatMsgBean implements Serializable {

    /**
     * ReceiveId : 17090515420560223893
     * msg : tyu
     * Name : debug111
     */
    static final long serialVersionUID = 45L;
    private String ReceiveId;//好友
    private String msg;
    private String Name;


    @Id(autoincrement = true)
    private Long         id;
    private String       sendId;//发送者
    private String       userId;//发送者
    @ToOne(joinProperty = "sendId")
    private UserInfoBean sendBean;
    @ToOne(joinProperty = "userId")
    private UserInfoBean user;

    private Long dateTime;

    @ToOne(joinProperty = "ReceiveId")
    private           UserInfoBean         UserInfoBean;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession           daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 738263186)
    private transient FriendChatMsgBeanDao myDao;

    @Generated(hash = 810318606)
    public FriendChatMsgBean(String ReceiveId, String msg, String Name, Long id,
            String sendId, String userId, Long dateTime) {
        this.ReceiveId = ReceiveId;
        this.msg = msg;
        this.Name = Name;
        this.id = id;
        this.sendId = sendId;
        this.userId = userId;
        this.dateTime = dateTime;
    }

    @Generated(hash = 743303664)
    public FriendChatMsgBean() {
    }

    public String getReceiveId() {
        return this.ReceiveId;
    }

    public void setReceiveId(String ReceiveId) {
        this.ReceiveId = ReceiveId;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSendId() {
        return this.sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public Long getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    @Generated(hash = 1157664268)
    private transient String sendBean__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 877489041)
    public UserInfoBean getSendBean() {
        String __key = this.sendId;
        if (sendBean__resolvedKey == null || sendBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao   = daoSession.getUserInfoBeanDao();
            UserInfoBean    sendBeanNew = targetDao.load(__key);
            synchronized (this) {
                sendBean = sendBeanNew;
                sendBean__resolvedKey = __key;
            }
        }
        return sendBean;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 728494189)
    public void setSendBean(UserInfoBean sendBean) {
        synchronized (this) {
            this.sendBean = sendBean;
            sendId = sendBean == null ? null : sendBean.getUserId();
            sendBean__resolvedKey = sendId;
        }
    }

    @Generated(hash = 1253302719)
    private transient String UserInfoBean__resolvedKey;
    @Generated(hash = 1867105156)
    private transient String user__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 654270903)
    public UserInfoBean getUserInfoBean() {
        String __key = this.ReceiveId;
        if (UserInfoBean__resolvedKey == null
                || UserInfoBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao       = daoSession.getUserInfoBeanDao();
            UserInfoBean    UserInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                UserInfoBean = UserInfoBeanNew;
                UserInfoBean__resolvedKey = __key;
            }
        }
        return UserInfoBean;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1387282139)
    public void setUserInfoBean(UserInfoBean UserInfoBean) {
        synchronized (this) {
            this.UserInfoBean = UserInfoBean;
            ReceiveId = UserInfoBean == null ? null : UserInfoBean.getUserId();
            UserInfoBean__resolvedKey = ReceiveId;
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 886087880)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFriendChatMsgBeanDao() : null;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

}
