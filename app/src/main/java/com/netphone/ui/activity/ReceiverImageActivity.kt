package com.netphone.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.netphone.R
import com.netphone.adapter.ImageAdapter
import com.netphone.databinding.ActivityReceiverImageBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/21.
 */

class ReceiverImageActivity : BaseActivity<ActivityReceiverImageBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_receiver_image)
    }

    override fun initData() {
//        var receiverImages = LTApi.getInstance().getReceiverImages(LtConstant.info.userId)
        var receiverImages = LTApi.getInstance().getReceiverImages(LTApi.getInstance().currentInfo.userId)

        binding.title!!.title.setText(context.resources.getString(R.string.photo))
        binding.recycle.adapter = ImageAdapter(context, receiverImages)
        binding.recycle.layoutManager = LinearLayoutManager(context)
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
