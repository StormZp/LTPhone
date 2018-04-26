package com.netphone.netsdk.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.Tool.Constant;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgp on 2017/8/16.
 * 封装对讲机通信各种发送指令
 */

public class CmdUtils {
    private final String TAG = CmdUtils.class.getCanonicalName();
    private static byte[]   cmds;
    private static CmdUtils mInstance;
    private int serialNo = 0;

    private CmdUtils() {

    }

    public synchronized static CmdUtils getInstance() {
        if (mInstance == null)
            mInstance = new CmdUtils();
        return mInstance;
    }

    /**
     * 返回根据协议后包装的byte[]数组
     */
    private byte[] idBytes;

    public byte[] sendCmds(byte cmdId, byte cmdId2, byte[] byteArray) {
        int packageLength = byteArray.length + 14;
        cmds = new byte[packageLength];//固定长度
        //按协议统一进行包装
        //帧头
        cmds[0] = 0X7E;
        //流水号
        byte[] serialHexNo = ByteIntUtils.int2byte(serialNo++);
        cmds[1] = serialHexNo[0];
        cmds[2] = serialHexNo[1];
        //终端ID
        if (idBytes == null) {
            String getDeviceId = CRC16.getDeviceId(context);
            if (context == null) {
                LogUtil.error(TAG, "是崩溃后 被赋值的context被回收，导致的 空context");
            }
            idBytes = CRC16.hexStringToBytes(getDeviceId);
            LogUtil.error(TAG, "getDeviceID=" + getDeviceId);
        }
        if (idBytes == null) {
            LogUtil.error(TAG, "idBytes仍然为null");
        }
        cmds[3] = idBytes[0];
        cmds[4] = idBytes[1];
        cmds[5] = idBytes[2];
        cmds[6] = idBytes[3];
        //指令
        cmds[7] = cmdId;
        cmds[8] = cmdId2;
        //数据长度，2个字节
        int    length = byteArray.length;
        byte[] bytes  = ByteIntUtils.int2byte(length);
        cmds[9] = bytes[0];
        cmds[10] = bytes[1];
        //数据内容
        System.arraycopy(byteArray, 0, cmds, 11, byteArray.length);
        //CRC16校验,取2位
        int    crc16    = CRC16.calcCrc16(cmds, 1, cmds.length - 1);
        byte[] crcBytes = ByteIntUtils.int2byte(crc16);
        if (cmds.length>=packageLength){
            cmds[packageLength - 3] = crcBytes[0];
            cmds[packageLength - 2] = crcBytes[1];
//        cmds[packageLength - 3] = 0X5E;
//        cmds[packageLength - 2] = 0X5E;
            // 帧尾
            cmds[packageLength - 1] = 0x7F;
        }

        return cmds;
    }

    /**
     * 返回根据协议后包装的byte[]数组
     */
    public byte[] sendFileCmds(byte cmdId, byte cmdId2, byte[] byteArray, byte[] fileArray) {
        int packageLength = fileArray.length + byteArray.length + 14 + 2;
        cmds = new byte[packageLength];//固定长度
        //按协议统一进行包装
        //帧头
        cmds[0] = 0X7E;
        //流水号
        byte[] serialHexNo = ByteIntUtils.int2byte(serialNo++);
        cmds[1] = serialHexNo[0];
        cmds[2] = serialHexNo[1];
        //终端ID
//        Random random = new Random();//默认构造方法
//        byte id = (byte) random.nextInt(100);
        if (idBytes == null) {
            String getDeviceId = CRC16.getDeviceId(context);
            idBytes = CRC16.hexStringToBytes(getDeviceId);
        }
        cmds[3] = idBytes[0];
        cmds[4] = idBytes[1];
        cmds[5] = idBytes[2];
        cmds[6] = idBytes[3];
        //指令
        cmds[7] = cmdId;
        cmds[8] = cmdId2;
        //body长度，2个字节
        int    length = byteArray.length + fileArray.length + 2;//body包含三部分
        byte[] bytes  = ByteIntUtils.int2byte(length);
        cmds[9] = bytes[0];
        cmds[10] = bytes[1];
        //body里面部分
        //第一部分,json数据长度
        byte[] jsonlengths = ByteIntUtils.int2byte(byteArray.length);
        cmds[11] = jsonlengths[0];
        cmds[12] = jsonlengths[1];
        //第二部分,json数据内容
        System.arraycopy(byteArray, 0, cmds, 13, byteArray.length);
        //第三部分，文件流内容
        System.arraycopy(fileArray, 0, cmds, 13 + byteArray.length, fileArray.length);
        //校验,CRC16校验
//        int crc16 = CRC16.calcCrc16(cmds,1,cmds.length-1);
//        byte[] crcBytes = ByteIntUtils.int2byte(crc16);
//        cmds[packageLength - 3] = crcBytes[0];
//        cmds[packageLength - 2] = crcBytes[1];
        cmds[packageLength - 3] = 0X5E;
        cmds[packageLength - 2] = 0X5E;
        // 帧尾
        cmds[packageLength - 1] = 0x7F;
        return cmds;
    }

