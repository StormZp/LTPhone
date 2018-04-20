package com.netphone.utils;

import android.app.Activity;
import android.content.Context;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.netphone.R;
import com.netphone.listener.PermissionListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by XYSM on 2018/4/20.
 */

public class PermissionUtil {
    public static void requiestPermission(Activity activity, final Context context, final PermissionListener listener, final String... permissions) {
        final RxPermissions rxPermissions = new RxPermissions(activity);

        rxPermissions.shouldShowRequestPermissionRationale(activity, permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (!aBoolean)
//                {context.startActivity(new Intent(context, PermissionDilog.class));}
                new AlertView(context.getResources().getString(R.string.warn), context.getResources().getString(R.string.permissions_hint), context.getResources().getString(R.string.text_cancel),
                        new String[]{context.getResources().getString(R.string.text_sure)}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
                                @Override
                                public void accept(Permission permission) throws Exception {
                                    if (permission.granted) {
                                        // 用户已经同意该权限
//                                        LTConfigure.getInstance().startLocationService();
                                        listener.PermissionSuccess();
                                    } else if (permission.shouldShowRequestPermissionRationale) {
                                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                        listener.PermissionFail();
                                    } else {
                                        // 用户拒绝了该权限，并且选中『不再询问』
                                        listener.PermissionNever();
                                    }
                                }
                            });
                        }
                    }
                }).show();
                else
                listener.PermissionSuccess();
            }
        });
    }
}
