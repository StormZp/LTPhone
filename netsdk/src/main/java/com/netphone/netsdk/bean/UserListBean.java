package com.netphone.netsdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lgp on 2017/8/16.
 * 用户列表
 */

public class UserListBean implements Serializable {
    private List<UserInfoBean> UserInfo;
    private List<GroupInfoBean> GroupInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private int id;

    public List<UserInfoBean> getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(List<UserInfoBean> UserInfo) {
        this.UserInfo = UserInfo;
    }

    public List<GroupInfoBean> getGroupInfo() {
        return GroupInfo;
    }

    public void setGroupInfo(List<GroupInfoBean> GroupInfo) {
        this.GroupInfo = GroupInfo;
    }
}
