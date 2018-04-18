package com.netphone.netsdk.bean;

import com.netphone.gen.DaoSession;
import com.netphone.gen.GroupChatMsgBeanDao;
import com.netphone.gen.GroupInfoBeanDao;
import com.netphone.gen.UserInfoBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * Created by XYSM on 2018/4/18.
 */
@Entity
public class GroupChatMsgBean implements Serializable {

    /**
     * FromUserId : 17090410015273146616
     * FromUserName : 龚杰
     * Msg : cf
     * FromGroupId : 17090609520052554619
     */
    static final long serialVersionUID = 44L;

    private String FromUserId;
    private String FromUserName;
    private String Msg;
    private String FromGroupId;

    @Id(autoincrement = true)
    private Long id;

    @ToOne(joinProperty = "FromGroupId")
    private GroupInfoBean GroupInfoBean;

    @ToOne(joinProperty = "FromUserId")
    private UserInfoBean UserInfoBean;
    private String receiveId;
    @ToOne(joinProperty = "receiveId")
    private UserInfoBean receiveBean;

    private Long dateTime;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 541475279)
    private transient GroupChatMsgBeanDao myDao;


    @Generated(hash = 500398444)
    private transient String GroupInfoBean__resolvedKey;

    @Generated(hash = 1253302719)
    private transient String UserInfoBean__resolvedKey;

    @Generated(hash = 1153883242)
    private transient String receiveBean__resolvedKey;


    @Generated(hash = 276494666)
    public GroupChatMsgBean(String FromUserId, String FromUserName, String Msg,
            String FromGroupId, Long id, String receiveId, Long dateTime) {
        this.FromUserId = FromUserId;
        this.FromUserName = FromUserName;
        this.Msg = Msg;
        this.FromGroupId = FromGroupId;
        this.id = id;
        this.receiveId = receiveId;
        this.dateTime = dateTime;
    }

    @Generated(hash = 1465497762)
    public GroupChatMsgBean() {
    }


    public String getFromUserId() {
        return FromUserId;
    }

    public void setFromUserId(String FromUserId) {
        this.FromUserId = FromUserId;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String FromUserName) {
        this.FromUserName = FromUserName;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getFromGroupId() {
        return FromGroupId;
    }

    public void setFromGroupId(String FromGroupId) {
        this.FromGroupId = FromGroupId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1915095814)
    public GroupInfoBean getGroupInfoBean() {
        String __key = this.FromGroupId;
        if (GroupInfoBean__resolvedKey == null
                || GroupInfoBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupInfoBeanDao targetDao = daoSession.getGroupInfoBeanDao();
            GroupInfoBean GroupInfoBeanNew = targetDao.load(__key);
            synchronized (this) {
                GroupInfoBean = GroupInfoBeanNew;
                GroupInfoBean__resolvedKey = __key;
            }
        }
        return GroupInfoBean;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 920959817)
    public void setGroupInfoBean(GroupInfoBean GroupInfoBean) {
        synchronized (this) {
            this.GroupInfoBean = GroupInfoBean;
            FromGroupId = GroupInfoBean == null ? null : GroupInfoBean.getGroupID();
            GroupInfoBean__resolvedKey = FromGroupId;
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
    @Generated(hash = 2033425503)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupChatMsgBeanDao() : null;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 918976156)
    public UserInfoBean getUserInfoBean() {
        String __key = this.FromUserId;
        if (UserInfoBean__resolvedKey == null || UserInfoBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean UserInfoBeanNew = targetDao.load(__key);
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
    @Generated(hash = 2131795543)
    public void setUserInfoBean(UserInfoBean UserInfoBean) {
        synchronized (this) {
            this.UserInfoBean = UserInfoBean;
            FromUserId = UserInfoBean == null ? null : UserInfoBean.getUserId();
            UserInfoBean__resolvedKey = FromUserId;
        }
    }

    public String getReceiveId() {
        return this.receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 595854603)
    public UserInfoBean getReceiveBean() {
        String __key = this.receiveId;
        if (receiveBean__resolvedKey == null || receiveBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean receiveBeanNew = targetDao.load(__key);
            synchronized (this) {
                receiveBean = receiveBeanNew;
                receiveBean__resolvedKey = __key;
            }
        }
        return receiveBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 745371903)
    public void setReceiveBean(UserInfoBean receiveBean) {
        synchronized (this) {
            this.receiveBean = receiveBean;
            receiveId = receiveBean == null ? null : receiveBean.getUserId();
            receiveBean__resolvedKey = receiveId;
        }
    }
}
