package com.test.jni;

/**
 * Created by Storm on 2018/3/4.
 */

public class ADPCM {
    static {
        System.loadLibrary("webrtc");
    }

    public native void adpcm_thirdparty_reset();

    public native byte[] adpcm_coder(short[] indata, byte[] outdata, int len);

    public native short[] adpcm_decoder(byte[] indata, short[] outdata, int len);


    public short[] volumeAdjust(short[] data) {
        int vo = 98;
        for (int i = 0; i < data.length; i++) {

            short tmp = (short) (data[i] * 5);

            if (tmp > 32767) {
                tmp = 32767;
            } else if (tmp < -32768)
                tmp = -32768;

            data[i] = tmp;
        }

        return data;
    }

    public void calc1(short[] lin,int off,int len) {
//        int i,j;
//        for (i = 0; i < len; i++) {
//            j = lin[i+off];
//            lin[i+off] = (short)(j>>2);
//        }
    }
}
