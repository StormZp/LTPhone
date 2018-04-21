package com.netphone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netphone.R;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created by XYSM on 2018/4/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final GlideCircleTransform mGlideCircleTransform;
    private List<UserInfoBean> list = null;
    private Context mContext;
    private LayoutInflater mLayoutInflater = null;


    public ImageAdapter(Context mContext, List<UserInfoBean> list) {
        this.mContext = mContext;
        mGlideCircleTransform = new GlideCircleTransform(mContext);
        this.list = list;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_image, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
    }



    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {
        TextView catalog;
        TextView name;
        TextView online;
        ImageView head;
        View mView;

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
