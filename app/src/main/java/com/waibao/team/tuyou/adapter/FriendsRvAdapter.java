package com.waibao.team.tuyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.FriendDto;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.listener.OnItemClickListener;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Delete_exe on 2016/5/15.
 */
public class FriendsRvAdapter extends RecyclerView.Adapter<FriendsRvAdapter.ViewHolder> {

    private Context context;
    private List<FriendDto> datas;
    private OnItemClickListener onItemClickListener;

    public FriendsRvAdapter(List<FriendDto> datas) {
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FriendsRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.item_friends, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FriendsRvAdapter.ViewHolder holder, final int position) {
//        JMessageClient.getUserInfo(datas.get(position).getI, new GetUserInfoCallback() {
//            @Override
//            public void gotResult(int i, String s, UserInfo userInfo) {
        holder.name.setText(datas.get(position).getNickname() + "(" + datas.get(position).getLoginName() + ")");
//            }
//        });

        Glide.with(context).load(Config.Pic + datas.get(position).getImgUrl())
                .bitmapTransform(new CropCircleTransformation(context)).into(holder.imageView);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView gexing;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.friends_name);
            gexing = (TextView) itemView.findViewById(R.id.gexing);
            imageView = (ImageView) itemView.findViewById(R.id.friends_head_img);
        }
    }
}
