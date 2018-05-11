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
            EventConfig.GROUP_REFRESH -> {
                if (appBean.data != null) {
                    var infoBean = appBean.data as GroupInfoBean
                    if (groupAdapter != null && infoBean != null)
                        groupAdapter!!.refresh(infoBean.groupID, infoBean.onLineCount)
                    if (currentGroup != null && currentGroup!!.groupID.equals(infoBean.groupID)) {
                        currentMic = LTApi.getInstance().getUserInfo(infoBean.micer.userId)
                        currentGroup = infoBean;
                    } else {
                        currentMic = null;
                        currentGroup!!.micer = null
                    }
                } else {
                    initData()
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
                    var group = LTApi.getInstance().SearchGroup(key)
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
        if (!com.netphone.netsdk.Tool.Constant.isOnline)
            return
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.groups)

        currentGroup = LTApi.getInstance().currentGroupInfo

        currentGroup!!.micer = currentMic

        registerEventBus()
        if (currentGroup == null) {
            binding.layCurrent.visibility = View.GONE
        } else {
            LogUtil.error("GroupsFragment.kt", "50\tinitData()\n" + currentGroup!!.groupID);
            binding.layCurrent.visibility = View.VISIBLE
            binding.tvCurrent.setText(currentGroup!!.groupName)
            Glide.with(context).load(TcpConfig.URL + currentGroup!!.headIcon).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivCurrent)

            binding.layCurrent.setOnClickListener {
                val bundle = Bundle()
                LogUtil.error("GroupsFragment.kt", "111\tinitData()\n" + Gson().toJson(currentGroup));
                bundle.putSerializable("bean", currentGroup)
                jump(GroupChatActivity::class.java, bundle)
            }

        }

        if (Constant.myGroupList == null) {
            Constant.myGroupList = arrayListOf();
        }
//        LogUtil.error("GroupsFragment.kt", "86\tinitData()\n" + Constant.myGroupList.size);

        if (currentGroup != null) {
            var arrss: ArrayList<GroupInfoBean> = arrayListOf<GroupInfoBean>()
            if (Constant.myGroupList != null && Constant.myGroupList.size != 0) {
                binding.catalog2.visibility = View.VISIBLE
                for (i in 0 until Constant.myGroupList.size) {
                    if (!TextUtils.isEmpty(currentGroup!!.groupID) && currentGroup!!.groupID.equals(Constant.myGroupList[i].groupID)) {
                        continue
                    }
                    arrss.add(Constant.myGroupList[i])

                }
                groupAdapter = GroupAdapter(context, arrss)

                binding.recycle.layoutManager = LinearLayoutManager(context)
                binding.recycle.adapter = groupAdapter
            } else {
                binding.catalog2.visibility = View.GONE
            }
        } else {
            if (Constant.myGroupList != null && Constant.myGroupList.size != 0) {
                binding.catalog2.visibility = View.VISIBLE
            } else {
                binding.catalog2.visibility = View.GONE
            }
            groupAdapter = GroupAdapter(context, Constant.myGroupList)

            binding.recycle.layoutManager = LinearLayoutManager(context)
            binding.recycle.adapter = groupAdapter
        }

    }

}
