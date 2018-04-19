package com.netphone.ui.activity

import android.os.Bundle
import android.view.View
import com.netphone.R
import com.netphone.databinding.ActivityInfoGroupBinding
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.GroupInfoBean
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/16.
 */
open class GroupInfoActivity : BaseActivity<ActivityInfoGroupBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_info_group)
    }

    override fun initData() {
        var userInfoBean = intent.extras.getSerializable("bean") as GroupInfoBean
        binding.title.title.text = userInfoBean.groupName


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