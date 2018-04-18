package com.netphone.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.netphone.R
import com.netphone.databinding.ActivityMainBinding
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.listener.OnNetworkListener
import com.netphone.netsdk.utils.LogUtil
import com.netphone.ui.fragment.FriendsFragment
import com.netphone.ui.fragment.GroupsFragment
import com.netphone.ui.fragment.SessionFragment
import com.netphone.ui.fragment.SettingFragment
import com.netphone.utils.LightStatusBarUtils
import com.storm.developapp.tools.AppManager
import com.storm.tool.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        LightStatusBarUtils.setLightStatusBar(activity, true)
    }

    override fun onResume() {
        super.onResume()
        LightStatusBarUtils.setLightStatusBar(activity, true)
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
    }

    override fun initListener() {
        LTConfigure.getInstance().setOnNetworkListener(object : OnNetworkListener {
            override fun onServiceConnect() {
                toasts(context.resources.getString(R.string.net_connect) + " "+context.resources.getString(R.string.success))
            }

            override fun onNoNet() {
                LogUtil.error("MainActivity.kt", "onNoNet\n" + "这里有个文本")
            }

            override fun onWifiNet() {
                LogUtil.error("MainActivity.kt", "onWifiNet\n" + "这里有个文本")

            }

            override fun onMobileNet() {
                LogUtil.error("MainActivity.kt", "onMobileNet\n" + "这里有个文本")
            }
        })

    }

    override fun receiveEvent(appBean: AppBean<Any>) {
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
