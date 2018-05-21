package com.netphone.netsdk.bean;

import java.util.List;

/**
 * Created Storm <p>
 * Time    2018/5/21 10:40<p>
 * Message {好友刷新bean}
 */
public class FriendReFreshBean {

    /**
     * {
     * "Count": 0,  /*总包数
     * // "Index": 0,  /*当前包号
     * //         "List": [
     * //   {
     * //       "UserId": "", /*用户ID
     * //           "RealName": "", /*用户名称
     * //           "IsOnLine": 0, /*是否在线
     * //           "HeadIcon": "", /*头像
     * //           "ExpiredDate": "", /*过期时间
     * //           "Py": "" /*名称拼音
     * //   }
     * // ]
     * //}
     */

    private int                Count;
    private int                Index;
    private List<UserInfoBean> List;

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

    public java.util.List<UserInfoBean> getList() {
        return List;
    }

    public void setList(java.util.List<UserInfoBean> list) {
        List = list;
    }
}
