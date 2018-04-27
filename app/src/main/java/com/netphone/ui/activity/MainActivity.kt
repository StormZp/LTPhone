package com.netphone.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.netphone.R
import com.netphone.config.EventConfig
import com.netphone.config.MyApp
import com.netphone.databinding.ActivityMainBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.listener.OnErrorListener
import com.netphone.ui.dialog.PermissionDialog
import com.netphone.ui.fragment.FriendsFragment
import com.netphone.ui.fragment.GroupsFragment
import com.netphone.ui.fragment.SessionFragment
import com.netphone.ui.fragment.SettingFragment
import com.netphone.utils.LTListener
import com.netphone.utils.ToastUtil
import com.storm.developapp.tools.AppManager
import com.storm.tool.base.BaseActivity
import io.reactivex.functions.Consumer


class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()
//        LightStatusBarUtils.setLightStatusBar(activity, true)
    }

    override fun onResume() {
        super.onResume()
//        LightStatusBarUtils.setLightStatusBar(activity, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        LTConfigure.getInstance().onDestory()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitSystem()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun initData() {
        binding.click = OnClick()
        registerEventBus()

        binding.tabSession.isChecked = true
        binding.viewpage.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                when (position) {
                    1 -> {
                        return FriendsFragment()
                    }
                    3 -> {
                        return GroupsFragment()
                    }
                    4 -> {
                        return SettingFragment()
                    }

                }
                return SessionFragment()
            }

            override fun getCount(): Int {
                return 5
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            getRxPermissions().shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(object : Consumer<Boolean> {
                override fun accept(t: Boolean?) {
                    if (!t!!) {
                        jump(PermissionDialog::class.java)
                    } else {
                        LTConfigure.getInstance().startLocationService()
                    }
                }
            })

        LTListener.newInstance().setOnReFreshListener()
        LTListener.newInstance().setOnBroadcastListener()
    }

    override fun initListener() {
        LTConfigure.getInstance().mOnErrorListener = object : OnErrorListener {
            override fun onOrderError() {

            }

            override fun onNotLogin() {
                activity.runOnUiThread {
                    ToastUtil.toasts(context.getResources().getString(R.string.not_login))
                }
            }

            override fun onCRCError() {

            }

            override fun onError() {

            }
        }
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.SQUEEZE_OFF_LINE -> {
                AlertView(context.resources.getString(R.string.warn), context.resources.getString(R.string.login_other), context.resources.getString(R.string.text_cancel),
                        arrayOf(context.resources.getString(R.string.text_sure)), null, activity, AlertView.Style.Alert,
                        OnItemClickListener { o, position ->
                            if (position == 0) {
                                AppManager.appManager.finishAllActivity()
                                var intent = Intent(MyApp.getContext(), LoginActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                MyApp.getContext().startActivity(intent)
                            }
                        }).show()
            }
        }
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {

    }


    inner class OnClick {
        open fun message(view: View) {
            binding.viewpage.setCurrentItem(0, false)
        }

        open fun friends(view: View) {
            binding.viewpage.setCurrentItem(1, false)
        }

        open fun call(view: View) {
            var currentGroupInfo = LTApi.getInstance().currentGroupInfo
            if (currentGroupInfo != null) {
                val bundle = Bundle()
                bundle.putSerializable("bean", currentGroupInfo)
                jump(GroupChatActivity::class.java, bundle)
            } else {
                toasts(context.resources.getString(R.string.You_re_not_in_the_group_yet))
                binding.viewpage.setCurrentItem(3, false)
                binding.tabGroup.isChecked = true
            }
        }

        open fun groups(view: View) {
            binding.viewpage.setCurrentItem(3, false)
        }

        open fun setting(view: View) {
            binding.viewpage.setCurrentItem(4, false)
        }
    }

    /**
     * 退出系统
     */
    private var exitTime: Long = 0

    private fun exitSystem() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(applicationContext, "再按一次返回键退出程序",
                    Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            AppManager.appManager.AppExit(context)
        }
    }
}
