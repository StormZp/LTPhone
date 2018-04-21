package com.netphone.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netphone.R;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.ImageBean;
import com.netphone.ui.activity.BigImageActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by XYSM on 2018/4/18.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final SimpleDateFormat mDateFormat;
    private List<ImageBean> list = null;
    private Context mContext;
    private LayoutInflater mLayoutInflater = null;


    public ImageAdapter(Context mContext, List<ImageBean> list) {
        this.mContext = mContext;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_image, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final ImageBean image = list.get(position);

        Glide.with(mContext).load(TcpConfig.URL + image.getResourceHref()).into(viewHolder.head);
        viewHolder.name.setText(image.getResourceName());
        if (image.getFormUser() != null)
            viewHolder.from.setText(image.getFormUser().getRealName());
        viewHolder.time.setText(mDateFormat.format(image.getDate()));
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", image);
                mContext.startActivity(new Intent(mContext, BigImageActivity.class).putExtras(bundle));
            }
        });
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    final static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  from;
        TextView  name;
        TextView  time;
        ImageView head;
        CheckBox  edit;
        View      mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = (TextView) mView.findViewById(R.id.name);
            edit = mView.findViewById(R.id.edit);
            from = (TextView) mView.findViewById(R.id.from);
            time = (TextView) mView.findViewById(R.id.time);
            head = (ImageView) mView.findViewById(R.id.head);
        }
    }
}
