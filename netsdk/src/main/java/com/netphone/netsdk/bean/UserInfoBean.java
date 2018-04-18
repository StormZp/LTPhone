package com.netphone.netsdk.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.netphone.netsdk.utils.Cn2Spell;

import java.io.Serializable;

/**
 * Created by lgp on 2017/8/23.
 * 用户列表item
 */
public class UserInfoBean implements Serializable, Comparable<UserInfoBean> {
    /**
     * IsOnLine : 0
     * RealName : gongjie3
     * UserId : 17072514050381189
     * IsDizzy : false
     */

    private String RealName;//用户姓名
    private String UserId;//用户ID
    private int IsDizzy;//是否遥晕 1:是,0:否
    private int IsOnLine;
    private String HeadIcon;
    private String Description;
    private String Gender;
    private String ExpiredDate;
    private LastPositionBean LastPosition;


    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
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

    public int getIsDizzy() {
        return IsDizzy;
    }

    public void setIsDizzy(int isDizzy) {
        IsDizzy = isDizzy;
    }

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

    public String getPinyin() {
        if (TextUtils.isEmpty(pinyin) && !TextUtils.isEmpty(RealName)) {
            pinyin = Cn2Spell.getPinYin(RealName); // 根据姓名获取拼音
            firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
            if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
                firstLetter = "#";
            }
        } else {
            if (TextUtils.isEmpty(pinyin))
                pinyin = "#";
        }
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        if (TextUtils.isEmpty(firstLetter) && !TextUtils.isEmpty(RealName)) {
            pinyin = Cn2Spell.getPinYin(RealName); // 根据姓名获取拼音
            firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
            if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
                firstLetter = "#";
            }
        } else {
            if (TextUtils.isEmpty(firstLetter))
                firstLetter = "#";
        }
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }


    @Override
    public int compareTo(@NonNull UserInfoBean userInfoBean) {
        if (!TextUtils.isEmpty(userInfoBean.getFirstLetter()) && !userInfoBean.getFirstLetter().equals("#")) {
            return 1;
        } else if (!TextUtils.isEmpty(userInfoBean.getFirstLetter()) && userInfoBean.getFirstLetter().equals("#")) {
            return -1;
        } else {
            return getPinyin().compareToIgnoreCase(userInfoBean.getPinyin());
        }
    }
}
