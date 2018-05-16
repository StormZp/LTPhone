package com.test.jni;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by XYSM on 2018/5/15.
 */

public class VoiceUtil {
    private static VoiceUtil instance;
    public static final int BUFFER_SIZE = 80;
    public static final int HEAD_SIZE   = 10;

    public static VoiceUtil getInstance() {
        if (instance == null) {
            instance = new VoiceUtil();
        }
        return instance;
    }

    ExecutorService recordThread = Executors.newFixedThreadPool(1);

    private AcousticEchoCanceler canceler;//用于降低回声
    private AutomaticGainControl comtrol;//自动增益控制
    private NoiseSuppressor      noiseSuppressor;//用于降低回声


    private int samp_rate     = 8000;//采样率
    private int audioFormat   = AudioFormat.ENCODING_PCM_16BIT;//bit
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;//单声道
    private int audioSource   = MediaRecorder.AudioSource.MIC;//录制模式
    private int bufferSize;//每一帧大小

    private boolean isRecording;//是否正在录制

    /*----------------------------------录制----------------------------------*/
    private AudioRecord record = null;//用于录音
    private ADPCM            adpcm;//用于转码
    private OnRecordListener onRecordListener;

    public void initRecord(OnRecordListener onRecordListener) {
        stopRecord();
        this.onRecordListener = onRecordListener;
        if (adpcm == null)
            adpcm = new ADPCM();
        if (bufferSize == 0) {
            bufferSize = AudioRecord.getMinBufferSize(samp_rate, channelConfig, audioFormat) / 2;
        }
        record = new AudioRecord(audioSource, samp_rate, channelConfig, audioFormat, bufferSize);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && record.getAudioSessionId() != 0) {
            initAEC(record.getAudioSessionId());
            initNS(record.getAudioSessionId());
        }
    }

    public void startReocrd() {
        isRecording = true;
        recordThread.execute(recordWork);
    }

    public void stopRecord() {
        isRecording = false;
        if (record != null) {
            record.stop();
            record.release();
            record = null;
        }
        adpcm = null;
        this.onRecordListener = null;
    }

    Runnable recordWork = new Runnable() {

        public void run() {
            if (!isRecording)
                return;
            record.startRecording();

            short[] audioData = new short[bufferSize / 2];
            byte[]  zip       = new byte[audioData.length / 2];
            int     num       = 0;

            while (isRecording) {
                num = record.read(audioData, 0, bufferSize / 2);
//                Log.e(TAG, "编码前:\t" + gson.toJson(audioData));
                if (num == AudioRecord.ERROR_INVALID_OPERATION) {
//                    Log2Util.error(TAG, "Error ERROR_INVALID_OPERATION");
                } else if (num == AudioRecord.ERROR_BAD_VALUE) {
//                    Log2Util.error(TAG, "Error ERROR_BAD_VALUE");
                } else {
//                    Log2Util.error(TAG, "OK, 捕获了  " + num + " bytes !");
                    adpcm.adpcm_thirdparty_reset();
                    zip = adpcm.adpcm_coder(audioData, zip, audioData.length);

                    if (zip != null && onRecordListener != null) {
                        onRecordListener.recording(zip);
                    }
                }
            }
        }
    };


    public boolean isRecording() {
        return isRecording;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean initAEC(int audioSession) {
        if (canceler != null) {
            return false;
        }
        if (AcousticEchoCanceler.isAvailable()) {
            canceler = AcousticEchoCanceler.create(audioSession);
            if (canceler != null) {
                canceler.setEnabled(true);
                return canceler.getEnabled();
            } else
                return false;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean initNS(int audioSession) {

        if (noiseSuppressor != null) {
            return false;
        }
        if (NoiseSuppressor.isAvailable()) {
            noiseSuppressor = NoiseSuppressor.create(audioSession);
            if (noiseSuppressor != null) {
                noiseSuppressor.setEnabled(true);
                return noiseSuppressor.getEnabled();
            } else
                return false;
        } else {
            return false;
        }
    }

    public interface OnRecordListener {
        void recording(byte[] arr);
    }



    /*----------------------------------播放----------------------------------*/


    private boolean    isPlaying;
    private AudioTrack track;//播放声音

    public void initPlayer() {
        stopPlayer();
        isPlaying = true;
        if (adpcm == null)
            adpcm = new ADPCM();
        if (bufferSize == 0) {
            bufferSize = AudioRecord.getMinBufferSize(samp_rate, channelConfig, audioFormat) / 2;
        }
        track = new AudioTrack(AudioManager.STREAM_VOICE_CALL, samp_rate, channelConfig, audioFormat,
                bufferSize * 2 * 10, AudioTrack.MODE_STREAM);
        track.play();
    }


    public void setPlayData(byte[] data) {
        short[] decode = new short[data.length * 2];
        adpcm.adpcm_thirdparty_reset();
        decode = adpcm.adpcm_decoder(data, decode, data.length);

        if (track != null && isPlaying)
            track.write(decode, 0, decode.length);
    }

    public void stopPlayer() {
        isPlaying = false;
        if (track != null) {
            track.stop();
            track.release();
            track = null;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

}
