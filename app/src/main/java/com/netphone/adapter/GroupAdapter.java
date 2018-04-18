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

import com.netphone.R;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.ui.activity.GroupChatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XYSM on 2018/4/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private LayoutInflater mLayoutInflater = null;
    private Context mContext;
    private List<GroupInfoBean> datas = new ArrayList<>();
    private String id;

    public GroupAdapter(Context mContext, List<GroupInfoBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
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

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivHead;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvName = (TextView) view.findViewById(R.id.tv_name);
            ivHead = (ImageView) view.findViewById(R.id.iv_head);
        }

        public View getView() {
            return view;
        }
    }
}
