package com.netphone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netphone.R
import com.netphone.databinding.FragmentGroupsBinding
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseFragment

/**
 * Created by XYSM on 2018/4/16.
 */

class GroupsFragment : BaseFragment<FragmentGroupsBinding>() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBinding(R.layout.fragment_groups, container)
        return mView
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
    }

    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.groups)
    }

}
