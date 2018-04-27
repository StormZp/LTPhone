package com.netphone.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netphone.R;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.ui.activity.FriendChatActivity;
import com.netphone.utils.GlideCircleTransform;

import java.util.Collections;
import java.util.List;

/**
 * Created by XYSM on 2018/4/18.
 */

public class Friend2Adapter extends RecyclerView.Adapter<Friend2Adapter.ViewHolder> {
    private final GlideCircleTransform mGlideCircleTransform;
    private List<UserInfoBean> list = null;
    private Context mContext;
    private LayoutInflater mLayoutInflater = null;


    public Friend2Adapter(Context mContext, List<UserInfoBean> list) {
        this.mContext = mContext;
        mGlideCircleTransform = new GlideCircleTransform(mContext);
        this.list = list;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<UserInfoBean> list) {
        this.list.clear();
        this.list.addAll(list);
        Collections.sort(this.list); // 对list进行排序，需要让User实现Comparable接口重写compareTo方法
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_friends, parent, false);
        return new Friend2Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final UserInfoBean user = list.get(position);
        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();
//        LogUtil.error("Friend2Adapter", "52\tonBindViewHolder()\n" + new Gson().toJson(user));
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(catalog)) {
            viewHolder.catalog.setVisibility(View.VISIBLE);

        } else {
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.name.setText(this.list.get(position).getRealName());
        viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
        boolean aTrue = !TextUtils.isEmpty(user.getIsOnLine()) && (user.getIsOnLine().equals("0") || user.getIsOnLine().equals("true"));
//        LogUtil.error("Friend2Adapter", "74\tonBindViewHolder()\n" + user.getIsOnLine());
        viewHolder.online.setText(aTrue ? mContext.getResources().getString(R.string.off_line) : mContext.getResources().getString(R.string.onLine));
        viewHolder.online.setTextColor(aTrue ? mContext.getResources().getColor(R.color.text_gray) : mContext.getResources().getColor(R.color.text_black));
        viewHolder.name.setTextColor(aTrue ? mContext.getResources().getColor(R.color.text_gray) : mContext.getResources().getColor(R.color.text_black));
        Glide.with(mContext).load(TcpConfig.URL + user.getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(viewHolder.head);


        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
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

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }
}
