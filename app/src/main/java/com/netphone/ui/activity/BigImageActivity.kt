package com.netphone.ui.activity

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.netphone.R
import com.netphone.databinding.ActivityBigImageBinding
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.ImageBean
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/20.
 */

class BigImageActivity : BaseActivity<ActivityBigImageBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_big_image)
    }

    override fun initData() {
        binding.title.title.text = context.getResources().getString(R.string.photo);

        var imageBean = intent.extras.getSerializable("bean") as ImageBean
        Glide.with(this).load(TcpConfig.URL+imageBean.getResourceHref()).error(R.mipmap.ic_launcher).into(binding.image);//.transform(new GlideCircleTransform(this))
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
