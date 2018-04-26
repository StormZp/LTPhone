package com.netphone.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.bumptech.glide.Glide
import com.netphone.R
import com.netphone.databinding.ActivityUserInfoBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.listener.OnChangeUserInfoListener
import com.netphone.utils.GlideCircleTransform
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/16.
 */
open class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {

    private var head = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_user_info)
    }

    override fun initData() {
        binding.title.title.text = context.resources.getString(R.string.user_info)

        var currentInfo = LTApi.getInstance().currentInfo
        binding.account.setText(currentInfo.realName)
        binding.sex.setText(currentInfo.gender)
        Glide.with(context).load(TcpConfig.URL + currentInfo!!.headIcon).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivHead)


        if (currentInfo.getGender() == null || currentInfo.getGender().equals("0"))
            binding.sex.setText(getApplicationContext().getResources().getString(R.string.woman));
        else
            binding.sex.setText(getApplicationContext().getResources().getString(R.string.man));

        binding.content.setText(currentInfo.description)
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

        open fun sex(view: View) {
            AlertView(context.resources.getString(R.string.warn), context.resources.getString(R.string.choose_sex), context.resources.getString(R.string.text_cancel),
                    arrayOf(context.resources.getString(R.string.woman), context.resources.getString(R.string.man)), null, context, AlertView.Style.ActionSheet, OnItemClickListener { o, position ->
                if (position == 0) {
                    binding.sex.text = context.resources.getString(R.string.woman)
                } else if (position == 1) {
                    binding.sex.text = context.resources.getString(R.string.man)
                }
            }).show()
        }

        open fun change(view: View) {
            var userInfoBean = UserInfoBean()
            var username = binding.account.text.toString()
            if (TextUtils.isEmpty(username)) {
                toasts(context.getResources().getString(R.string.account) + context.getResources().getString(R.string.not_null));

                return
            }
            userInfoBean.realName = username
            if (!TextUtils.isEmpty(head)) {
                userInfoBean.headIcon = head
            }
            userInfoBean.gender = if (binding.sex.text.toString().equals("女")) "0" else "1"
            userInfoBean.description = binding.content.text.toString()
            LTApi.getInstance().changeUserInfo(userInfoBean, object : OnChangeUserInfoListener {
                override fun onSuccess() {
                    activity.runOnUiThread {
                        toasts(context.getResources().getString(R.string.update_success));
                        finish()
                    }
                }

                override fun onFail() {
                    activity.runOnUiThread {
                        toasts(context.getResources().getString(R.string.update_fail));
                        finish()
                    }
                }
            })

        }
    }

}