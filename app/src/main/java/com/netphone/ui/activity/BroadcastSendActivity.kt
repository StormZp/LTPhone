package com.netphone.ui.activity

import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.view.View
import com.netphone.R
import com.netphone.config.EventConfig
import com.netphone.databinding.ActivityBroadcastSendBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseActivity
import java.text.SimpleDateFormat

/**
 * Created by XYSM on 2018/4/23.
 */

class BroadcastSendActivity : BaseActivity<ActivityBroadcastSendBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_broadcast_send)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun initData() {
        var extras = intent.extras
        var state = extras.getInt("state", 0)
        if (state == 0) {//接收方
            binding.submit.visibility = View.GONE
        } else {//广播方
            binding.click = OnClick()
            binding.submit.visibility = View.VISIBLE
//            binding.tv
        }

        var timeTask = TimeTask()
        timeTask.execute(0)
        registerEventBus()
    }

    override fun initListener() {
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.BROADCAST_OVER -> {
                finish()
            }
        }
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {
            LTApi.getInstance().stopBroadcastSend()
            finish()
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
            binding.time.text = formatter.format((values[0]!! - 8 * 60 * 60) * 1000)
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }
    }
}
