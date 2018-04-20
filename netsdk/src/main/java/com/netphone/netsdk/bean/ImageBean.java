package com.netphone.netsdk.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created Storm
 * Time    2018/4/20 16:58
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
    
}
