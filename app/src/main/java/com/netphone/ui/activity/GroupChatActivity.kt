package com.netphone.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.netphone.R
import com.netphone.adapter.GroupChatAdapter
import com.netphone.databinding.ActivityChatGroupBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.GroupInfoBean
import com.netphone.utils.LTListener
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/16.
 */
open class GroupChatActivity : BaseActivity<ActivityChatGroupBinding>() {
    private lateinit var groupInfo: GroupInfoBean
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
        binding.recycle.adapter = GroupChatAdapter(context, groupChatMessage)
        binding.recycle.layoutManager = LinearLayoutManager(context)

        LTListener.newInstance().joinGroupListener(groupInfo.groupID)
    }

    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
        binding.title.menuDate.setOnClickListener { jump(GroupInfoActivity::class.java, intent.extras) }
        binding.click = OnClick()
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {

        }

        open fun submit(view: View) {
            var toString = binding.etContent.text.toString()
            LTApi.newInstance().sendGroupMessage(groupInfo.groupID, groupInfo.groupName, toString)
            binding.etContent.setText("")
        }
    }
}