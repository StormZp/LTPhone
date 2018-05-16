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
import com.netphone.netsdk.LTApi;
import com.netphone.netsdk.Tool.Constant;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.ReplyMsgBean;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.ui.activity.FriendChatActivity;
import com.netphone.utils.ChatTimeUtil;
import com.netphone.utils.GlideCircleTransform;
import com.netphone.utils.ToastUtil;

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
        View view = mLayoutInflater.inflate(R.layout.item_reply, parent, false);
        return new ReplyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final ReplyMsgBean user = list.get(position);

        if (user.getReceiver() == null) {
            user.setReceiver(LTApi.getInstance().getUserInfo(user.getReceiveID()));
        }
        LogUtil.error("ReplyAdapter", "68\tonBindViewHolder()\n" +new  Gson().toJson(user));
        if (user.getReceiver() != null) {
            viewHolder.name.setText(user.getReceiver().getRealName());
            viewHolder.content.setText(user.getLastMsg());
            if (user.getLastTime() != 0) {
                viewHolder.time.setText(ChatTimeUtil.getInterval(user.getLastTime()));
            }
            if (user.getUnread() != 0) {
                viewHolder.unread.setVisibility(View.VISIBLE);
                viewHolder.unread.setText(user.getUnread() + "");
            } else {
                viewHolder.unread.setVisibility(View.INVISIBLE);

            }
//            LogUtil.error("ReplyAdapter", "77\tonBindViewHolder()\n" + TcpConfig.URL + user.getReceiver().getHeadIcon());
            Glide.with(mContext).load(TcpConfig.URL + user.getReceiver().getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(viewHolder.head);

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Constant.isOnline) {
                        ToastUtil.Companion.toasts(mContext.getResources().getString(R.string.already_line_off));
                        return;
                    }
                    Intent intent = new Intent(mContext, FriendChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", user.getReceiver());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  name;
        TextView  content;
        TextView  time;
        TextView  unread;
        ImageView head;
        View      mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = (TextView) mView.findViewById(R.id.name);
            content = (TextView) mView.findViewById(R.id.content);
            time = (TextView) mView.findViewById(R.id.time);
            unread = (TextView) mView.findViewById(R.id.unread);
            head = (ImageView) mView.findViewById(R.id.head);
        }
    }
}
