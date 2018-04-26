package com.netphone.ui.dialog

import android.os.Bundle
import android.view.View
import com.netphone.R
import com.netphone.databinding.DialogMessageBinding
import com.netphone.gen.UserInfoBeanDao
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.BroadcastBean
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/20.
 */

class MessageDialog : BaseActivity<DialogMessageBinding>() {
    private var isCall = false
    private lateinit var bean: BroadcastBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.dialog_message)
    }

    override fun initData() {
        var extras = intent.extras
        bean = extras.getSerializable("bean") as BroadcastBean


        var userInfoBeanDao = LTConfigure.getInstance().daoSession.userInfoBeanDao
        var unique = userInfoBeanDao.queryBuilder().where(UserInfoBeanDao.Properties.UserId.eq(bean.fromUserId)).unique()

        if (unique != null) {
            binding.name.text = context.resources.getString(R.string.come_form) + unique.realName
        }

        binding.content.setText(bean.message)
        binding.click = OnClick()
    }

    override fun initListener() {
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun sure(view: View) {
            if (isCall) {//第二步
                var toString = binding.content.text.toString()
                LTApi.getInstance().sendFriendMessage(bean.fromUserId, toString)
                finish()
            } else {//第一步
                isCall = true
                binding.content.setText("")
                binding.content.isEnabled = true
                binding.ivSubmit.setText(context.resources.getString(R.string.text_sure))
            }
        }

        open fun nunn(view: View) {
        }

        open fun finish(view: View) {
            activity.finish()
        }
    }


}
