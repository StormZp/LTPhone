package com.netphone.ui.activity

import android.os.Bundle
import android.view.View
import com.netphone.R
import com.netphone.databinding.ActivityChangePwBinding
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseActivity

/**
 * Created XYSM
 * Time    2018/4/17 15:22
 * Message {修改密码}
 */

class ChangePWActivity : BaseActivity<ActivityChangePwBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding(R.layout.activity_change_pw)
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }


    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
    }

    override fun initData() {
        binding.click = onClick()
        setBarColor(R.color.black)
        binding.title.title.text = context.resources.getString(R.string.change_password)
    }

    inner class onClick {

        open fun deletePsw(view: View) {
            toasts("删除")
        }

        open fun submit(view: View) {
        }
    }

}


