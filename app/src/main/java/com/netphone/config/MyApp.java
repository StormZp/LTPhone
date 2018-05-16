package com.netphone.config;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.netphone.netsdk.LTConfigure;
import com.netphone.utils.CrashHandler;
import com.netphone.utils.ToastUtil;

/**
 * Created by XYSM on 2018/4/13.
 */

public class MyApp extends Application {
    private static Context mContext;
    private static MyApp   myApp;

    public static MyApp getInstense() {
        if (myApp == null) {
            myApp = new MyApp();
        }
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this.getApplicationContext();

        LTConfigure.init(mContext);

        //分包
        MultiDex.install(this);

        //崩溃处理
        CrashHandler.getInstance().init(mContext);

        ToastUtil.Companion.init(mContext);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }


    public static Context getContext() {
        return mContext;
    }
}
