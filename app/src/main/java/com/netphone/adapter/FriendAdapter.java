package com.netphone.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netphone.R;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.ui.activity.FriendChatActivity;
import com.netphone.utils.GlideCircleTransform;

import java.util.List;

public class FriendAdapter extends BaseAdapter {

    private final GlideCircleTransform mGlideCircleTransform;
    private List<UserInfoBean> list = null;
    private Context mContext;


    public FriendAdapter(Context mContext, List<UserInfoBean> list) {
        this.mContext = mContext;
        mGlideCircleTransform = new GlideCircleTransform(mContext);
        this.list = list;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder;
        final UserInfoBean user = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_friends, null);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.catalog = (TextView) view.findViewById(R.id.catalog);
            viewHolder.online = (TextView) view.findViewById(R.id.online);
            viewHolder.head = (ImageView) view.findViewById(R.id.head);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取首字母作为目录catalog
        String catalog = list.get(position).getFirstLetter();

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(catalog)) {
            viewHolder.catalog.setVisibility(View.VISIBLE);

        } else {
            viewHolder.catalog.setVisibility(View.GONE);
        }

        viewHolder.name.setText(this.list.get(position).getRealName());
        viewHolder.catalog.setText(user.getFirstLetter().toUpperCase());
        viewHolder.online.setText(user.getIsOnLine() == 0 ? mContext.getResources().getString(R.string.off_line) : mContext.getResources().getString(R.string.onLine));
//        LogUtil.error("FriendAdapter", "62\tgetView()\n" + user.getHeadIcon());
        Glide.with(mContext).load(TcpConfig.URL + user.getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(viewHolder.head);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", user);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return view;

    }

    final static class ViewHolder {
        TextView catalog;
        TextView name;
        TextView online;
        ImageView head;
    }

    /**
     * 获取catalog首次出现位置
     */
    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }

}