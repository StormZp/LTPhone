package com.netphone.ui.activity

import android.os.Bundle
import android.view.View
import com.netphone.R
import com.netphone.databinding.ActivityUserInfoBinding
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/16.
 */
open class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_user_info)
    }

    override fun initData() {
        binding.title.title.text ="ldkjgfldf"
    }

    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
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