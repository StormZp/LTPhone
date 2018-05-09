package com.netphone.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.githang.statusbar.StatusBarCompat
import com.netphone.BuildConfig
import com.netphone.R
import com.netphone.config.MyApp
import com.netphone.databinding.ActivityLoginBinding
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.listener.OnLoginListener
import com.netphone.netsdk.listener.OnNetworkListener
import com.netphone.netsdk.utils.LogUtil
import com.netphone.netsdk.utils.SharedPreferenceUtil
import com.netphone.utils.ToastUtil
import com.storm.developapp.tools.AppManager
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/3.
 */

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding(R.layout.activity_login)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }


    override fun initListener() {
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
                    if (SharedPreferenceUtil.read(Constant.Auto_Login, false)) {
                        binding.auto.isChecked = true
                        onClick().submit(View(context))
                    }
                }
            }
        })
    }

    override fun initData() {
        binding.click = onClick()
        StatusBarCompat.setStatusBarColor(this, context.resources.getColor(R.color.black), false);

        if (BuildConfig.DEBUG) {
            binding.etAccount.setText(SharedPreferenceUtil.read(Constant.username, ""))
            binding.etPassword.setText(SharedPreferenceUtil.read(Constant.password, ""))
        }


    }

    inner class onClick {
        open fun deletePsw(view: View) {
            binding.etPassword.setText("")
        }

        open fun submit(view: View) {
            var password = binding.etPassword.text.toString().trim()
            var account = binding.etAccount.text.toString().trim()

            if (binding.auto.isChecked) {
                SharedPreferenceUtil.put(Constant.Auto_Login, true)
            }
            if (TextUtils.isEmpty(account)) {
                toasts(context.resources.getString(R.string.account_not_null))
            }

            if (!LTConfigure.getInstance().isIsInit) {
                LTConfigure.init(MyApp.getContext())
            }

            LTConfigure.getInstance().ltApi.login(account, password, object : OnLoginListener {
                override fun onSuccess(bean: UserInfoBean?) {
                    Constant.info = bean;
                    activity.runOnUiThread {
                        ToastUtil.toasts(ToastUtil.context!!.resources.getString(R.string.text_login) + ToastUtil.context!!.resources.getString(R.string.success))
                        jump(MainActivity::class.java)
                        finish()
                    }
                    Constant.info = bean;
                }

                override fun onFail(code: Int, error: String?) {
                    LogUtil.error("LoginActivity.kt", "onFail\n" + "$error")
                    activity.runOnUiThread {
                        ToastUtil.toasts(error!!)
                    }
                }

            })
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.appManager.finishAllActivity()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}


