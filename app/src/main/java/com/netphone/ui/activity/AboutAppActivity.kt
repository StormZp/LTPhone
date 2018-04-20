package com.netphone.ui.activity

import android.os.Bundle
import android.view.View
import com.netphone.R
import com.netphone.databinding.ActivityAboutBinding
import com.netphone.netsdk.base.AppBean
import com.netphone.utils.AppUtil
import com.storm.tool.base.BaseActivity


/**
 * Created storm
 * Time    2018/4/20 16:45
 * Message {关于app}
 */
class AboutAppActivity : BaseActivity<ActivityAboutBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_about)
    }

    override fun initData() {
        binding.title.title.text = context.resources.getString(R.string.look_version)
        binding.tvVersion.text = context.resources.getString(R.string.app_versioin) + ":" + " " + AppUtil.getAppVersionName(context)
    }

    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
        binding.click = OnClick()
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {

        }
    }
}
