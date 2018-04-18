package com.netphone.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.netphone.R
import com.netphone.adapter.GroupAdapter
import com.netphone.databinding.FragmentGroupsBinding
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.GroupInfoBean
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
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
    }

    lateinit var groupAdapter: GroupAdapter
    private var currentGroup: GroupInfoBean? = null;

    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.groups)

        currentGroup = LTConfigure.getInstance().currentGroup

        if (currentGroup == null) {
            binding.layCurrent.visibility = View.GONE
        } else {
        LogUtil.error("GroupsFragment.kt", "50\tinitData()\n" + currentGroup!!.groupID);
            binding.layCurrent.visibility = View.VISIBLE
            binding.tvCurrent.setText(currentGroup!!.groupName)
            Glide.with(context).load(TcpConfig.URL + currentGroup!!.headIcon).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivCurrent)

            binding.layCurrent.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("bean", currentGroup)
                jump(GroupChatActivity::class.java, bundle)
            }

        }

        if (currentGroup != null) {
            var arrss: ArrayList<GroupInfoBean> = arrayListOf<GroupInfoBean>()
            if (Constant.listBean != null && Constant.listBean.groupInfo != null) {
                for (i in 0 until Constant.listBean.groupInfo.size) {
                    if (!TextUtils.isEmpty(currentGroup!!.groupID) && currentGroup!!.groupID.equals(Constant.listBean.groupInfo[i].groupID)) {
                        continue
                    }
                    arrss.add(Constant.listBean.groupInfo[i])

                }
                groupAdapter = GroupAdapter(context, arrss)

                binding.recycle.layoutManager = LinearLayoutManager(context)
                binding.recycle.adapter = groupAdapter
            }
        } else {
            groupAdapter = GroupAdapter(context, Constant.listBean.groupInfo)

            binding.recycle.layoutManager = LinearLayoutManager(context)
            binding.recycle.adapter = groupAdapter
        }

    }

}
