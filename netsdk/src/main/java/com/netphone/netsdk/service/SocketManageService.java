package com.netphone.netsdk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.socket.TcpSocket;
import com.netphone.netsdk.utils.CRC16;
import com.netphone.netsdk.utils.LogUtil;

/**
 * Created by XYSM on 2018/4/13.
 */

public class SocketManageService extends Service {
//    private TcpSocket mTcpSocket;//tcp客户端

    private  byte[] idBytes;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()  {
        super.onCreate();
        LogUtil.error("SocketManageService", "30\nonCreate()" + "SocketManageService启动了");
//        mTcpSocket = new TcpSocket(LTConfigure.getInstance().getContext());
        TcpSocket.init(LTConfigure.getInstance().getContext());

        TcpSocket.getInstance().connect();
        //终端ID
        idBytes = CRC16.hexStringToBytes(CRC16.getDeviceId(LTConfigure.getInstance().getContext()));
    }
}
