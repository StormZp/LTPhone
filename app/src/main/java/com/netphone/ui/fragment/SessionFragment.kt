package com.netphone.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.netphone.R
import com.netphone.adapter.ReplyAdapter
import com.netphone.config.EventConfig
import com.netphone.databinding.FragmentSessionBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.utils.LogUtil
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
        var sessionList = LTApi.getInstance().getSessionList()
        LogUtil.error("SessionFragment.kt", "33\tonResume()\n" + Gson().toJson(sessionList));
        adapter.setDatas(sessionList)
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
        when (appBean.code) {
            EventConfig.FRIEND_SEND_MSG -> {
                adapter.setDatas(LTApi.getInstance().getSessionList())
            }
            EventConfig.BROADCAST_STATE -> {
                var state = appBean.data as String?
                if (!TextUtils.isEmpty(state)) {
                    binding.tvHint.setText(state)
                    binding.tvHint.visibility = View.VISIBLE
                } else {
                    binding.tvHint.visibility = View.GONE
                }

            }
            EventConfig.REFRESH_FRIEND -> {
                if (adapter == null) {
                    adapter = ReplyAdapter(context, LTApi.getInstance().getSessionList())
                    binding.recycle.adapter = adapter
                    binding.recycle.layoutManager = LinearLayoutManager(context)
                }
                adapter.setDatas( LTApi.getInstance().getSessionList())
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
        binding.titleSearch.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var key = binding.titleSearch.etSearch.text.toString()
                if (!TextUtils.isEmpty(key)) {
                    var searchSession = LTApi.getInstance().SearchSession(key)
                    adapter.setDatas(searchSession)
                } else {
                    adapter.setDatas(LTApi.getInstance().getSessionList())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.message)
        adapter = ReplyAdapter(context, LTApi.getInstance().getSessionList())
        binding.recycle.adapter = adapter
        binding.recycle.layoutManager = LinearLayoutManager(context)

        registerEventBus()
    }
}
