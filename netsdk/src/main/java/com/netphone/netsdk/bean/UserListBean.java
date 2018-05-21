package com.netphone.netsdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created Storm <p>
 * Time    2018/5/21 10:42<p>
 * Message {用户列表}
 */
public class UserListBean implements Serializable {

    private List<UserInfoBean> UserInfo;
    private List<GroupInfoBean> GroupInfo;

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
