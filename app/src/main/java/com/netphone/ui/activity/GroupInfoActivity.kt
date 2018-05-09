package com.netphone.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.netphone.R
import com.netphone.adapter.GroupMemberAdapter
import com.netphone.config.Constant
import com.netphone.databinding.ActivityInfoGroupBinding
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.GroupInfoBean
import com.netphone.utils.GlideCircleTransform
import com.storm.tool.base.BaseActivity

/**
 * Created by XYSM on 2018/4/16.
 */
open class GroupInfoActivity : BaseActivity<ActivityInfoGroupBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_info_group)
    }

    override fun initData() {
        var userInfoBean = intent.extras.getSerializable("bean") as GroupInfoBean
        binding.title.title.text = context.resources.getString(R.string.group_info)
        binding.name.text = userInfoBean.groupName
        Glide.with(context).load(TcpConfig.URL + userInfoBean.headIcon).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.head)

        if (Constant.groupsMemberInfo != null) {
            binding.number.text = context.resources.getString(R.string.group_users) + ": "+Constant.groupsMemberInfo.size
            var friend2Adapter = GroupMemberAdapter(context,Constant.myFriendList)
            binding.recycle.layoutManager = LinearLayoutManager(context)
            binding.recycle.adapter = friend2Adapter
        }

    }

    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
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