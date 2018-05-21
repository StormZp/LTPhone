package com.netphone.netsdk.bean;

import com.netphone.gen.DaoSession;
import com.netphone.gen.ImageBeanDao;
import com.netphone.gen.UserInfoBeanDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * Created Storm<p>
 * Time    2018/4/20 16:58<p>
 * Message {图片数据}
 */
@Entity
public class ImageBean implements Serializable {

    /**
     * Category : 1
     * FromUserId : 17080216141881996
     * ResourceName : 图片一
     * ResourceHref : /PublicInfoManage/AttachmentInfo/GetFile?keyValue=18032715061144912607
     */
    private int Category;
    private String FromUserId;
    private String ReceiveId;//用户ID
    private String ResourceName;
    private String ResourceHref;
    private long date; //时间段

    static final long serialVersionUID = 42L;
    @ToOne(joinProperty = "FromUserId")
    private UserInfoBean formUser;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 911538130)
    private transient ImageBeanDao myDao;
    @Generated(hash = 451238850)
    private transient String formUser__resolvedKey;

    @Generated(hash = 158614178)
    public ImageBean(int Category, String FromUserId, String ReceiveId, String ResourceName,
            String ResourceHref, long date) {
        this.Category = Category;
        this.FromUserId = FromUserId;
        this.ReceiveId = ReceiveId;
        this.ResourceName = ResourceName;
        this.ResourceHref = ResourceHref;
        this.date = date;
    }

    @Generated(hash = 645668394)
    public ImageBean() {
    }

    public int getCategory() {
        return this.Category;
    }

    public void setCategory(int Category) {
        this.Category = Category;
    }

    public String getFromUserId() {
        return this.FromUserId;
    }

    public void setFromUserId(String FromUserId) {
        this.FromUserId = FromUserId;
    }

    public String getReceiveId() {
        return this.ReceiveId;
    }

    public void setReceiveId(String ReceiveId) {
        this.ReceiveId = ReceiveId;
    }

    public String getResourceName() {
        return this.ResourceName;
    }

    public void setResourceName(String ResourceName) {
        this.ResourceName = ResourceName;
    }

    public String getResourceHref() {
        return this.ResourceHref;
    }

    public void setResourceHref(String ResourceHref) {
        this.ResourceHref = ResourceHref;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1000248935)
    public UserInfoBean getFormUser() {
        String __key = this.FromUserId;
        if (formUser__resolvedKey == null || formUser__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserInfoBeanDao targetDao = daoSession.getUserInfoBeanDao();
            UserInfoBean formUserNew = targetDao.load(__key);
            synchronized (this) {
                formUser = formUserNew;
                formUser__resolvedKey = __key;
            }
        }
        return formUser;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 308601812)
    public void setFormUser(UserInfoBean formUser) {
        synchronized (this) {
            this.formUser = formUser;
            FromUserId = formUser == null ? null : formUser.getUserId();
            formUser__resolvedKey = FromUserId;
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
    @Generated(hash = 1437742468)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getImageBeanDao() : null;
    }
    
}
