package com.netphone.ui.activity

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.bumptech.glide.Glide
import com.githang.statusbar.StatusBarCompat
import com.netphone.R
import com.netphone.databinding.ActivityVoiceFriendBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.listener.OnFriendCallListener
import com.netphone.netsdk.utils.LogUtil
import com.netphone.utils.GlideCircleTransform
import com.storm.tool.base.BaseActivity
import java.text.SimpleDateFormat


/**
 * Created by XYSM on 2018/4/24.
 */

class FriendVoiceActivity : BaseActivity<ActivityVoiceFriendBinding>() {
    private var isOpenOn = false//是否开扩音
    private var isSound = false;//是否有声音

    private var audioManager: AudioManager? = null
    private lateinit var userInfoBean: UserInfoBean
    private var speakPhone = false

    private var mp: MediaPlayer? = null//铃声
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_voice_friend)
    }

    override fun onDestroy() {
        super.onDestroy()
        LTApi.getInstance().turnOffCall()
        playStop()
    }

    override fun initData() {
        StatusBarCompat.setStatusBarColor(this, context.resources.getColor(R.color.bg_voice), false);
        audioManager == getSystemService(Context.AUDIO_SERVICE) as AudioManager
        binding.click = OnClick()

        var state = intent.extras.getInt("state", 0)
        showButton(state)

        userInfoBean = intent.extras.getSerializable("bean") as UserInfoBean
        binding.tvName.text = userInfoBean.realName
        Glide.with(activity).load(TcpConfig.URL + userInfoBean.headIcon).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivHead)

        var id = ""
        if (state == 0) {  //主叫
            id = userInfoBean.userId
            playSound();
        } else { //被叫
            startPlayRing();
        }
        LTApi.getInstance().friendCall(id, object : OnFriendCallListener {
            override fun onCallStart() {
                activity.runOnUiThread {
                    playStop()
                    binding.state.text = context.resources.getString(R.string.calling_now)
                    var timeTask = TimeTask()
                    timeTask.execute(0)
                }

            }

            override fun onCallAccept() {
                activity.runOnUiThread {
                    binding.state.text = context.resources.getString(R.string.Setting_call_connection)
                    showButton(2)
                    playStop()
                }


            }

            override fun onCallReject() {
                activity.runOnUiThread {
                    toasts(context.resources.getString(R.string.refuse_class))
                    activity.finish()

                }

            }

            override fun onCallFail(state: Int, message: String?) {
                activity.runOnUiThread {

                    toasts(message!!)
                    activity.finish()
                }

            }
        })

    }

    private fun showButton(state: Int) {
        activity.runOnUiThread {
            when (state) {
                0 -> {//打电话的
                    binding.laySound.visibility = View.INVISIBLE
                    binding.layRefuse.visibility = View.INVISIBLE
                    binding.layOff.visibility = View.VISIBLE
                    binding.layReceive.visibility = View.INVISIBLE
                    binding.layIson.visibility = View.INVISIBLE
                }
                1 -> {//接电话的
                    binding.laySound.visibility = View.INVISIBLE
                    binding.layRefuse.visibility = View.VISIBLE
                    binding.layOff.visibility = View.INVISIBLE
                    binding.layReceive.visibility = View.VISIBLE
                    binding.layIson.visibility = View.INVISIBLE
                }
                2 -> {//开始通话
                    binding.laySound.visibility = View.VISIBLE
                    binding.layRefuse.visibility = View.INVISIBLE
                    binding.layOff.visibility = View.VISIBLE
                    binding.layReceive.visibility = View.INVISIBLE
                    binding.layIson.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun initListener() {
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {

        }

        open fun mute(view: View) {

//            if (audioManager != null)
            setAudio(isSound)
            isSound = !isSound
        }

        open fun refuse(view: View) {
            LTApi.getInstance().CallRefuse()
            finish()
        }

        open fun down(view: View) {
            finish()
        }

        open fun accept(view: View) {
            LTApi.getInstance().CallAccept()
            showButton(2)
        }

        open fun open(view: View) {
            isOpenOn = !isOpenOn
            setSpeakerphoneOn(isOpenOn, activity, audioManager)
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

    private fun setAudio(leg: Boolean) {
        if (leg) {
            binding.ivSound.setBackgroundResource(R.mipmap.icon_jingyin)
            binding.btnSound.setText(context.resources.getString(R.string.text_hands_free))
//            audioManager!!.isMicrophoneMute = false
        } else {
            binding.ivSound.setBackgroundResource(R.mipmap.icon_jingyin_sel)
//            audioManager!!.isMicrophoneMute = true

        }
    }

    //被叫取系统铃声
    private fun startPlayRing() {
        mp = MediaPlayer()
        try {
            mp!!.setDataSource(this, RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            mp!!.prepare()
            mp!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //主动拨打电话声音
    private fun playSound() {
        mp = MediaPlayer.create(this, R.raw.callring)//重新设置要播放的音频
        mp!!.start()
    }

    private fun playStop() {
        if (mp != null) {
            mp!!.stop();
            mp = null;
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
            binding.tvTime.setText(formatter.format((values[0]!! - 8 * 60 * 60) * 1000).toString() + "")
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }
    }


    private fun setSpeakerphoneOn(on: Boolean) {
        if (on) {
            // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
            audioManager!!.setSpeakerphoneOn(true)
            // 添加的代码，恢复系统声音设置
            audioManager!!.setMode(AudioManager.STREAM_SYSTEM)
            volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
            LogUtil.error("true打开喇叭扩音器")
        } else {
            audioManager!!.setSpeakerphoneOn(false)//关闭扬声器
            audioManager!!.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL)
            volumeControlStream = AudioManager.STREAM_VOICE_CALL
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager!!.setMode(AudioManager.MODE_IN_COMMUNICATION)
            LogUtil.error("关闭扬声器")
        }
    }
}
