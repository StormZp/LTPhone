package com.netphone.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.netphone.R
import com.netphone.adapter.FriendChatAdapter
import com.netphone.config.EventConfig
import com.netphone.databinding.ActivityChatFriendBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.FriendChatMsgBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.utils.AppUtil
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/16.
 */
open class FriendChatActivity : BaseActivity<ActivityChatFriendBinding>() {

    private var isShowKeyBoard = false
    private lateinit var user: UserInfoBean
    private lateinit var friendChatAdapter: FriendChatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_chat_friend)
    }

    override fun initData() {
        user = intent.extras.getSerializable("bean") as UserInfoBean
        binding.title.title.text = user.realName
        binding.title.menuDate.setImageResource(R.mipmap.icon_tel)
        binding.title.menuDate.visibility = View.VISIBLE
        binding.title.menuDate.setOnClickListener {
            jump(FriendVoiceActivity::class.java, intent.extras)
        }

        binding.click = OnClick()

        friendChatAdapter = FriendChatAdapter(context, LTApi.newInstance().getFriendChatMessage(user.userId))
        binding.recycle.adapter = friendChatAdapter
        binding.recycle.layoutManager = LinearLayoutManager(context)

        LTApi.newInstance().joinFriendChat(user.userId)
        registerEventBus()
    }

    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.FRIEND_SEND_MSG -> {
                if (appBean.msg.equals(user.userId)) {
                    var friendChatMsgBean = appBean.data as FriendChatMsgBean
                    friendChatAdapter.addData(friendChatMsgBean)
                }
            }
        }
        binding.recycle.smoothScrollToPosition(friendChatAdapter.itemCount - 1);
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun submit(view: View) {
            var toString = binding.etContent.text.toString()
            LTApi.newInstance().sendFriendMessage(user.userId, toString)
            binding.etContent.setText("")
        }

        open fun keyboardShow(view: View) {
            if (isShowKeyBoard) {
                AppUtil.closeKeyboard(context)
                isShowKeyBoard = false
                binding.keyboard.setImageResource(R.mipmap.icon_jp)
            } else {
                binding.keyboard.setImageResource(R.mipmap.icon_jp2)
                isShowKeyBoard = true
                AppUtil.openKeyboard(binding.etContent, context)
            }
        }
    }
}