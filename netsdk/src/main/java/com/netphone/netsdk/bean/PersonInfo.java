package com.netphone.netsdk.bean;

import java.io.Serializable;

/**
 * Created by lgp on 2017/8/1.
 * 登录个人信息
 */

public class PersonInfo implements Serializable {
    private String RealName;//用户姓名
    private String UserId;//用户ID
    private int IsDizzy;//是否遥晕 1:是,0:否
    private int IsOnLine;
    private String HeadIcon;
    private String Description;
    private String Gender;
    private String ExpiredDate;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getIsOnLine() {
        return IsOnLine;
    }

    public void setIsOnLine(int isOnLine) {
        IsOnLine = isOnLine;
    }

    public String getHeadIcon() {
        return HeadIcon;
    }

    public void setHeadIcon(String headIcon) {
        HeadIcon = headIcon;
    }

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getIsDizzy() {
        return IsDizzy;
    }

    public void setIsDizzy(int isDizzy) {
        IsDizzy = isDizzy;
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "RealName='" + RealName + '\'' +
                ", UserId='" + UserId + '\'' +
                ", IsDizzy=" + IsDizzy +
                '}';
    }
}
