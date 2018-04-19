package com.netphone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netphone.R;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.utils.GlideCircleTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by XYSM on 2018/4/18.
 */

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<GroupChatMsgBean> mDatas;
    private LayoutInflater mLayoutInflater = null;
    private GlideCircleTransform mGlideCircleTransform;
    private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GroupChatAdapter(Context context, ArrayList<GroupChatMsgBean> datas) {
        mDatas = datas;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mGlideCircleTransform = new GlideCircleTransform(mContext);
    }

    public void addMsg(GroupChatMsgBean msgBean) {
        mDatas.add(0, msgBean);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_chat, parent, false);
        return new GroupChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GroupChatMsgBean bean = mDatas.get(position);
        if (!TextUtils.isEmpty(bean.getMsg()))
            holder.leftContent.setText(bean.getMsg());
        holder.time.setText(dateformat.format(bean.getDateTime()));
        if (bean.getUserInfoBean() != null) {
            holder.leftName.setText(bean.getUserInfoBean().getRealName());
            Glide.with(mContext).load(TcpConfig.URL + bean.getUserInfoBean().getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(holder.leftHead);
        }

        LogUtil.error("GroupChatAdapter", "57\tonBindViewHolder()\n" + new Gson().toJson(bean));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView leftName;
        private TextView leftContent;
        private ImageView leftHead;

        private TextView RightContent;
        private ImageView RightHead;

        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            time = itemView.findViewById(R.id.time);
            leftName = itemView.findViewById(R.id.left_name);
            leftContent = itemView.findViewById(R.id.left_content);
            leftHead = itemView.findViewById(R.id.left_head);


        }
    }
}
