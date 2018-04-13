package com.netphone.ui.activity

import android.os.Bundle
import com.netphone.R
import com.netphone.databinding.ActivityMainBinding
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.listener.OnNetworkListener
import com.netphone.netsdk.utils.LogUtil
import com.storm.tool.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_main)



        binding.text.setOnClickListener {

        }
    }

    override fun initData() {
    }

    override fun initListener() {
        LTConfigure.getInstance().setOnNetworkListener(object : OnNetworkListener {
            override fun onNoNet() {
                LogUtil.error("MainActivity.kt", "onNoNet\n" + "这里有个文本")
            }

            override fun onWifiNet() {
                LogUtil.error("MainActivity.kt", "onWifiNet\n" + "这里有个文本")

            }

            override fun onMobileNet() {
                LogUtil.error("MainActivity.kt", "onMobileNet\n" + "这里有个文本")
            }
        })

    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }
}