    /**
     * 登录
     *
     * @param account
     * @param password
     */
    private Context context = LTConfigure.getInstance().getContext();

    /**
     * 发送登录
     *
     * @param account
     * @param password
     * @return
     */
    public byte[] sendLogin(String account, String password) {
//        this.context = context;
//        this.context = MyApp.app.mContext;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Account", account);
        map.put("Password", password);
        String json = new Gson().toJson(map);
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
//        Log2Util.debug(json + "");
        byte   cmdId  = 0x00;
        byte   cmdId2 = 0x00;
        byte[] temp   = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }


    /**
     * 发送群聊心跳包
     *
     * @return
     */
    public byte[] sendGroupBeat() {
        byte[] enmy = new byte[8];
        enmy[0] = (byte) 0xff;
        enmy[1] = (byte) 0;
        enmy[2] = (byte) 0;
        enmy[3] = idBytes[0];
        enmy[4] = idBytes[1];
        enmy[5] = idBytes[2];
        enmy[6] = idBytes[3];
        enmy[7] = (byte) 0xff;
        return enmy;
    }

    /**
     * 修改密码
     *
     * @param oldpw
     * @param newpw
     * @return
     */
    public byte[] editPW(String oldpw, String newpw) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("oldpwd", oldpw);
        map.put("newpwd", newpw);
        String json = new Gson().toJson(map);
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
        byte   cmdId     = (byte) 0x00;
        byte   cmdId2    = (byte) 0x10;
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 收到用户列表推送
     */
    public byte[] notifServerRececivedUserList() {
        byte   cmdId     = 0x01;
        byte   cmdId2    = 0x00;
        byte[] byteArray = new byte[1];
        byteArray[0] = 0x00;
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 发起通话请求
     */
    public byte[] sendCallRequest(String userId) {
        byte cmdId  = 0x00;
        byte cmdId2 = 0x02;
        //再进行UTF8编码
        byte[] byteArray = userId.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 发起视频请求
     */
    public byte[] sendLookRequest(String userId) {
        byte cmdId  = 0x00;
        byte cmdId2 = 0x06;
        //再进行UTF8编码
        byte[] byteArray = userId.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 处理通话
     */
    public byte[] handleCall(int voiceflag) {
        byte   cmdId     = 0x01;
        byte   cmdId2    = 0x01;
        byte[] byteArray = new byte[1];
        if (voiceflag == 0) {
            byteArray[0] = 0x01; //拒绝通话
        } else if (voiceflag == 1) {
            byteArray[0] = 0x00; //接受通话
        }
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 处理视频
     */
    public byte[] handleLook(int voiceflag) {
        byte   cmdId     = 0x01;
        byte   cmdId2    = 0x04;
        byte[] byteArray = new byte[1];
        if (voiceflag == 0) {
            byteArray[0] = 0x01; //拒绝通话
        } else if (voiceflag == 1) {
            byteArray[0] = 0x00; //接受通话
        }
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 主动挂断通话
     */
    public byte[] turnOffCall() {
        byte   cmdId     = 0x00;
        byte   cmdId2    = 0x03;
        byte[] byteArray = new byte[1];
        byteArray[0] = 0x00; //没有内容，随意写一个
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 主动挂断视频
     */
    public byte[] turnOffLook() {
        byte   cmdId     = 0x00;
        byte   cmdId2    = 0x07;
        byte[] byteArray = new byte[1];
        byteArray[0] = 0x00; //没有内容，随意写一个
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 收到对方挂断通话
     */
    public byte[] replyTurnOffOthers() {
        byte   cmdId     = 0x01;
        byte   cmdId2    = 0x03;
        byte[] byteArray = new byte[1];
        byteArray[0] = 0x00; //没有内容，随意写一个
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 获取音频录音数据包
     */
    public byte[] getRecordVoiceData(byte[] audios, byte[] idBytes) {
//            LogUtil.error("获取音频录音数据包");
        byte[] data = new byte[audios.length + Constant.VOICE_DATA_HEARD];
        //帧头
        data[0] = (byte) 0xff;
        byte[] bytes = ByteIntUtils.int2byte(audios.length + 2);
        //语音长度
        data[1] = bytes[0];
        data[2] = bytes[1];
        //用于识别,取终端ID前四位
        data[3] = idBytes[0];
        data[4] = idBytes[1];
        data[5] = idBytes[2];
        data[6] = idBytes[3];

        System.arraycopy(audios, 0, data, Constant.VOICE_DATA_HEARD - 1, audios.length);
        //帧尾
        data[audios.length + Constant.VOICE_DATA_HEARD - 1] = (byte) 0xff;
        return data;
    }

    /**
     * 取消呼叫
     */
    public byte[] cancelVoiceCall(String userId) {
        byte cmdId  = 0x00;
        byte cmdId2 = 0x09;
        //再进行UTF8编码
        byte[] byteArray = userId.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 发送心跳包
     */
    public byte[] sendHeratPackage() {
        byte   cmdId     = 0x02;
        byte   cmdId2    = 0x00;
        byte[] byteArray = new byte[1];
        byteArray[0] = 0x00; //没有内容，随意写一个
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 发送取消强制广播
     */
    public byte[] sendCancelForce() {
        byte   cmdId     = 0x00;
        byte   cmdId2    = 0x19;
        byte[] byteArray = new byte[1];
        byteArray[0] = 0x00; //没有内容，随意写一个
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 上传GPS
     *
     * @param longitude
     * @param latitude
     */
    public byte[] uploadGPS(String longitude, String latitude) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Lon", longitude);
        map.put("Lat", latitude);
        String json = new Gson().toJson(map);
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
        byte   cmdId     = 0x00;
        byte   cmdId2    = 0x01;
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 一键呼救
     *
     * @param longitude
     * @param latitude
     */
    public byte[] uploadhelp(String longitude, String latitude) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Lon", longitude);
        map.put("Lat", latitude);
        String json = new Gson().toJson(map);
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
        byte   cmdId     = 0x00;
        byte   cmdId2    = 0x18;
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 发群消息
     *
     * @param ReceiveId
     * @param msg
     */
    public byte[] sendGroupCommonBeanApi(String ReceiveId, String msg, byte cmdId, byte cmdId2) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("GroupId", ReceiveId);
        map.put("Message", msg);
        String json = new Gson().toJson(map);
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 发送好友消息
     *
     * @param ReceiveId
     * @param msg
     */
    public byte[] sendFriendCommonBeanApi(String ReceiveId, String msg, byte cmdId, byte cmdId2) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ReceiveId", ReceiveId);
        map.put("msg", msg);
        String json = new Gson().toJson(map);
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 上传文件，图片，视频...
     */
    public byte[] uploadFile(byte[] fileArray, byte cmdId, byte cmdId2, Map<String, Object> map) {
        String json = new Gson().toJson(map);
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendFileCmds(cmdId, cmdId2, byteArray, fileArray);
        return temp;
    }

    /**
     * 公共的封装发送数据方法
     */
    public byte[] commonApi(byte cmdId, byte cmdId2, String id) {
        if (TextUtils.isEmpty(id))
            return null;
        byte[] byteArray = id.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 公共的封装发送数据方法,空内容
     */
    public byte[] commonApi2(byte cmdId, byte cmdId2) {
        byte[] byteArray = new byte[1];
        byteArray[0] = 0x00; //没有内容，随意写一个
        byte[] temp = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 公共的带有发送体的方法
     *
     * @param ReceiveId
     * @param msg
     */
    public byte[] sendCommonBeanApi(String ReceiveId, String msg, byte cmdId, byte cmdId2) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ReceiveId", ReceiveId);
        map.put("msg", msg);
        String json = new Gson().toJson(map);
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }

    /**
     * 公共的带有map的方法
     */
    public byte[] sendCommonMapApi(Map<String, Object> map, byte cmdId, byte cmdId2) {
        String json = "0";
        if (map != null) {
            json = new Gson().toJson(map);
        }
        //再进行UTF8编码
        byte[] byteArray = json.getBytes(Charset.forName("utf-8"));
        byte[] temp      = sendCmds(cmdId, cmdId2, byteArray);
        return temp;
    }
}
