package com.netphone.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.Gson
import com.netphone.R
import com.netphone.adapter.ChatAdapter
import com.netphone.databinding.ActivityChatGroupBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.GroupInfoBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.listener.OnGetGroupMemberListener
import com.netphone.netsdk.listener.OnGroupComeInListener
import com.netphone.netsdk.listener.OnGroupStateListener
import com.netphone.netsdk.utils.LogUtil
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
        binding.recycle.adapter = ChatAdapter(context, groupChatMessage)
        binding.recycle.layoutManager = LinearLayoutManager(context)

        LTConfigure.getInstance().ltApi.joinGroup(groupInfo.groupID, object : OnGroupComeInListener {
            override fun onComeInSuccess() {

                LogUtil.error("GroupChatActivity.kt", "34\tonComeInSuccess()\n" + "加入成功");
            }

            override fun onComeInFail(code: Int, errorMessage: String?) {
                LogUtil.error("GroupChatActivity.kt", "38\tonComeInFail()\n" + "加入失败$errorMessage");

            }
        }, object : OnGetGroupMemberListener {
            override fun onGetMemberFail() {
                LogUtil.error("GroupChatActivity.kt", "42\tonGetMemberFail()\n" + "获取失败");

            }

            override fun onGetMemberSuccess(members: MutableList<UserInfoBean>?) {
                LogUtil.error("GroupChatActivity.kt", "47\tonGetMemberSuccess()\n" + "获取成功" + Gson().toJson(members));
            }
        }, object : OnGroupStateListener {
            override fun onMenberExit(member: UserInfoBean?) {
                LogUtil.error("GroupChatActivity.kt", "52\tonMenberExit()\n" + Gson().toJson(member));
            }

            override fun onMenberJoin(member: UserInfoBean?) {
                LogUtil.error("GroupChatActivity.kt", "56\tonMenberJoin()\n" + Gson().toJson(member));
            }

            override fun onMenberhaveMac(member: UserInfoBean?) {
                LogUtil.error("GroupChatActivity.kt", "60\tonMenberhaveMac()\n" + Gson().toJson(member));
            }

            override fun onMemberRelaxedMac(member: UserInfoBean?) {
                LogUtil.error("GroupChatActivity.kt", "64\tonMemberRelaxedMac()\n" + Gson().toJson(member));
            }

            override fun onSystemReLaxedMac() {
                LogUtil.error("GroupChatActivity.kt", "68\tonSystemReLaxedMac()\n");
            }

            override fun onGrabWheatSuccess() {
                LogUtil.error("GroupChatActivity.kt", "72\tonGrabWheatSuccess()\n");
            }

            override fun onGrabWheatFail(code: Int, error: String?) {
                LogUtil.error("GroupChatActivity.kt", "76\tonGrabWheatFail()\n" + error);
            }

            override fun onRelaxedMacSuccess() {
                LogUtil.error("GroupChatActivity.kt", "80\tonRelaxedMacSuccess()\n");
            }

            override fun onRelaxedMacFail(code: Int, error: String?) {
                LogUtil.error("GroupChatActivity.kt", "84\tonRelaxedMacFail()\n" + error);
            }
        })
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
//            binding.etContent.setText("")
        }
    }
}