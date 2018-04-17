package com.netphone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netphone.R;
import com.netphone.netsdk.bean.GroupInfoBean;

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

    public void setCurrentId(String id) {
        this.id = id;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupInfoBean groupInfoBean = datas.get(position);
        if (!TextUtils.isEmpty(id)&&id.equals(groupInfoBean.getGroupID())) {
            holder.getView().setVisibility(View.GONE);
        } else {
            holder.getView().setVisibility(View.VISIBLE);
        }
        holder.tvName.setText(groupInfoBean.getGroupName());

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
