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
    private Context mContext;

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


    public Context getContext() {
        return mContext;
    }
}
