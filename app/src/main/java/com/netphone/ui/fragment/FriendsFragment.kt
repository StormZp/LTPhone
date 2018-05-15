package com.netphone.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netphone.R
import com.netphone.adapter.Friend2Adapter
import com.netphone.config.Constant
import com.netphone.config.EventConfig
import com.netphone.databinding.FragmentFriendsBinding
import com.netphone.netsdk.LTApi
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
        when (appBean.code) {
            EventConfig.REFRESH_FRIEND -> {
                friend2Adapter.setList(Constant.myFriendList)
                friend2Adapter.setOnline(true)

            }
            EventConfig.LINE_STATE -> {
                var state = appBean.data as Int
                friend2Adapter.setList(Constant.myFriendList)
                if (state == 0) {
                    friend2Adapter.setOnline(true)
                } else {
                    friend2Adapter.setOnline(false)
                }

            }
            EventConfig.BROADCAST_STATE -> {
                var state = appBean.data as String?
                if (!TextUtils.isEmpty(state)){
                    binding.tvHint.setText(state)
                    binding.tvHint.visibility = View.VISIBLE
                }else{
                    binding.tvHint.visibility = View.GONE
                }

            }
        }
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
        binding.sideBar.setOnStrSelectCallBack { index, selectStr ->
            for (i in 0 until Constant.myFriendList.size) {
                if (selectStr.equals(Constant.myFriendList.get(i).getFirstLetter())) {
                    smoothMoveToPosition(binding.listView, i);// 选择到首字母出现的位置
                    break
                }
            }
        }

        binding.listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (mShouldScroll) {
                    mShouldScroll = false;
                    smoothMoveToPosition(binding.listView, mToPosition);
                }
            }
        });

        binding.titleSearch.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var key = binding.titleSearch.etSearch.text.toString()
                if (!TextUtils.isEmpty(key)) {
                    var friends = LTApi.getInstance().SearchFriend(key)
//                    friend2Adapter.setDatas(searchSession)
                    friend2Adapter = Friend2Adapter(context, friends)
                    mLinearLayoutManager = LinearLayoutManager(context)
                    binding.listView.layoutManager = mLinearLayoutManager
                    binding.listView.adapter = friend2Adapter
                } else {
                    friend2Adapter = Friend2Adapter(context, Constant.myFriendList)
                    mLinearLayoutManager = LinearLayoutManager(context)
                    binding.listView.layoutManager = mLinearLayoutManager
                    binding.listView.adapter = friend2Adapter
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var friend2Adapter: Friend2Adapter
    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.fridends)
        registerEventBus()

        if (Constant.myFriendList == null) {
            Constant.myFriendList = arrayListOf();
        }

        if (Constant.myFriendList != null) {
//            LogUtil.error("FriendsFragment.kt", "83\tinitData()\n" + Constant.myGroupList.size);
            Collections.sort(Constant.myFriendList); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
            friend2Adapter = Friend2Adapter(context, Constant.myFriendList)
            mLinearLayoutManager = LinearLayoutManager(context)
            binding.listView.layoutManager = mLinearLayoutManager
            binding.listView.adapter = friend2Adapter
        }
    }


    /** 目标项是否在最后一个可见项之后 */
    private var mShouldScroll: Boolean = false
    /** 记录目标项位置 */
    private var mToPosition: Int = 0

    /**
     * 滑动到指定位置
     * @param mRecyclerView
     * @param position
     */
    private fun smoothMoveToPosition(mRecyclerView: RecyclerView, position: Int) {
        // 第一个可见位置
        val firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0))
        // 最后一个可见位置
        val lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.childCount - 1))

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position)
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            val movePosition = position - firstItem
            if (movePosition >= 0 && movePosition < mRecyclerView.childCount) {
                val top = mRecyclerView.getChildAt(movePosition).top
                mRecyclerView.smoothScrollBy(0, top)
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position)
            mToPosition = position
            mShouldScroll = true
        }
    }


}
