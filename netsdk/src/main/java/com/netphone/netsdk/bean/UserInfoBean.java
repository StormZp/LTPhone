package com.netphone.netsdk.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.netphone.netsdk.utils.Cn2Spell;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by lgp on 2017/8/23.
 * 用户列表item
 */
@Entity
public class UserInfoBean implements Serializable, Comparable<UserInfoBean> {
    /**
     * IsOnLine : 0
     * RealName : gongjie3
     * UserId : 17072514050381189
     * IsDizzy : false
     */
    static final long serialVersionUID = 42L;

    private String RealName;//用户姓名
    @Id
    private String UserId;//用户ID
    private String IsDizzy;//是否遥晕 1:是,0:否
    private String IsOnLine;
    private String HeadIcon;
    private String Description;
    private String Gender;
    private String ExpiredDate;
    @Transient
    private LastPositionBean LastPosition;


    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母



    @Generated(hash = 946396616)
    public UserInfoBean(String RealName, String UserId, String IsDizzy, String IsOnLine,
            String HeadIcon, String Description, String Gender, String ExpiredDate,
            String pinyin, String firstLetter) {
        this.RealName = RealName;
        this.UserId = UserId;
        this.IsDizzy = IsDizzy;
        this.IsOnLine = IsOnLine;
        this.HeadIcon = HeadIcon;
        this.Description = Description;
        this.Gender = Gender;
        this.ExpiredDate = ExpiredDate;
        this.pinyin = pinyin;
        this.firstLetter = firstLetter;
    }

    @Generated(hash = 1818808915)
    public UserInfoBean() {
    }



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

    public String getIsOnLine() {
        return IsOnLine;
    }

    public void setIsOnLine(String IsOnLine) {
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

    public String getIsDizzy() {
        return IsDizzy;
    }

    public void setIsDizzy(String isDizzy) {
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
    public int compareTo(@NonNull UserInfoBean another) {
        if (getFirstLetter().equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!getFirstLetter().equals("#") && another.getFirstLetter().equals("#")){
            return -1;
        } else {
            return getPinyin().compareToIgnoreCase(another.getPinyin());
        }
    }
}
