package com.netphone.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netphone.R;
import com.netphone.netsdk.Tool.Constant;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.ui.activity.GroupChatActivity;
import com.netphone.utils.GlideCircleTransform;
import com.netphone.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XYSM on 2018/4/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater = null;
    private Context mContext;
    private List<GroupInfoBean> datas = new ArrayList<>();
    private String               id;
    private GlideCircleTransform mGlideCircleTransform;

    public GroupAdapter(Context mContext, List<GroupInfoBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
        mGlideCircleTransform = new GlideCircleTransform(mContext);
    }

    public void refresh(String groupId, int onLineCount) {
        LogUtil.error("GroupAdapter", "45\trefresh()\n" + new Gson().toJson(datas));
        LogUtil.error("GroupAdapter", "47\trefresh()\n" + "groupId:" + groupId);
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getGroupID().equals(groupId)) {
                datas.get(i).setOnLineCount(onLineCount);
            }
        }
        notifyDataSetChanged();
    }

    public void setList( List<GroupInfoBean> datas){
        if (datas!=null){
            datas.clear();
            datas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GroupInfoBean groupInfoBean = datas.get(position);
        holder.tvName.setText(groupInfoBean.getGroupName());
        holder.tvOnline.setText(groupInfoBean.getOnLineCount() + "/" + groupInfoBean.getAllCount());
        Glide.with(mContext).load(TcpConfig.URL + groupInfoBean.getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(holder.ivHead);

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constant.isOnline) {
                    ToastUtil.Companion.toasts(mContext.getResources().getString(R.string.already_line_off));
                    return;
                }
                Intent intent = new Intent(mContext, GroupChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", groupInfoBean);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView  tvName;
        private TextView  tvOnline;
        private ImageView ivHead;
        private View      view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvOnline = (TextView) view.findViewById(R.id.on_line_number);
            ivHead = (ImageView) view.findViewById(R.id.iv_head);
        }

        public View getView() {
            return view;
        }
    }
}
