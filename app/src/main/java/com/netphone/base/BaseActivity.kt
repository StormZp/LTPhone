package com.storm.tool.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.githang.statusbar.StatusBarCompat
import com.netphone.R
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.utils.EventBusUtil
import com.netphone.utils.ProgressUtils
import com.netphone.utils.ToastUtil
import com.storm.developapp.tools.AppManager
import com.tbruyelle.rxpermissions2.RxPermissions
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @创建作者    Storm
 * @创建时间    2017-09-18 23:29
 * @创建描述    ${activity 基类}
 */
open abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    lateinit var binding: T
    lateinit var context: Context
    lateinit var activity: Activity

    var isRegisterEventBus = false

    val PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.appManager.addActivity(this)
        context = this
        activity = this
    }

    override fun onResume() {
        super.onResume()

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //沉浸式
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            val decorView = window.decorView
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
//        }
    }

    /**
     * 绑定事件总线
     */
    protected fun registerEventBus() {
        EventBusUtil.register(this);
        isRegisterEventBus = true
    }

    /**
     * 取消绑定事件总线
     */
    protected fun unregisterEventBus() {
        if (isRegisterEventBus)
            EventBusUtil.unregister(this);
        isRegisterEventBus = false
    }

    abstract fun initData()
    abstract fun initListener()


    override fun onDestroy() {
        super.onDestroy()
        AppManager.appManager.removeActivity(this)
        unregisterEventBus()
    }


    /**
     * 转菊花
     */
    fun showProgress() {
        if (!activity.isFinishing)
            ProgressUtils.showProgressDialog(context)

    }

    fun hideProgress() {
        ProgressUtils.cannelProgressDialog()
    }

    /**
     * 绑定
     */
    fun initBinding(layout: Int) {

        binding = DataBindingUtil.setContentView<T>(this, layout)!!
//        setBarColor(R.color.white)
        StatusBarCompat.setStatusBarColor(this,context.resources.getColor( R.color.white), true);
        initData()
        initListener()
    }


    /**
     * 跳转
     */
    open fun jump(t: Class<*>) {
        startActivity(Intent(activity, t))
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }

    open fun jump(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }

    open fun jump(t: Class<*>, bundle: Bundle) {
        startActivity(Intent(activity, t).putExtras(bundle))
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }

    /**
     * 提示
     */
    open fun toasts(content: String) {
        activity.runOnUiThread {
            ToastUtil.toasts(content)
        }
    }

    open fun toastl(content: String) {
        ToastUtil.toastl(content)
    }


    /**
     * 权限申请
     */
    protected fun getRxPermissions(): RxPermissions {
        var rxPermissions = RxPermissions(activity)
        return rxPermissions!!
    }


    /**
     * eventbus封装
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEventBusCome(appBean: AppBean<Any>?) {
        if (appBean != null) {
            receiveEvent(appBean);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    open fun onStickyEventBusCome(appBean: AppBean<Any>?) {
        if (appBean != null) {
            receiveStickyEvent(appBean);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param appBean 事件
     */
    abstract fun receiveEvent(appBean: AppBean<Any>)

    /**
     * 接受到分发的粘性事件
     *
     * @param appBean 粘性事件
     */
    abstract fun receiveStickyEvent(appBean: AppBean<Any>)


}