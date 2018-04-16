package com.netphone.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
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
import com.storm.tool.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_main)


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
            binding.viewpage.setCurrentItem(0,false)
        }

        open fun friends(view: View) {
            binding.viewpage.setCurrentItem(1,false)
        }

        open fun call(view: View) {
            toasts("通话")
        }

        open fun groups(view: View) {
            binding.viewpage.setCurrentItem(3,false)
        }

        open fun setting(view: View) {
            binding.viewpage.setCurrentItem(4,false)
        }
    }
}
