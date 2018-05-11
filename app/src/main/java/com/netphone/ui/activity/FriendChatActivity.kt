package com.netphone.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.google.gson.Gson
import com.netphone.R
import com.netphone.adapter.FriendChatAdapter
import com.netphone.config.EventConfig
import com.netphone.databinding.ActivityChatFriendBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.FriendChatMsgBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.utils.LogUtil
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
            LogUtil.error("FriendChatActivity.kt", "39\tinitData()\n" + Gson().toJson(user));
            if (user.isOnLine.equals("0") || user.isOnLine.equals("false")) {
                toasts(context.getResources().getString(R.string.user_line_off));
                return@setOnClickListener;
            }
            if (user.userId.equals(LTApi.getInstance().currentInfo)) {
                toasts(context.getResources().getString(R.string.chat_with_u));
                return@setOnClickListener;
            }
            jump(FriendVoiceActivity::class.java, intent.extras)
        }

        binding.click = OnClick()

        friendChatAdapter = FriendChatAdapter(context, LTApi.getInstance().getFriendChatMessage(user.userId))
        binding.recycle.adapter = friendChatAdapter
        binding.recycle.layoutManager = LinearLayoutManager(context)

        if (friendChatAdapter.itemCount != 0)
            binding.recycle.smoothScrollToPosition(friendChatAdapter.itemCount - 1);
        LTApi.getInstance().joinFriendChat(user.userId)
        registerEventBus()
    }

    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
        binding.etContent.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                isShowKeyBoard = false
                binding.keyboard.setImageResource(R.mipmap.icon_jp)
//                binding.etContent.clearFocus();//失去焦点
            } else {
                binding.keyboard.setImageResource(R.mipmap.icon_jp2)
                isShowKeyBoard = true
//                binding.etContent.requestFocus();//获取焦点 光标出现
            }
        }
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.FRIEND_SEND_MSG -> {
                if (appBean.msg.equals(user.userId)) {
                    var friendChatMsgBean = appBean.data as FriendChatMsgBean
                    friendChatAdapter.addData(friendChatMsgBean)
                    LTApi.getInstance().joinFriendChat(user.userId)
                }
            }
        }
        if (friendChatAdapter.itemCount != 0)
            binding.recycle.smoothScrollToPosition(friendChatAdapter.itemCount - 1);
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun submit(view: View) {
            var toString = binding.etContent.text.toString()
            if (TextUtils.isEmpty(toString)) {
                toasts(context.resources.getString(R.string.message_not_null))
                return
            }
            LTApi.getInstance().sendFriendMessage(user.userId, toString)
            binding.etContent.setText("")
        }

        open fun keyboardShow(view: View) {
            if (isShowKeyBoard) {
                AppUtil.closeKeyboard(context)
                isShowKeyBoard = false
                binding.keyboard.setImageResource(R.mipmap.icon_jp)
                binding.etContent.clearFocus();//失去焦点
            } else {
                binding.keyboard.setImageResource(R.mipmap.icon_jp2)
                isShowKeyBoard = true
                AppUtil.openKeyboard(binding.etContent, context)
                binding.etContent.requestFocus();//获取焦点 光标出现
            }
        }
    }
}