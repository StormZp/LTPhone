package com.netphone.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.netphone.R
import com.netphone.adapter.GroupChatAdapter
import com.netphone.config.EventConfig
import com.netphone.databinding.ActivityChatGroupBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.GroupChatMsgBean
import com.netphone.netsdk.bean.GroupInfoBean
import com.netphone.utils.AppUtil
import com.netphone.utils.GroupUtil
import com.netphone.utils.LTListener
import com.netphone.view.InputMethodLayout
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/16.
 */
open class GroupChatActivity : BaseActivity<ActivityChatGroupBinding>() {
    private lateinit var groupInfo: GroupInfoBean
    private lateinit var adapter: GroupChatAdapter;
    private var isShowKeyBoard = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_chat_group)
    }

    override fun initData() {
        groupInfo = intent.extras.getSerializable("bean") as GroupInfoBean
        binding.title.title.text = groupInfo.groupName
        binding.title.menuDate.visibility = View.VISIBLE
        binding.title.menuDate.setImageResource(R.mipmap.icon_qz)


        var groupChatMessage = LTApi.newInstance().getGroupChatMessage(groupInfo.groupID)
        groupChatMessage = GroupUtil.getSortReverseList(groupChatMessage)
        adapter = GroupChatAdapter(context, groupChatMessage)
        binding.recycle.adapter = adapter
        binding.recycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)

        LTListener.newInstance().joinGroupListener(groupInfo.groupID)
    }

    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
        binding.title.menuDate.setOnClickListener { jump(GroupInfoActivity::class.java, intent.extras) }
        binding.click = OnClick()
        registerEventBus()
        binding.inputLay.setOnkeyboarddStateListener {
            when (it.toByte()) {
                InputMethodLayout.KEYBOARD_STATE_SHOW -> {
                    binding.keyboard.setImageResource(R.mipmap.icon_jp2)
                    isShowKeyBoard = true
                    AppUtil.openKeyboard(binding.etContent, context)
                }
                InputMethodLayout.KEYBOARD_STATE_HIDE -> {
                    AppUtil.closeKeyboard(context)
                    isShowKeyBoard = false
                    binding.keyboard.setImageResource(R.mipmap.icon_jp)
                }
            }
        }
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.RECEIVER_WORD_GROUP -> {//文字消息
                adapter.addMsg(appBean.data as GroupChatMsgBean)
            }
        }
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {

        }

        open fun submit(view: View) {
            var toString = binding.etContent.text.toString()
            LTApi.newInstance().sendGroupMessage(groupInfo.groupID, toString)
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