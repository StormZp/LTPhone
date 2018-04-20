package com.netphone.config;

import android.app.Application;
import android.content.Context;
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
    }


    public static Context getContext() {
        return mContext;
    }
}
