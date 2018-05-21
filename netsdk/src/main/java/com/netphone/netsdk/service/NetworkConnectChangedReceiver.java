package com.netphone.netsdk.service;

/**
 * Created by Storm on 2018/2/24.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.netphone.netsdk.LTConfigure;


/**
 * Created Storm<p>
 * Time    2018/5/21 10:17<p>
 * Message {  网络改变监控广播
              <p>
              监听网络的改变状态,只有在用户操作网络连接开关(wifi,mobile)的时候接受广播,<p>
              然后对相应的界面进行相应的操作，并将 状态 保存在我们的APP里面}
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 这个监听wifi的打开与关闭，与wifi的连接无关
//        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
//            LogUtil.error(  "wifiState" + wifiState);
//            switch (wifiState) {
//                case WifiManager.WIFI_STATE_DISABLED:
////                    APP.getInstance().setEnablaWifi(false);
//                    break;
//                case WifiManager.WIFI_STATE_DISABLING:
//
//                    break;
//                case WifiManager.WIFI_STATE_ENABLING:
//                    break;
//                case WifiManager.WIFI_STATE_ENABLED:
////                    APP.getInstance().setEnablaWifi(true);
//                    break;
//                case WifiManager.WIFI_STATE_UNKNOWN:
//                    break;
//                default:
//                    break;
//
//
//            }
//        }
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager
        // .WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，
        // 当然刚打开wifi肯定还没有连接到有效的无线
//        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
//            Parcelable parcelableExtra = intent
//                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//            if (null != parcelableExtra) {
//                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                NetworkInfo.State state = networkInfo.getState();
//                boolean isConnected = state == NetworkInfo.State.CONNECTED;// 当然，这边可以更精确的确定状态
//                LogUtil.error(  "isConnected" + isConnected);
//                if (isConnected) {
////                    APP.getInstance().setWifi(true);
//                } else {
////                    APP.getInstance().setWifi(false);
//                }
//            }
//        }
        // 这个监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        // 这个广播的最大弊端是比上边两个广播的反应要慢，如果只是要监听wifi，我觉得还是用上边两个配合比较合适
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            LogUtil.error(  "CONNECTIVITY_ACTION");

            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.isConnected()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
//                        LogUtil.error(  "当前WiFi连接可用 ");
                        if (  LTConfigure.getInstance().mOnNetworkListener!=null)   LTConfigure.getInstance().mOnNetworkListener.onWifiNet();
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
//                        LogUtil.error(  "当前移动网络连接可用 ");
                        if (  LTConfigure.getInstance().mOnNetworkListener!=null)   LTConfigure.getInstance().mOnNetworkListener.onMobileNet();
                    }
                } else {
//                    LogUtil.error(  "当前没有网络连接，请确保你已经打开网络 ");
                    if (  LTConfigure.getInstance().mOnNetworkListener!=null)   LTConfigure.getInstance().mOnNetworkListener.onNoNet();
                }
//                 LogUtil.error( "info.getTypeName()" + activeNetwork.getTypeName());
//                LogUtil.error(  "getSubtypeName()" + activeNetwork.getSubtypeName());
//                LogUtil.error(  "getState()" + activeNetwork.getState());
//                LogUtil.error(  "getDetailedState()" + activeNetwork.getDetailedState().name());
//                LogUtil.error(  "getDetailedState()" + activeNetwork.getExtraInfo());
//                LogUtil.error(  "getType()" + activeNetwork.getType());
            } else {   // not connected to the internet
//                LogUtil.error(  "当前没有网络连接，请确保你已经打开网络 ");
                if (  LTConfigure.getInstance().mOnNetworkListener!=null)   LTConfigure.getInstance().mOnNetworkListener.onNoNet();
//                EventBusUtil.sendEvent(new AppBean(EventConfig.NET_WORK_CHANGE,1));

            }
        }
    }
}
