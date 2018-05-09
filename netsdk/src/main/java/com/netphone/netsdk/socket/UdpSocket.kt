package com.netphone.netsdk.socket

import com.google.gson.Gson
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.utils.CRC16
import com.netphone.netsdk.utils.LogUtil
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.UnknownHostException

/**
 * Created by XYSM on 2018/4/24.
 */

class UdpSocket {

    private var udpSocket: DatagramSocket? = null//UDP客户端
    private var udpPacket: DatagramPacket? = null//UDP发送包

    private var recordPort: Int = 0//录音端口号


    fun connect(recordPort: Int) {
        closeUdp()

        this.recordPort = recordPort
        try {
            udpSocket = DatagramSocket(recordPort)//新建一个DatagramSocket,获取客户端本地端口号

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun connect() {
        closeUdp()
        if (recordPort != 0)
            try {
                udpSocket = DatagramSocket(recordPort)//新建一个DatagramSocket,获取客户端本地端口号

            } catch (e: Exception) {
                e.printStackTrace()
            }

    }

    fun closeUdp() {
        if (udpSocket != null) {
            udpSocket!!.close()
            udpSocket = null
        }
    }

    companion object {
        private var mUdpSocket: UdpSocket? = null
        private var address: InetAddress? = null//录音地址
        private var idBytes: ByteArray? = null//终端ID

        val instance: UdpSocket
            get() {
                val deviceId = CRC16.getDeviceId(LTConfigure.mContext)
                idBytes = CRC16.hexStringToBytes(deviceId)
                try {
                    address = InetAddress.getByName(TcpConfig.HOST)
                } catch (e: UnknownHostException) {
                    LogUtil.error("UdpSocket", e)
                }

                if (mUdpSocket == null) {
                    mUdpSocket = UdpSocket()
                }
                return mUdpSocket!!
            }
    }

    private var isRecordVoice = false;//是否录制音频
    private var isPlayVoice = false;//是否播放音频

    @Synchronized
    open fun play() {
        LogUtil.error("UdpSocket.kt","90\tplay()\n"+"开始播放");
    }
    open fun stopPlay(){
        LogUtil.error("UdpSocket.kt","93\tstopPlay()\n"+"停止播放");
    }

    open fun record() {
        LogUtil.error("UdpSocket.kt","97\trecord()\n"+"开始录制");
    }

    open fun stopRecord() {
        LogUtil.error("UdpSocket.kt","95\tstopRecord()\n"+"停止录制");
    }

    /**
     * 发送udp数据
     *
     * @param audios
     * @param sendMode 0：表示普通模式，用client发送；1;表示强制广播模式，用brocastClient发送
     */
    private fun sendData(audios: ByteArray, sendMode: Int) {
        when (sendMode) {
            0 -> {
                udpPacket = getCommonDatagramPacket(audios, audios.size, address!!, recordPort)
                if (udpSocket != null && !udpSocket!!.isClosed) {
                    LogUtil.error("SocketManageService.kt", "582\n" + "发送音频数据${Gson().toJson(audios)}")
                    udpSocket!!.send(udpPacket)
                } else {
//                    initClient()
                    instance
                }
            }
            1 -> {
            }
        }
    }

    private fun getCommonDatagramPacket(recvBuf: ByteArray, length: Int, addr: InetAddress, port: Int): DatagramPacket {
        return DatagramPacket(recvBuf, length, addr, port)
    }

}
