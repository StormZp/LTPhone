package com.netphone.netsdk.listener;

import com.netphone.netsdk.bean.PersonInfo;
import com.netphone.netsdk.bean.UserListBean;

/**
 * Created by XYSM on 2018/4/13.
 */

public interface OnLoginListener {
    void onSuccess(PersonInfo personBean);
    void onFail(int code,String error);
    void onComplete(UserListBean userListBean);
}
