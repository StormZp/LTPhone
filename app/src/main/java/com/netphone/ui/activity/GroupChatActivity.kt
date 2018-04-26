package com.netphone.ui.activity

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import com.netphone.R
import com.netphone.adapter.GroupChatAdapter
import com.netphone.config.EventConfig
import com.netphone.databinding.ActivityChatGroupBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.GroupChatMsgBean
import com.netphone.netsdk.bean.GroupInfoBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.utils.LogUtil
import com.netphone.utils.AppUtil
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

    private var userInfoBean = LTApi.newInstance().currentInfo
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
//        groupChatMessage = GroupUtil.getSortReverseList(groupChatMessage)
        adapter = GroupChatAdapter(context, groupChatMessage)
        binding.recycle.adapter = adapter
        binding.recycle.layoutManager = LinearLayoutManager(context)
        if (adapter.itemCount != 0)
            binding.recycle.smoothScrollToPosition(adapter.itemCount - 1);
        LTListener.newInstance().joinGroupListener(groupInfo.groupID)
        setSpeakerphoneOn(true,activity,getSystemService(Context.AUDIO_SERVICE) as AudioManager)
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
//                    binding.keyboard.setImageResource(R.mipmap.icon_jp)
                }
            }
        }
        binding.sendVoice.setOnTouchListener { v, event ->
//            LogUtil.error("GroupChatActivity.kt", "74\tinitListener()\n" + event.action);
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    LTApi.newInstance().stopGroupVoice()

                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_DOWN -> {
                    LTApi.newInstance().sendGroupVoice()
                }


                else -> {
                }
            }
            true

        }
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.RECEIVER_WORD_GROUP -> {//文字消息
                adapter.addMsg(appBean.data as GroupChatMsgBean)
            }
            EventConfig.COME_IN_SUCCESS -> {
                var groupChatMsgBean = GroupChatMsgBean()
                groupChatMsgBean.fromUserName = context.resources.getString(R.string.add_seccess)
                adapter.addMsg(groupChatMsgBean)
            }
            EventConfig.COME_IN_FAIL -> {
                toasts(context.resources.getString(R.string.add_fail))
                finish()
            }
            EventConfig.GET_MEMBER_FAIL -> {
            }
            EventConfig.GET_MEMBER_SUCCESS -> {

            }
            EventConfig.MENBER_EXIT -> {
                var groupChatMsgBean = GroupChatMsgBean()
                groupChatMsgBean.fromUserName = (appBean.data as UserInfoBean).realName + context.resources.getString(R.string.exit_group)
                adapter.addMsg(groupChatMsgBean)
            }
            EventConfig.MENBER_JOIN -> {
                var userInfoBean1 = appBean.data as UserInfoBean
                if (!userInfoBean.userId.equals(userInfoBean1.userId)) {
                    var groupChatMsgBean = GroupChatMsgBean()
                    groupChatMsgBean.fromUserName = userInfoBean1.realName + context.resources.getString(R.string.add_group)
                    adapter.addMsg(groupChatMsgBean)
                }
            }
            EventConfig.MENBER_HAVE_MAC -> {
                var groupChatMsgBean = GroupChatMsgBean()
                groupChatMsgBean.fromUserName = (appBean.data as UserInfoBean).realName + context.resources.getString(R.string.have_mic)
                adapter.addMsg(groupChatMsgBean)
            }
            EventConfig.MEMBER_RELAXE_DMAC -> {
                var groupChatMsgBean = GroupChatMsgBean()
                groupChatMsgBean.fromUserName = (appBean.data as UserInfoBean).realName + context.resources.getString(R.string.have_relase_mic)
                adapter.addMsg(groupChatMsgBean)
            }
            EventConfig.SYSTEM_RELAXED_MAC -> {
                var groupChatMsgBean = GroupChatMsgBean()
                groupChatMsgBean.fromUserName = context.resources.getString(R.string.system_relase_mic)
                adapter.addMsg(groupChatMsgBean)
            }
            EventConfig.GRAB_WHEAT_SUCCESS -> {
                var groupChatMsgBean = GroupChatMsgBean()
                groupChatMsgBean.fromUserName = context.resources.getString(R.string.preemption) + context.resources.getString(R.string.success)
                adapter.addMsg(groupChatMsgBean)
            }
        }
        if (adapter.itemCount != 0)
            binding.recycle.smoothScrollToPosition(adapter.itemCount - 1);
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {

        }

        open fun submit(view: View) {
            var toString = binding.etContent.text.toString()
            if (TextUtils.isEmpty(toString)) {
                toasts(context.resources.getString(R.string.message_not_null))
                return
            }
            LTApi.newInstance().sendGroupMessage(groupInfo.groupID, toString)
            binding.etContent.setText("")
        }


        open fun showEdit(view: View) {
            binding.layEdit.visibility = View.VISIBLE
            binding.layVoice.visibility = View.GONE

//            binding.keyboard.setImageResource(R.mipmap.icon_jp2)
            isShowKeyBoard = true
//            AppUtil.openKeyboard(binding.etContent, context)
        }

        open fun showVoice(view: View) {
            binding.layEdit.visibility = View.GONE
            binding.layVoice.visibility = View.VISIBLE

            AppUtil.closeKeyboard(context)
            isShowKeyBoard = false
//            binding.keyboard.setImageResource(R.mipmap.icon_jp)
        }
    }

    open fun setSpeakerphoneOn(on: Boolean, activity: Activity, audioManager: AudioManager) {
        if (on) {
            // 为true打开喇叭扩音器；为false关闭喇叭扩音器.
            audioManager.setSpeakerphoneOn(true)
            // 添加的代码，恢复系统声音设置
            audioManager.setMode(AudioManager.STREAM_SYSTEM)
            activity.volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
            LogUtil.error("true打开喇叭扩音器")
        } else {
            audioManager.setSpeakerphoneOn(false)//关闭扬声器
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL)
            activity.volumeControlStream = AudioManager.STREAM_VOICE_CALL
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION)
            LogUtil.error("关闭扬声器")
        }
    }
}