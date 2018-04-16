package com.netphone.ui.activity

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.netphone.R
import com.netphone.databinding.ActivityLoginBinding
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.bean.UserListBean
import com.netphone.netsdk.listener.OnLoginListener
import com.netphone.netsdk.utils.LogUtil
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/3.
 */

class LoginActivity : BaseActivity<ActivityLoginBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initBinding(R.layout.activity_login)
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }


    override fun initListener() {

    }

    override fun initData() {
        binding.click = onClick()

    }

    inner class onClick {
        open fun deletePsw(view: View) {
            toasts("删除")
        }

        open fun submit(view: View) {
//            toasts("登录")
//            var sendLogin = CmdUtils.getInstance().sendLogin( "debug", "123456")
//            TcpSocket.getInstance().addData(sendLogin)
            LTConfigure.getInstance().ltApi.login("debug", "123456", object : OnLoginListener {
                override fun onFail(code: Int, error: String?) {
                    LogUtil.error("LoginActivity.kt", "onFail\n" + "$error")
                    activity.runOnUiThread {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onSuccess(bean: UserInfoBean?) {
//                    LogUtil.error("LoginActivity.kt", "onSuccess\n" + Gson().toJson(personBean))
                    Constant.info = bean;
                    activity.runOnUiThread {
                        Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                    }
                    Constant.info = bean;

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


