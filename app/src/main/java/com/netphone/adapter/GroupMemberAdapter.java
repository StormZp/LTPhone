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
import com.netphone.R;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.utils.GlideCircleTransform;

import java.util.List;

/**
 * Created  Storm
 * Time    2018/4/19 15:10
 * Message {群成员}
 */
public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {
    private Context mContext;
    private List<UserInfoBean> mDatas;
    private LayoutInflater mLayoutInflater = null;
    private GlideCircleTransform mGlideCircleTransform;

    public GroupMemberAdapter(Context context, List<UserInfoBean> datas) {
        mDatas = datas;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mGlideCircleTransform = new GlideCircleTransform(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_group_member, parent, false);
        return new GroupMemberAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserInfoBean user = mDatas.get(position);

        boolean aTrue = !TextUtils.isEmpty(user.getIsOnLine()) && (user.getIsOnLine().equals("1") || user.getIsOnLine().equals("true"));

        if (aTrue) {
            holder.online.setVisibility(View.VISIBLE);
            holder.offLine.setVisibility(View.GONE);
        } else {
            holder.online.setVisibility(View.GONE);
            holder.offLine.setVisibility(View.VISIBLE);
        }
        holder.name.setText(user.getRealName());
        Glide.with(mContext).load(TcpConfig.URL + user.getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(mGlideCircleTransform).into(holder.head);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView offLine;
        private ImageView head;
        private ImageView online;


        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            name = itemView.findViewById(R.id.name);
            offLine = itemView.findViewById(R.id.off_line);
            head = itemView.findViewById(R.id.head);
            online = itemView.findViewById(R.id.on_line);

        }
    }
}
