package com.netphone.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netphone.R
import com.netphone.adapter.ReplyAdapter
import com.netphone.config.EventConfig
import com.netphone.databinding.FragmentSessionBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseFragment

/**
 * Created by XYSM on 2018/4/16.
 */

class SessionFragment : BaseFragment<FragmentSessionBinding>() {
    private lateinit var adapter: ReplyAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBinding(R.layout.fragment_session, container)
        return mView
    }

    override fun onResume() {
        super.onResume()
        adapter.setDatas(LTApi.newInstance().getSessionList())
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.FRIEND_SEND_MSG -> {
                adapter.setDatas(LTApi.newInstance().getSessionList())
            }
        }
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
    }

    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.message)
        adapter = ReplyAdapter(context, LTApi.newInstance().getSessionList())
        binding.recycle.adapter = adapter
        binding.recycle.layoutManager = LinearLayoutManager(context)

        registerEventBus()
    }
}
