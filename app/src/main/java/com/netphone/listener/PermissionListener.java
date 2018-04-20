package com.netphone.listener;

/**
 * Created by XYSM on 2018/4/20.
 */

public interface PermissionListener {
    void PermissionSuccess();
    void PermissionFail();
    void PermissionNever();
}
