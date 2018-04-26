package com.netphone.netsdk.socket

import com.google.gson.Gson
import com.meidp.voicelib.ADPCM
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.utils.CRC16
import com.netphone.netsdk.utils.CmdUtils
import com.netphone.netsdk.utils.LogUtil
import com.netphone.netsdk.utils.VoiceUtil

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
    private var voiceRecord: VoiceUtil? = null

    @Synchronized
    open fun play() {
        isPlayVoice = true
        thread {
            var adpcm = ADPCM()
            val recvBuf = ByteArray(VoiceUtil.getBufferSize() / 2 + Constant.VOICE_DATA_HEARD)
            val recvPacket = DatagramPacket(recvBuf, recvBuf.size)
            var playPerpare = VoiceUtil.playPerpare()
            LogUtil.error("UdpSocket.kt", "88\tplay()\n" + "端口号" + recordPort);

            while (isPlayVoice) {
                if (udpSocket == null || udpSocket!!.isClosed) {
                    //                    initClient()
                    instance.connect()
                    continue
                }
                try {
                    udpSocket!!.receive(recvPacket);
                } catch (e: Exception) {
                    LogUtil.error("SocketManageService.kt${Gson().toJson(recvPacket.data)}", e)
//                    initClient()
                    instance
                }

                var data = recvPacket.data
                var voice = ByteArray(data.size - Constant.VOICE_DATA_HEARD)
                LogUtil.error("SocketManageService.kt", "478\n" + "有收到消息\t端口号$recordPort")
                System.arraycopy(data, Constant.VOICE_DATA_HEARD - 1, voice, 0, data.size - Constant.VOICE_DATA_HEARD)
                adpcm.adpcm_thirdparty_reset()
                var decode = ShortArray(voice.size * 2)
                decode = adpcm.adpcm_decoder(voice, decode, voice.size)
                playPerpare.write(decode, 0, decode.size)
            }
        }
    }

    open fun record() {
        isRecordVoice = true
        voiceRecord = VoiceUtil.newInstance(object : VoiceUtil.RecordVoice {
            override fun onRecordDataListener(bytes: ByteArray) {
                //  发送录音数据
                LogUtil.error("UdpSocket.kt", "117\tonRecordDataListener()\n" + "有数据" + bytes.size + "端口号" + recordPort);
                sendData(CmdUtils.getInstance().getRecordVoiceData(bytes, idBytes), 0)
            }
        })
        thread { voiceRecord!!.record() }
    }

    open fun stopRecord() {
        isRecordVoice = false
        if (voiceRecord != null)
            voiceRecord!!.stopRecord()
        voiceRecord = null;
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
