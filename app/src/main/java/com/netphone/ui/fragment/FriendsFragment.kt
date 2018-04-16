package com.netphone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netphone.R
import com.netphone.adapter.FriendAdapter
import com.netphone.databinding.FragmentFriendsBinding
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseFragment
import java.util.*

/**
 * Created by XYSM on 2018/4/16.
 */

class FriendsFragment : BaseFragment<FragmentFriendsBinding>() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBinding(R.layout.fragment_friends, container)
        return mView
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
//        binding.listView.setOnItemLongClickListener { parent, view, position, id ->
//
//            var bundle = Bundle()
//            bundle.putSerializable("bean", Constant.listBean.userInfo[position])
//            jump(FriendChatActivity::class.java)
//
//            true
//        }
    }

    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.fridends)

        if (Constant.listBean.userInfo != null) {
            Collections.sort(Constant.listBean.userInfo); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
            var friendAdapter = FriendAdapter(context, Constant.listBean.userInfo)
            binding.listView.adapter = friendAdapter


        }
    }

}
