package com.netphone.netsdk.bean;

import com.netphone.gen.DaoSession;
import com.netphone.gen.GroupInfoBeanDao;
import com.netphone.gen.UserInfoBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lgp on 2017/8/23.
 */
@Entity
public class GroupInfoBean implements Serializable {
    /**
     * GroupID : 17080718265624509
     * GroupName : 群组1
     * GroupChilds : [{"UserId":"17072514050381189","RealName":"gongjie3","IsOnLine":0},{"UserId":"17072511273496609","RealName":"gongjie4","IsOnLine":0},{"UserId":"17072315170270392","RealName":"gongjie","IsOnLine":0},{"UserId":"17072315175130800","RealName":"gongjie1","IsOnLine":1},{"UserId":"17072411371211550","RealName":"gongjie2","IsOnLine":0}]
     */
    /**
     * GroupID : 17090609520052554619
     * GroupName : LT1
     * HeadIcon : /Content/images/head/user2-160x160.jpg
     * GroupChilds : []
     * AllCount : 8
     * OnLineCount : 0
     * Micer : {}
     */
    static final long serialVersionUID = 42L;
    @Id
    private String             GroupID;
    private String             GroupName;
    private String             HeadIcon;
    private int                AllCount;
    private int                OnLineCount;
    private String             userId;//用户的id
    @Transient
    private UserInfoBean       Micer;
    @Transient
    private List<UserInfoBean> GroupChilds;
    @ToOne(joinProperty = "userId")
    private UserInfoBean       userBean;//用户的id
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2020276714)
    private transient GroupInfoBeanDao myDao;
    @Generated(hash = 1432798415)
    public GroupInfoBean(String GroupID, String GroupName, String HeadIcon, int AllCount, int OnLineCount, String userId) {
        this.GroupID = GroupID;
        this.GroupName = GroupName;
        this.HeadIcon = HeadIcon;
        this.AllCount = AllCount;
        this.OnLineCount = OnLineCount;
        this.userId = userId;
    }
    @Generated(hash = 1490267550)
    public GroupInfoBean() {
    }
    public String getGroupID() {
        return this.GroupID;
    }
    public void setGroupID(String GroupID) {
        this.GroupID = GroupID;
    }
    public String getGroupName() {
        return this.GroupName;
    }
    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }
    public String getHeadIcon() {
        return this.HeadIcon;
    }
    public void setHeadIcon(String HeadIcon) {
        this.HeadIcon = HeadIcon;
    }
    public int getAllCount() {
        return this.AllCount;
    }
    public void setAllCount(int AllCount) {
        this.AllCount = AllCount;
    }
    public int getOnLineCount() {
        return this.OnLineCount;
    }
    public void setOnLineCount(int OnLineCount) {
        this.OnLineCount = OnLineCount;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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
    @Generated(hash = 1885503356)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupInfoBeanDao() : null;
    }
}
