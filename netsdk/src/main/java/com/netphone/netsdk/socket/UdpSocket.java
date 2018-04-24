package com.netphone.netsdk.socket;

import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.utils.CRC16;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by XYSM on 2018/4/24.
 */

public class UdpSocket {
    private DatagramSocket udpSocket;//UDP客户端
    private DatagramPacket udpPacket;//UDP发送包

    private int         recordPort;//录音端口号
    private InetAddress address;//录音地址
    private byte[]      idBytes;//终端ID

    public UdpSocket() {
        String deviceId = CRC16.getDeviceId(LTConfigure.mContext);
        idBytes = CRC16.hexStringToBytes(deviceId);
    }

}
