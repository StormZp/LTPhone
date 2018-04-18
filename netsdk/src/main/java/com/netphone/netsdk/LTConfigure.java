package com.netphone.netsdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.netphone.netsdk.Tool.TcpCmd;
import com.netphone.netsdk.listener.OnNetworkListener;
import com.netphone.netsdk.service.NetworkConnectChangedReceiver;
import com.netphone.netsdk.service.SocketManageService;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.netsdk.utils.SharedPreferenceUtil;

/**
 * Created Storm
 * Time    2018/4/13 11:07
 * Message {这是力同的管理类}
 */
public class LTConfigure {
    private static LTApi ltApi;
    public OnNetworkListener mOnNetworkListener;


    private static LTConfigure mlt;
    private static Context mContext;
    private static NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;

    /**
     * sdk初始化
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
        if (mlt == null) {
            mlt = new LTConfigure();
        }
        initReceiver();
        initSocket();
        SharedPreferenceUtil.Companion.init(mContext);
        ltApi = LTApi.newInstance();
    }


    private static void initSocket() {
        mContext.startService(new Intent(mContext, SocketManageService.class));
    }

    /**
     * 初始化广播接收
     */
    private static void initReceiver() {
        mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.registerReceiver(mNetworkConnectChangedReceiver, intentFilter);
    }

    /**
     * 设置日志
     *
     * @param islog
     */
    public void setLog(boolean islog) {
        LogUtil.setDebug(islog);
    }

    /**
     * 销毁
     */
    public void onDestory() {
        mContext.unregisterReceiver(mNetworkConnectChangedReceiver);
        mNetworkConnectChangedReceiver = null;
        mlt = null;
        TcpCmd.isConnectBeat = false;
        TcpCmd.isGroupBeat = false;
    }


    public LTApi getLtApi() {
        return ltApi;
    }

    /**
     * 获取当前类
     *
     * @return
     */
    public static LTConfigure getInstance() {
        return mlt;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 获取网络状态监听
     *
     * @param onNetworkListener
     */
    public void setOnNetworkListener(OnNetworkListener onNetworkListener) {
        mOnNetworkListener = onNetworkListener;
    }


}
