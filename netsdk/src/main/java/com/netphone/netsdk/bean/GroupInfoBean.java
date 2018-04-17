package com.netphone.netsdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lgp on 2017/8/23.
 */
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

    private String GroupID;
    private String GroupName;
    private String HeadIcon;
    private int AllCount;
    private int OnLineCount;
    private UserInfoBean Micer;
    private List<UserInfoBean> GroupChilds;

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getHeadIcon() {
        return HeadIcon;
    }

    public void setHeadIcon(String headIcon) {
        HeadIcon = headIcon;
    }

    public int getAllCount() {
        return AllCount;
    }

    public void setAllCount(int allCount) {
        AllCount = allCount;
    }

    public int getOnLineCount() {
        return OnLineCount;
    }

    public void setOnLineCount(int onLineCount) {
        OnLineCount = onLineCount;
    }

    public UserInfoBean getMicer() {
        return Micer;
    }

    public void setMicer(UserInfoBean micer) {
        Micer = micer;
    }

    public List<UserInfoBean> getGroupChilds() {
        return GroupChilds;
    }

    public void setGroupChilds(List<UserInfoBean> groupChilds) {
        GroupChilds = groupChilds;
    }
}
