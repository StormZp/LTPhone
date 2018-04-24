package com.netphone.ui.activity

import android.os.Bundle
import android.view.View
import com.githang.statusbar.StatusBarCompat

import com.netphone.R
import com.netphone.databinding.ActivityVoiceFriendBinding
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/24.
 */

class FriendVoiceActivity : BaseActivity<ActivityVoiceFriendBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_voice_friend)
    }

    override fun initData() {
        StatusBarCompat.setStatusBarColor(this, context.resources.getColor(R.color.bg_voice), false);
    }

    override fun initListener() {
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {

        }
    }
}
