package com.netphone.netsdk.socket

import com.google.gson.Gson
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.utils.CRC16
import com.netphone.netsdk.utils.LogUtil
import com.netphone.netsdk.utils.UdpUtil
import com.test.jni.OnVoiceListener
import com.test.jni.VoiceUtil
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.UnknownHostException
import kotlin.concurrent.thread

/**
 * Created by XYSM on 2018/4/24.
 */

class UdpSocket {

    private var udpSocket: DatagramSocket? = null//UDP客户端
    private var udpPacket: DatagramPacket? = null//UDP发送包

    private var recordPort: Int = 0//录音端口号


    fun connect(recordPort: Int) {
        closeUdp()
        UdpUtil.init(LTConfigure.mContext)
        this.recordPort = recordPort
        try {
            udpSocket = DatagramSocket(recordPort)//新建一个DatagramSocket,获取客户端本地端口号

        } catch (e: Exception) {
            LogUtil.error("UdpSocket.kt", "39\tconnect()\n" + e);
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
                LogUtil.error("UdpSocket.kt", "74\tgetDeviceID()\n" + deviceId + "\t" + Gson().toJson(idBytes));
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
        LogUtil.error("UdpSocket.kt", "90\tplay()\n" + "开始播放");
        VoiceUtil.getInstance().initPlay()
        sendData(ByteArray(0), 0)

        thread { playVoiceWork.run() }
    }

    open fun stopPlay() {
        LogUtil.error("UdpSocket.kt", "93\tstopPlay()\n" + "停止播放");
        VoiceUtil.getInstance().stopPlay()
    }

    open fun record() {
        LogUtil.error("UdpSocket.kt", "97\trecord()\n" + "开始录制");
        VoiceUtil.getInstance().setOnVoiceListener(object : OnVoiceListener {
            override fun recording(data: ByteArray?) {
                LogUtil.error("UdpSocket.kt", "106\trecording()\n" + "录制中" + data!!.size);
                sendData(data!!, 0)
            }
        })
        VoiceUtil.getInstance().startRecord()
    }

    open fun stopRecord() {
        LogUtil.error("UdpSocket.kt", "95\tstopRecord()\n" + "停止录制");
        VoiceUtil.getInstance().stopRecord()
    }

    /**
     * 发送udp数据
     *
     * @param audios
     * @param sendMode 0：表示普通模式，用client发送；1;表示强制广播模式，用brocastClient发送
     */
    private fun sendData(audios: ByteArray, sendMode: Int) {
        var udpDataEncode = UdpUtil.udpDataEncode(audios)
        when (sendMode) {
            0 -> {

                udpPacket = getCommonDatagramPacket(udpDataEncode, udpDataEncode.size, address!!, recordPort)
                if (udpSocket != null && !udpSocket!!.isClosed) {
                    LogUtil.error("SocketManageService.kt", "582\n" + "发送音频数据${udpDataEncode.size}")

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

    private val playVoiceWork = Runnable {
        // 接收udp
        val recvBuf = ByteArray(VoiceUtil.getBufferSize() + 10)
        var count = 0
        val recvPacket = DatagramPacket(recvBuf, recvBuf.size)

        while (true) {
            try {

                if (udpSocket != null && !udpSocket!!.isClosed()) {
                    LogUtil.error("UdpSocket.kt", "153\t()\n" + "开始播放");
                    udpSocket!!.receive(recvPacket)
                    val pack = recvPacket.data
                    if (pack != null && pack.size != 0) {
                        LogUtil.error("UdpSocket.kt", "153\t()\n" + pack.size);
                        var udpDataUncode = UdpUtil.udpDataUncode(pack)
                        VoiceUtil.getInstance().writePlayData(udpDataUncode)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
