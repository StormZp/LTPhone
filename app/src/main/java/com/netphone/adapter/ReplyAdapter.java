package com.netphone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netphone.R;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.ReplyMsgBean;
import com.netphone.utils.GlideCircleTransform;

import java.util.ArrayList;

/**
 * Created by XYSM on 2018/4/18.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
    private final GlideCircleTransform mGlideCircleTransform;
    private ArrayList<ReplyMsgBean> list = null;
    private Context mContext;
    private LayoutInflater mLayoutInflater = null;


    public ReplyAdapter(Context mContext, ArrayList<ReplyMsgBean> list) {
        this.mContext = mContext;
        mGlideCircleTransform = new GlideCircleTransform(mContext);
        this.list = list;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void addData(ReplyMsgBean bean) {
        list.add(bean);
        notifyDataSetChanged();
    }

    public void setDatas(ArrayList<ReplyMsgBean> datas) {
        list.clear();
        list.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_friends, parent, false);
        return new ReplyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final ReplyMsgBean user = list.get(position);

        if (user.getReceiver() != null) {
            viewHolder.name.setText(user.getReceiver().getRealName());
            viewHolder.online.setText(user.getLastMsg() + "----" + user.getUnread());
            Glide.with(mContext).load(TcpConfig.URL + user.getReceiver().getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(viewHolder.head);
        }

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  catalog;
        TextView  name;
        TextView  online;
        ImageView head;
        View      mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = (TextView) mView.findViewById(R.id.name);
            catalog = (TextView) mView.findViewById(R.id.catalog);
            online = (TextView) mView.findViewById(R.id.online);
            head = (ImageView) mView.findViewById(R.id.head);
        }
    }
}
