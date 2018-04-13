package com.storm.tool.base


import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netphone.R
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.utils.EventBusUtil
import com.netphone.utils.ToastUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable


/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class BaseFragment<T : ViewDataBinding> : Fragment(), Serializable {
    lateinit var binding: T
    private var mParam1: String? = null
    private var mParam2: String? = null

    var isRegisterEventBus = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterEventBus) {
            EventBusUtil.unregister(this);
        }
    }

    fun initBinding(layout: Int, view: ViewGroup?) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), layout, view, false)
        mView = binding.root
        initData()
        initListener()
    }

    /**
     * 绑定事件总线
     */
    protected fun registerEventBus() {

        EventBusUtil.register(this);
        isRegisterEventBus = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterEventBus()
    }

    /**
     * 取消绑定事件总线
     */
    protected fun unregisterEventBus() {
        if (isRegisterEventBus)
            EventBusUtil.unregister(this);
        isRegisterEventBus = false
    }


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


    abstract fun initListener()

    abstract fun initData()

    lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return mView
    }

    /**
     * 跳转
     */
    open fun jump(intent: Intent) {
        startActivity(intent)
        activity.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }

    open fun jump(t: Class<*>, bundle: Bundle) {
        startActivity(Intent(activity, t).putExtras(bundle))
        activity.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }

    open fun jump(t: Class<*>) {
        startActivity(Intent(activity, t))
        activity.overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }

    /**
     * 权限申请
     */
    protected fun getRxPermissions(): RxPermissions {
        var rxPermissions = RxPermissions(activity)
        return rxPermissions
    }


    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

    }

    /**
     * 提示
     */
    open fun toasts(content: String) {
        ToastUtil.toasts(content)
    }

    open fun toastl(content: String) {
        ToastUtil.toastl(content)
    }
}
