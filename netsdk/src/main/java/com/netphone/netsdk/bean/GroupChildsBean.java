package com.netphone.netsdk.bean;

import java.io.Serializable;

/**
 * Created by lgp on 2017/8/23.
 * 群组成员
 */
public class GroupChildsBean implements Serializable {

    /**
     * UserId : 群组成员ID
     * RealName : 群组成员称呼
     * IsOnLine : 1
     * IsDizzy :
     */

    private String UserId;
    private String RealName;
    private boolean IsOnLine;
    private boolean IsDizzy;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String RealName) {
        this.RealName = RealName;
    }

    public boolean getIsOnLine() {
        return IsOnLine;
    }

    public void setIsOnLine(boolean IsOnLine) {
        this.IsOnLine = IsOnLine;
    }

    public boolean getIsDizzy() {
        return IsDizzy;
    }

    public void setIsDizzy(boolean IsDizzy) {
        this.IsDizzy = IsDizzy;
    }
}
