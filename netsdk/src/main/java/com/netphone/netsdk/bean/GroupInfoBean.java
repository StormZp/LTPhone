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
    private String GroupID;

    private String GroupName;
    private List<GroupChildsBean> GroupChilds;

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

    public List<GroupChildsBean> getGroupChilds() {
        return GroupChilds;
    }

    public void setGroupChilds(List<GroupChildsBean> GroupChilds) {
        this.GroupChilds = GroupChilds;
    }

    @Override
    public String toString() {
        return "GroupInfoBean{" +
                "GroupID='" + GroupID + '\'' +
                ", GroupName='" + GroupName + '\'' +
                ", GroupChilds=" + GroupChilds +
                '}';
    }
}
