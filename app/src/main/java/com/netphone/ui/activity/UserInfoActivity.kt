package com.netphone.ui.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.netphone.R
import com.netphone.databinding.ActivityUserInfoBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.utils.GlideCircleTransform
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
        binding.title.title.text = context.resources.getString(R.string.user_info)

        var currentInfo = LTApi.newInstance().currentInfo
        binding.account.setText(currentInfo.realName)
        binding.sex.setText(currentInfo.gender)
        Glide.with(context).load(TcpConfig.URL + currentInfo!!.headIcon).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivHead)


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