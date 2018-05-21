package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.UserInfoBean;

import java.util.List;

/**
 * Created Storm<p>
 * Time    2018/5/21 10:23<p>
 * Message {获取某个群成员回调}
 */
public interface OnGetGroupMemberListener {
    void onGetMemberFail();
    void onGetMemberSuccess(List<UserInfoBean> members);

}
