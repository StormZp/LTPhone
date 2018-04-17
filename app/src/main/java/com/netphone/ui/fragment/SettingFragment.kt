package com.netphone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.netphone.R
import com.netphone.databinding.FragmentSettingBinding
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.ui.activity.ChangePWActivity
import com.netphone.ui.activity.UserInfoActivity
import com.netphone.utils.GlideCircleTransform
import com.storm.tool.base.BaseFragment

/**
 * Created by XYSM on 2018/4/16.
 */

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBinding(R.layout.fragment_setting, container)
        return mView
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
        binding.click = onClick()
    }

    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.setting_content)

        if (Constant.info != null) {
            binding.tvNickname.text = Constant.info.realName
//            binding.onlineState.text = if (Constant.info.isOnLine == 1) "在线1" else "离线1"
            binding.onlineSwitch.isChecked = true


            Glide.with(context).load( TcpConfig.URL + Constant.info.getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivHead)

        }
    }

    inner class onClick {
        open fun openUserInfo(view: View) {
            jump(UserInfoActivity::class.java)
        } open fun openPwChange(view: View) {
            jump(ChangePWActivity::class.java)
        }
    }

}
