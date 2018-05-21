package com.netphone.netsdk.bean;

import java.util.List;

/**
 * Created Storm <p>
 * Time    2018/5/21 10:41<p>
 * Message {群刷新bean}
 */
public class GroupReFreshBean {

    /**
     * Count : 0
     * Index : 0
     * List : [{"GroupID":"","GroupName":"","HeadIcon":"","AllCount":0,"OnLineCount":0,"Micer":{"UserId":"","UserName":""}}]
     */

    private int                 Count;
    private int                 Index;
    private List<GroupInfoBean> List;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public java.util.List<GroupInfoBean> getList() {
        return List;
    }

    public void setList(java.util.List<GroupInfoBean> list) {
        List = list;
    }
}
