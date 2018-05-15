package com.netphone.ui.activity

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.githang.statusbar.StatusBarCompat

import com.netphone.R
import com.netphone.config.EventConfig
import com.netphone.databinding.ActivityVoicePlayBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.utils.LogUtil
import com.storm.tool.base.BaseActivity
import java.text.SimpleDateFormat

/**
 * Created by XYSM on 2018/5/15.
 */

class VoicePlayActivity : BaseActivity<ActivityVoicePlayBinding>() {
    private var isOpenOn = false//是否开扩音
    private var audioManager: AudioManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_voice_play)
    }

    override fun onDestroy() {
        super.onDestroy()
        LTApi.getInstance().voiceStop();
//        playStop()

    }

    override fun initData() {
        StatusBarCompat.setStatusBarColor(this, context.resources.getColor(R.color.bg_voice), false);
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        binding.click = OnClick()

        when (intent.extras.getInt("state", 0)) {
            0 -> {//回播
                binding.layEcho.visibility = View.VISIBLE
                binding.layListener.visibility = View.GONE
            }
            1 -> {
                binding.layEcho.visibility = View.GONE
                binding.layListener.visibility = View.VISIBLE
            }
        }


        var timeTask = TimeTask()
        timeTask.execute(0)

        if (audioManager != null)
            setSpeakerphoneOn(isOpenOn, activity, audioManager)

    }

    override fun initListener() {
//        binding.title.back.setOnClickListener { finish() }
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.MANAGER_LISTENR_STOP -> {
                finish()
            }
        }
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {

        }

        open fun open(view: View) {
            isOpenOn = !isOpenOn
            if (audioManager != null)
                setSpeakerphoneOn(isOpenOn, activity, audioManager)
        }

        open fun down(view: View) {
            finish()
        }
    }

    open fun setSpeakerphoneOn(on: Boolean, activity: Activity, audioManager: AudioManager?) {
        if (on) {
            // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
            audioManager!!.setSpeakerphoneOn(true)
            // 添加的代码，恢复系统声音设置
            audioManager!!.setMode(AudioManager.STREAM_SYSTEM)
            activity.volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
            LogUtil.error("true打开喇叭扩音器")
            binding.ivIson.setBackgroundResource(R.mipmap.icon_yangshengqi_sel)
            binding.btnIson.setText(context.resources.getString(R.string.text_hands_free))
        } else {
            audioManager!!.setSpeakerphoneOn(false)//关闭扬声器
            audioManager!!.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL)
            activity.volumeControlStream = AudioManager.STREAM_VOICE_CALL
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION)
            LogUtil.error("关闭扬声器")
            binding.ivIson.setBackgroundResource(R.mipmap.icon_yangshengqi)
            binding.btnIson.setText(context.resources.getString(R.string.Handset))
        }
    }


    private val formatter = SimpleDateFormat("HH:mm:ss")

    inner class TimeTask : AsyncTask<Int?, Int?, Int?>() {
        private var count = 0

        override fun doInBackground(vararg params: Int?): Int {
            while (!activity.isFinishing) {
                publishProgress(count++)
                SystemClock.sleep(1000)
                if (activity.isFinishing) {
                    break
                }
            }

            return count;
        }


        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            binding.tvListener.setText(formatter.format((values[0]!! - 8 * 60 * 60) * 1000).toString() + "")
            binding.tvEchoTime.setText(formatter.format((values[0]!! - 8 * 60 * 60) * 1000).toString() + "")
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }
    }
}
