package com.netphone.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.netphone.BuildConfig
import com.netphone.R
import com.netphone.databinding.ActivityLoginBinding
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.bean.UserListBean
import com.netphone.netsdk.listener.OnLoginListener
import com.netphone.netsdk.listener.OnNetworkListener
import com.netphone.netsdk.utils.LogUtil
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/3.
 */

class LoginActivity : BaseActivity<ActivityLoginBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding(R.layout.activity_login)
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }


    override fun initListener() {
        LTConfigure.getInstance().setOnNetworkListener(object : OnNetworkListener {
            override fun onServiceConnect() {
                toasts(context.resources.getString(R.string.net_connect) + " " + context.resources.getString(R.string.success))
            }

            override fun onNoNet() {
                LogUtil.error("LoginActivity.kt", "onNoNet\n" + "这里有个文本")
            }

            override fun onWifiNet() {
                LogUtil.error("LoginActivity.kt", "onWifiNet\n" + "这里有个文本")

            }

            override fun onMobileNet() {
                LogUtil.error("LoginActivity.kt", "onMobileNet\n" + "这里有个文本")
            }
        })
    }

    override fun initData() {
        binding.click = onClick()
        setBarColor(R.color.black)

        if (BuildConfig.DEBUG){
            binding.etAccount.setText("debug")
            binding.etPassword.setText("123456")
        }
    }

    inner class onClick {
        open fun deletePsw(view: View) {
//            toasts("删除")
            binding.etPassword.setText("")
        }

        open fun submit(view: View) {
            var password = binding.etPassword.text.toString().trim()
            var account = binding.etAccount.text.toString().trim()
            if (TextUtils.isEmpty(account)) {
                toasts(context.resources.getString(R.string.account_not_null))
            }
            LTConfigure.getInstance().ltApi.login(account, password, object : OnLoginListener {
                override fun onFail(code: Int, error: String?) {
                    LogUtil.error("LoginActivity.kt", "onFail\n" + "$error")
                    activity.runOnUiThread {
                        toasts(error!!)
                    }
                }

                override fun onSuccess(bean: UserInfoBean?) {
//                    LogUtil.error("LoginActivity.kt", "onSuccess\n" + Gson().toJson(personBean))
                    Constant.info = bean;
                    activity.runOnUiThread {
                        toasts(context.resources.getString(R.string.text_login) + context.resources.getString(R.string.success))
                    }
                    Constant.info = bean;
                    showProgress()
                }


                override fun onComplete(userListBean: UserListBean?) {
//                    LogUtil.error("LoginActivity.kt", "onComplete\n" + Gson().toJson(userListBean))
                    Constant.listBean = userListBean;
                    jump(MainActivity::class.java)
                    finish()
                }
            })
        }
    }

}


