package com.netphone.netsdk.bean;

import java.io.Serializable;

/**
 * Created by lgp on 2017/9/1.
 * 强制加入群聊实体类
 */

public class GroupComeBean implements Serializable {


    /**
     * GroupId : 群组ID
     * GroupName :  群组名称
     * AdminUserId : 创建人ID
     * AdminName :  创建人姓名
     * Port : UDP服务端口号
     */

    private String GroupId;
    private String GroupName;
    private String AdminUserId;
    private String AdminName;
    private int Port;

    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
    }


    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public String getAdminUserId() {
        return AdminUserId;
    }

    public void setAdminUserId(String AdminUserId) {
        this.AdminUserId = AdminUserId;
    }

    public String getAdminName() {
        return AdminName;
    }

    public void setAdminName(String AdminName) {
        this.AdminName = AdminName;
    }

}
