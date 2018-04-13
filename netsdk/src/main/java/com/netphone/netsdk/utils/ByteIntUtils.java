package com.netphone.netsdk.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by lgp on 2017/7/31.
 */

public class ByteIntUtils {

    // 第一种方法：
    public static byte[] int2byte(int res) {

        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    public static short byte2int(byte[] res) {
    // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
// | 表示安位或
        short targets = (short) ((res[0] & 0xff)
                        | ((res[1] << 8) & 0xff00));
        return targets;
    }
    /**
     *    读取输入流中指定字节的长度
     *
     * @param in     输入流
     * @param length 指定长度
     * @return       指定长度的字节数组
     */
    public static byte[] readBytes(InputStream in, long length) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();// 建立字节流
        byte[] buffer = new byte[1024];//1024长度
        int read = 0;
        while (read < length) {// 循环将需要读取内容写入到bo中
            int cur = in.read(buffer, 0, (int) Math.min(1024, length - read));
            if (cur < 0) {//直到读到的返回标记为-1，表示读到流的结尾
                break;
            }
            read += cur;//每次读取的长度累加
            bo.write(buffer, 0, cur);
        }
        return bo.toByteArray();//返回内容
    }
    public static String utfToString(byte[] data) {
        String str = null;

        try {
            str = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return str;

    }
}
