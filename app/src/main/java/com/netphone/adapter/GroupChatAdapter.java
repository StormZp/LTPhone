package com.netphone.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netphone.R;
import com.netphone.netsdk.LTApi;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.utils.ChatTimeUtil;
import com.netphone.utils.GlideCircleTransform;

import java.util.ArrayList;

/**
 * Created by XYSM on 2018/4/18.
 */

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.ViewHolder> {
    private Context                     mContext;
    private ArrayList<GroupChatMsgBean> mDatas;
    private LayoutInflater mLayoutInflater = null;
    private GlideCircleTransform mGlideCircleTransform;

    private UserInfoBean mUserInfoBean;

    public GroupChatAdapter(Context context, ArrayList<GroupChatMsgBean> datas) {
        mDatas = datas;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mGlideCircleTransform = new GlideCircleTransform(mContext);
        mUserInfoBean = LTApi.getInstance().getCurrentInfo();
    }

    public void addMsg(GroupChatMsgBean msgBean) {
        mDatas.add( msgBean);
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

        if (bean.getDateTime() != null) {
            holder.time.setText(ChatTimeUtil.getInterval(bean.getDateTime()));

            if (bean.getFromUserId().equals(mUserInfoBean.getUserId())) {//此时为当前用户发送的信息
                holder.layLeft.setVisibility(View.GONE);
                holder.layRight.setVisibility(View.VISIBLE);
                if (bean.getUserInfoBean() != null) {
                    holder.RightContent.setText(bean.getMsg());
                    Glide.with(mContext).load(TcpConfig.URL + bean.getUserInfoBean().getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(holder.RightHead);
                }
            } else {//此时为其他用户发送的信息
                holder.layLeft.setVisibility(View.VISIBLE);
                holder.layRight.setVisibility(View.GONE);
                if (bean.getUserInfoBean() != null) {
                    holder.leftName.setText(bean.getUserInfoBean().getRealName());
                    holder.leftContent.setText(bean.getMsg());
                    Glide.with(mContext).load(TcpConfig.URL + bean.getUserInfoBean().getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(holder.leftHead);
                }
            }
        }else {
            holder.layLeft.setVisibility(View.GONE);
            holder.layRight.setVisibility(View.GONE);
            holder.time.setText(bean.getFromUserName());
        }


//        LogUtil.error("GroupChatAdapter", "57\tonBindViewHolder()\n" + new Gson().toJson(bean));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time;

        private RelativeLayout layRight;
        private RelativeLayout layLeft;

        private TextView  leftName;
        private TextView  leftContent;
        private ImageView leftHead;

        private TextView  RightContent;
        private ImageView RightHead;

        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            time = itemView.findViewById(R.id.time);
            leftName = itemView.findViewById(R.id.left_name);
            leftContent = itemView.findViewById(R.id.left_content);
            leftHead = itemView.findViewById(R.id.left_head);

            layLeft = itemView.findViewById(R.id.lay_left);
            layRight = itemView.findViewById(R.id.lay_right);

            RightContent = itemView.findViewById(R.id.right_content);
            RightHead = itemView.findViewById(R.id.right_head);

        }
    }
}
