package com.netphone.netsdk.utils

import android.app.Activity
import android.media.*
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.NoiseSuppressor
import android.os.Environment
import com.meidp.voicelib.ADPCM
import java.io.File


/**
 * Created by Storm on 2018/3/1.
 */

class VoiceUtil {

    companion object {
        var file: File = File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/reverseme.pcm")
        var isRecording = false;
        private var adpcm: ADPCM = ADPCM()
        private lateinit var listener: RecordVoice
        //16K采集率
        val frequency = 8000
        //格式
        val channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO
        //16Bit
        val audioEncoding = AudioFormat.ENCODING_PCM_16BIT

        var track: AudioTrack? = null
        var audioRecord: AudioRecord? = null

        open fun newInstance(listener: RecordVoice): VoiceUtil {
            var voiceUtil = VoiceUtil()
            Companion.listener = listener
            return voiceUtil;
        }

        open fun playPerpare(): AudioTrack {
            track = AudioTrack(AudioManager.STREAM_VOICE_CALL, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    getBufferSize(), AudioTrack.MODE_STREAM)
            track!!.play()

            return track!!
        }

        open fun playClose() {
            if (track != null) {
                track!!.stop()
                track!!.release()
                track = null
            }
        }

        open fun getBufferSize(): Int {

            val bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding) / 4
            return bufferSize
        }

        open fun setSpeakerphoneOn(on: Boolean, activity: Activity, audioManager: AudioManager) {
            if (on) {
                // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
                audioManager.setSpeakerphoneOn(true)
                // 添加的代码，恢复系统声音设置
                audioManager.setMode(AudioManager.STREAM_SYSTEM)
                activity.volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
                LogUtil.error("true打开喇叭扩音器")
            } else {
                audioManager.setSpeakerphoneOn(false)//关闭扬声器
                audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL)
                activity.volumeControlStream = AudioManager.STREAM_VOICE_CALL
                //把声音设定成Earpiece（听筒）出来，设定为正在通话中
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION)
                LogUtil.error("关闭扬声器")
            }
        }

    }


    open fun record() {


        LogUtil.error("voiceUtil.kt", "开始录音")
        var bufferSize = getBufferSize()
        stopRecord()

        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, frequency, channelConfiguration, audioEncoding, bufferSize)
        val buffer = ShortArray(bufferSize)
        audioRecord!!.startRecording()

        if (audioRecord!!.audioSessionId != null) {

            var canceler: AcousticEchoCanceler? = AcousticEchoCanceler.create(audioRecord!!.audioSessionId)//用于降低回声
            if (canceler != null)
                canceler.enabled = true
            var noiseSuppressor: NoiseSuppressor? = NoiseSuppressor.create(audioRecord!!.audioSessionId);
            if (noiseSuppressor != null)
                noiseSuppressor.enabled = true
        }

        isRecording = true
        while (isRecording && audioRecord != null) {
            val bufferReadResult = audioRecord!!.read(buffer, 0, bufferSize)
            //编码后的数据
            adpcm.adpcm_thirdparty_reset()
            var decode = ByteArray(buffer.size / 2)
            decode = adpcm.adpcm_coder(buffer, decode, buffer.size)
            listener.onRecordDataListener(decode)
        }
    }

    @Synchronized
    open fun stopRecord() {
        isRecording = false
        if (audioRecord != null) {
            audioRecord!!.stop()
            audioRecord!!.release()
            audioRecord = null;
        }
    }

    interface RecordVoice {
        fun onRecordDataListener(bytes: ByteArray)
    }


}
