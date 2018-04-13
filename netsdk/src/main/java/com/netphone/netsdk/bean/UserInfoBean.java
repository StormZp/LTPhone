package com.netphone.netsdk.bean;

import java.io.Serializable;

/**
 * Created by lgp on 2017/8/23.
 * 用户列表item
 */
public class UserInfoBean implements Serializable {
    /**
     * IsOnLine : 0
     * RealName : gongjie3
     * UserId : 17072514050381189
     * IsDizzy : false
     */

    private int IsOnLine;
    private String RealName;
    private String UserId;
    private String HeadIcon;
    private String ExpiredDate;
    private boolean IsDizzy;

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
    }

    public boolean isDizzy() {
        return IsDizzy;
    }

    public void setDizzy(boolean dizzy) {
        IsDizzy = dizzy;
    }

    public String getHeadIcon() {
        return HeadIcon;
    }

    public void setHeadIcon(String headIcon) {
        HeadIcon = headIcon;
    }

    public int getIsOnLine() {
        return IsOnLine;
    }

    public void setIsOnLine(int IsOnLine) {
        this.IsOnLine = IsOnLine;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String RealName) {
        this.RealName = RealName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public boolean isIsDizzy() {
        return IsDizzy;
    }

    public void setIsDizzy(boolean IsDizzy) {
        this.IsDizzy = IsDizzy;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "IsOnLine=" + IsOnLine +
                ", RealName='" + RealName + '\'' +
                ", UserId='" + UserId + '\'' +
                ", IsDizzy=" + IsDizzy +
                '}';
    }
}
