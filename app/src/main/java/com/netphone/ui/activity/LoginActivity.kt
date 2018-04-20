package com.netphone.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.githang.statusbar.StatusBarCompat
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
import com.netphone.utils.ToastUtil
import com.netphone.utils.ToastUtil.Companion.context
import com.netphone.utils.ToastUtil.Companion.toasts
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/3.
 */

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    var muLoginListener: MuLoginListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding(R.layout.activity_login)
    }

    override fun onDestroy() {
        super.onDestroy()
        muLoginListener = null
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }


    override fun initListener() {
        muLoginListener = MuLoginListener(this)
        LTConfigure.getInstance().setOnNetworkListener(object : OnNetworkListener {
            override fun onNoNet() {
            }

            override fun onWifiNet() {
            }

            override fun onMobileNet() {
            }

            override fun onConnectFail() {
                activity.runOnUiThread {
                    ToastUtil.toastl(ToastUtil.context!!.resources.getString(R.string.net_connect) + ToastUtil.context!!.resources.getString(R.string.fail))
                }
            }

            override fun onConnectSuccess() {
                activity.runOnUiThread {
                    ToastUtil.toastl(ToastUtil.context!!.resources.getString(R.string.net_connect) + ToastUtil.context!!.resources.getString(R.string.success))
                }
            }
        })
    }

    override fun initData() {
        binding.click = onClick()
        StatusBarCompat.setStatusBarColor(this, context.resources.getColor(R.color.black), false);

        if (BuildConfig.DEBUG) {
            binding.etAccount.setText("debug")
            binding.etPassword.setText("123456")
        }
    }

    inner class onClick {
        open fun deletePsw(view: View) {
            binding.etPassword.setText("")
        }

        open fun submit(view: View) {
            var password = binding.etPassword.text.toString().trim()
            var account = binding.etAccount.text.toString().trim()
            if (TextUtils.isEmpty(account)) {
                toasts(context.resources.getString(R.string.account_not_null))
            }
            LTConfigure.getInstance().ltApi.login(account, password, muLoginListener!!)
        }
    }


    class MuLoginListener(act: LoginActivity) : OnLoginListener {
        var activity = act
        override fun onFail(code: Int, error: String?) {
            LogUtil.error("LoginActivity.kt", "onFail\n" + "$error")
            activity.activity.runOnUiThread {
                toasts(error!!)
            }
        }

        override fun onSuccess(bean: UserInfoBean?) {
//                    LogUtil.error("LoginActivity.kt", "onSuccess\n" + Gson().toJson(personBean))
            Constant.info = bean;
            activity.activity.runOnUiThread {
                toasts(context!!.resources.getString(R.string.text_login) + context!!.resources.getString(R.string.success))
                activity.showProgress()
            }
            Constant.info = bean;
        }


        override fun onComplete(userListBean: UserListBean?) {
            activity.runOnUiThread {
                activity.hideProgress()
                Constant.listBean = userListBean;
                activity.jump(MainActivity::class.java)
                activity.finish()
            }
        }
    }
}


