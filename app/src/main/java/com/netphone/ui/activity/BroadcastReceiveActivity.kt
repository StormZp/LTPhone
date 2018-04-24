package com.netphone.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.netphone.R
import com.netphone.config.EventConfig
import com.netphone.databinding.ActivityBroadcastReceiveBinding
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/23.
 */

class BroadcastReceiveActivity : BaseActivity<ActivityBroadcastReceiveBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_broadcast_receive)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun initData() {
        binding.click = OnClick()
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
            finish()
        }
    }
}
