package com.netphone.netsdk.utils;

import android.content.Context;

/**
 * Created by XYSM on 2018/5/11.
 */

public class UdpUtil {
    private static byte[] idBytes;

    private static int VOID_HEAD_LENGHT = 10;

    public static void init(Context context) {
        if (idBytes == null) {
            String getDeviceId = CRC16.getDeviceId(context);
            idBytes = CRC16.hexStringToBytes(getDeviceId);
        }
    }

    /**
     * udp 数据压缩
     *
     * @return
     */
    public static byte[] udpDataEncode(byte[] audios) {

        byte[] voices = new byte[audios.length + VOID_HEAD_LENGHT];
        voices[0] = (byte) 0xff;//帧头
        //语音长度
        int    length = audios.length + 2;
        byte[] bytes  = ByteIntUtils.int2byte(length);
        voices[1] = bytes[0];
        voices[2] = bytes[1];
        //用于识别,取终端ID前四位
        voices[3] = idBytes[0];
        voices[4] = idBytes[1];
        voices[5] = idBytes[2];
        voices[6] = idBytes[3];


        System.arraycopy(audios, 0, voices, VOID_HEAD_LENGHT-1, audios.length);
        //帧尾
        voices[audios.length + VOID_HEAD_LENGHT-1] = (byte) 0xff;

        return voices;
    }

    /**
     * udp 数据解压
     *
     * @return
     */
    public static byte[] udpDataUncode(byte[] pack) {
        byte[] voiceLengths = new byte[4];
        //数据长度
        System.arraycopy(pack, 1, voiceLengths, 0, 2);
        int length = ByteUtil.getInt(voiceLengths, 0);

        byte[] voicepacks = new byte[length - 2];
        LogUtil.error("UdpUtil", "56\tudpDataUncode()\n" + voicepacks.length + "\t" + pack.length);
        System.arraycopy(pack, VOID_HEAD_LENGHT-1, voicepacks, 0, length - 2);
        return voicepacks;
    }


}
