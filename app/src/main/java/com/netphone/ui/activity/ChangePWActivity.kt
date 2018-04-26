package com.netphone.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.netphone.BuildConfig
import com.netphone.R
import com.netphone.databinding.ActivityChangePwBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.listener.OnChangePasswordListener
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
        binding.click = onClick()
    }

    override fun initData() {
        binding.click = onClick()
        binding.title.title.text = context.resources.getString(R.string.change_password)

        if (BuildConfig.DEBUG) {
            binding.etOld.setText("123456")
            binding.etNew.setText("123456")
            binding.etSure.setText("123456")
        }
    }

    inner class onClick {
        open fun submit(view: View) {
            val oldPW = binding.etOld.getText().toString().trim()
            if (TextUtils.isEmpty(oldPW)) {
                toasts(context.resources.getString(R.string.old_passowrd) + context.resources.getString(R.string.not_null))
                return
            }
            val newPW = binding.etNew.getText().toString().trim()
            val againPw = binding.etSure.getText().toString().trim()
            if (TextUtils.isEmpty(newPW)) {
                toasts(context.resources.getString(R.string.new_passowrd) + context.resources.getString(R.string.not_null))
                return
            }
            if (TextUtils.isEmpty(againPw)) {
                toasts(context.resources.getString(R.string.input_again) + context.resources.getString(R.string.new_passowrd))
                return
            }
            if (oldPW == newPW) {
                toasts(context.resources.getString(R.string.password_came))
                return
            }

            if (againPw != newPW) {
                toasts(context.resources.getString(R.string.new_password_not_came))
                return
            }




            LTApi.getInstance().changePassword(oldPW, newPW, object : OnChangePasswordListener {
                override fun onSuccess() {
                    activity.runOnUiThread {
                        toasts(context.getResources().getString(R.string.change_seccess))
                        jump(LoginActivity::class.java)
                    }
                }

                override fun onFail(code: Int, error: String?) {
                    toasts(error!!)
                }
            })
        }
    }

}


