package com.netphone.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.netphone.R
import com.netphone.adapter.GroupAdapter
import com.netphone.config.Constant
import com.netphone.config.EventConfig
import com.netphone.databinding.FragmentGroupsBinding
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.GroupInfoBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.utils.LogUtil
import com.netphone.ui.activity.GroupChatActivity
import com.netphone.utils.GlideCircleTransform
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
        when (appBean.code) {
            EventConfig.GROUP_DEL -> {
                if (appBean.data != null) {
                    var infoBean = appBean.data as GroupInfoBean
                    if (groupAdapter != null && infoBean != null) {
                        groupAdapter!!.del(infoBean.groupID)

                    } else {
                        currentMic = null;
                        if (currentGroup != null)
                            currentGroup!!.micer = null
                    }
                } else {
                    initData()
                }
            }

            EventConfig.LINE_STATE -> {
                var state = appBean.data as Int
                if (state == 0) {
                    groupAdapter = GroupAdapter(context, LTApi.getInstance().getAllGroup());
                    binding.recycle.adapter = groupAdapter;
                }
            }

            EventConfig.GROUP_REFRESH -> {
//                var allGroup = LTApi.getInstance().getAllGroup()
//                groupAdapter!!.setList(allGroup);
//                if (appBean.data != null) {
//                    var infoBean = appBean.data as GroupInfoBean
//                    if (groupAdapter != null && infoBean != null)
//                        groupAdapter!!.refresh(infoBean.groupID, infoBean.onLineCount)
//                    if (currentGroup != null && infoBean.micer != null && currentGroup!!.groupID.equals(infoBean.groupID)) {
//                        currentMic = LTApi.getInstance().getUserInfo(infoBean.micer.userId)
//                        currentGroup = infoBean;
//                    } else {
//                        currentMic = null;
//                        if (currentGroup != null)
//                            currentGroup!!.micer = null
//                    }
//                } else {
                initData()
//                }


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
        }
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
        binding.titleSearch.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                var key = binding.titleSearch.etSearch.text.toString()
                if (!TextUtils.isEmpty(key)) {
                    var group = LTApi.getInstance().searchGroup(key)
                    groupAdapter = GroupAdapter(context, group)
                    binding.recycle.layoutManager = LinearLayoutManager(context)
                    binding.recycle.adapter = groupAdapter
                } else {
                    initData()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    var groupAdapter: GroupAdapter? = null
    private var currentGroup: GroupInfoBean? = null;
    private var currentMic: UserInfoBean? = null;

    override fun initData() {
        if (!LTApi.getInstance().getIsOnline())
            return
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.groups)

        currentGroup = LTApi.getInstance().currentGroupInfo

        if (currentGroup != null)
            currentGroup!!.micer = currentMic

        registerEventBus()
        if (currentGroup == null) {
            binding.layCurrent.visibility = View.GONE
        } else {
            LogUtil.error("GroupsFragment.kt", "50\tinitData()\n" + currentGroup!!.groupID);
            binding.layCurrent.visibility = View.VISIBLE
            binding.tvCurrent.setText(currentGroup!!.groupName)
            Glide.with(context).load(TcpConfig.URL + currentGroup!!.headIcon).placeholder(R.mipmap.icon_qunzutouxiang).error(R.mipmap.icon_qunzutouxiang).transform(GlideCircleTransform(context)).into(binding.ivCurrent)

            binding.layCurrent.setOnClickListener {
                val bundle = Bundle()
                LogUtil.error("GroupsFragment.kt", "111\tinitData()\n" + Gson().toJson(currentGroup));
                bundle.putSerializable("bean", currentGroup)
                jump(GroupChatActivity::class.java, bundle)
            }

        }

        if (Constant.myGroupList == null) {
            Constant.myGroupList = arrayListOf()
        }
        var arrss: ArrayList<GroupInfoBean> = LTApi.getInstance().getAllGroup()

        if (currentGroup != null && arrss.size != 0) {
            for (i in 0 until arrss.size) {
                if (currentGroup!!.groupID.equals(arrss[i].groupID)) {
                    arrss.removeAt(i)
                    break
                }
            }
        }

        if (arrss != null && arrss.size != 0) {
            binding.catalog2.visibility = View.VISIBLE
        } else {
            binding.catalog2.visibility = View.GONE
        }

        groupAdapter = GroupAdapter(context, arrss)
        binding.recycle.layoutManager = LinearLayoutManager(context)
        binding.recycle.adapter = groupAdapter

//        LogUtil.error("GroupsFragment.kt", "86\tinitData()\n" + LtConstant.myGroupList.size);

//        var arrss: ArrayList<GroupInfoBean> =  LTApi.getInstance().getAllGroup()
//        if (currentGroup != null) {
//            if (arrss != null && arrss.size != 0) {
//                binding.catalog2.visibility = View.VISIBLE
//                for (i in 0 until arrss.size) {
//                    if (!TextUtils.isEmpty(currentGroup!!.groupID) && currentGroup!!.groupID.equals(arrss[i].groupID)) {
//                        continue
//                    }
//                    arrss.add(Constant.myGroupList[i])
//
//                }
//                groupAdapter = GroupAdapter(context,  arrss)
//
//                binding.recycle.layoutManager = LinearLayoutManager(context)
//                binding.recycle.adapter = groupAdapter
//            } else {
//                binding.catalog2.visibility = View.GONE
//            }
//        } else {
//            if (Constant.myGroupList != null && Constant.myGroupList.size != 0) {
//                binding.catalog2.visibility = View.VISIBLE
//            } else {
//                binding.catalog2.visibility = View.GONE
//            }
//            groupAdapter = GroupAdapter(context, Constant.myGroupList)
//
//            binding.recycle.layoutManager = LinearLayoutManager(context)
//            binding.recycle.adapter = groupAdapter
//        }

    }

}
