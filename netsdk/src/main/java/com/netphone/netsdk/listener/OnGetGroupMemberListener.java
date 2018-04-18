package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.UserInfoBean;

import java.util.List;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnGetGroupMemberListener {
    void onGetMemberFail();
    void onGetMemberSuccess(List<UserInfoBean> members);

}
