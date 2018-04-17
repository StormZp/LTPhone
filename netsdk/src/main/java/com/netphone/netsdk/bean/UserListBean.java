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


    public static class GroupInfoBean {
        /**
         * GroupID : 17090609520052554619
         * GroupName : LT1
         * HeadIcon : /Content/images/head/user2-160x160.jpg
         * GroupChilds : []
         * AllCount : 8
         * OnLineCount : 0
         * Micer : {}
         */

        private String GroupID;
        private String GroupName;
        private String HeadIcon;
        private int AllCount;
        private int OnLineCount;
        private MicerBean Micer;
        private List<?> GroupChilds;

        public String getGroupID() {
            return GroupID;
        }

        public void setGroupID(String GroupID) {
            this.GroupID = GroupID;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public String getHeadIcon() {
            return HeadIcon;
        }

        public void setHeadIcon(String HeadIcon) {
            this.HeadIcon = HeadIcon;
        }

        public int getAllCount() {
            return AllCount;
        }

        public void setAllCount(int AllCount) {
            this.AllCount = AllCount;
        }

        public int getOnLineCount() {
            return OnLineCount;
        }

        public void setOnLineCount(int OnLineCount) {
            this.OnLineCount = OnLineCount;
        }

        public MicerBean getMicer() {
            return Micer;
        }

        public void setMicer(MicerBean Micer) {
            this.Micer = Micer;
        }

        public List<?> getGroupChilds() {
            return GroupChilds;
        }

        public void setGroupChilds(List<?> GroupChilds) {
            this.GroupChilds = GroupChilds;
        }

        public static class MicerBean {
        }
    }
}
