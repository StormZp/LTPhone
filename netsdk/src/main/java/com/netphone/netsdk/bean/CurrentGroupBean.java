package com.netphone.netsdk.bean;

import com.netphone.gen.CurrentGroupBeanDao;
import com.netphone.gen.DaoSession;
import com.netphone.gen.GroupInfoBeanDao;
import com.netphone.gen.UserInfoBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:40<p>
 * Message {当前群bean}
 */
@Entity
public class CurrentGroupBean {
    @Unique
    private           String        userId;//用户的id
    @ToOne(joinProperty = "userId")
    private           UserInfoBean  userBean;//用户的id
    private           String        GroupID;
    @ToOne(joinProperty = "GroupID")
    private  GroupInfoBean groupBean;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 680380946)
    private transient CurrentGroupBeanDao myDao;
    @Generated(hash = 528944296)
    public CurrentGroupBean(String userId, String GroupID) {
        this.userId = userId;
        this.GroupID = GroupID;
    }
    @Generated(hash = 744012183)
    public CurrentGroupBean() {
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getGroupID() {
        return this.GroupID;
    }
    public void setGroupID(String GroupID) {
        this.GroupID = GroupID;
    }
    @Generated(hash = 1618597290)
    private transient String userBean__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1082844326)
    public UserInfoBean getUserBean() {
        String __key = this.userId;
        if (userBean__resolvedKey == null || userBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean userBeanNew = targetDao.load(__key);
            synchronized (this) {
                userBean = userBeanNew;
                userBean__resolvedKey = __key;
            }
        }
        return userBean;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 699074777)
    public void setUserBean(UserInfoBean userBean) {
        synchronized (this) {
            this.userBean = userBean;
            userId = userBean == null ? null : userBean.getUserId();
            userBean__resolvedKey = userId;
        }
    }
    @Generated(hash = 916692355)
    private transient String groupBean__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1124033612)
    public GroupInfoBean getGroupBean() {
        String __key = this.GroupID;
        if (groupBean__resolvedKey == null || groupBean__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupInfoBeanDao targetDao = daoSession.getGroupInfoBeanDao();
            GroupInfoBean groupBeanNew = targetDao.load(__key);
            synchronized (this) {
                groupBean = groupBeanNew;
                groupBean__resolvedKey = __key;
            }
        }
        return groupBean;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 160282698)
    public void setGroupBean(GroupInfoBean groupBean) {
        synchronized (this) {
            this.groupBean = groupBean;
            GroupID = groupBean == null ? null : groupBean.getGroupID();
            groupBean__resolvedKey = GroupID;
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
    @Generated(hash = 644872510)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCurrentGroupBeanDao() : null;
    }
}
