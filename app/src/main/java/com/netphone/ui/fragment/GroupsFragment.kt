package com.netphone.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netphone.R
import com.netphone.adapter.GroupAdapter
import com.netphone.databinding.FragmentGroupsBinding
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.utils.SharedPreferenceUtil
import com.storm.tool.base.BaseFragment

/**
 * Created by XYSM on 2018/4/16.
 */

class GroupsFragment : BaseFragment<FragmentGroupsBinding>() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBinding(R.layout.fragment_groups, container)
        return mView
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
    }

    lateinit var groupAdapter: GroupAdapter
    private var currentId = "";

    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.groups)
        currentId = SharedPreferenceUtil.read(Constant.currentGroupId, "")
        if (TextUtils.isEmpty(currentId)) {
            binding.layCurrent.visibility = View.GONE
        } else {
            binding.layCurrent.visibility = View.VISIBLE
        }

        if (Constant.listBean != null && Constant.listBean.groupInfo != null) {
            groupAdapter = GroupAdapter(context, Constant.listBean.groupInfo)
            if (!TextUtils.isEmpty(currentId))
                groupAdapter.setCurrentId(currentId)
            binding.recycle.layoutManager = LinearLayoutManager(context)
            binding.recycle.adapter = groupAdapter
        }
    }

}
