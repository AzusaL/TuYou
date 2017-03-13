package com.waibao.team.tuyou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Azusa on 2016/6/9.
 */
public class SearchFriendsRvAdapter extends RecyclerView.Adapter<SearchFriendsRvAdapter.ViewHolder> {

    private List<User> datas;
    private OnItemClickListener onItemClickListener;

    public SearchFriendsRvAdapter(List<User> datas) {
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SearchFriendsRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_addfriends, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SearchFriendsRvAdapter.ViewHolder holder, final int position) {
        holder.name.setText(datas.get(position).getNickname());

        Glide.with(ConstanceUtils.CONTEXT)
                .load(Config.Pic + datas.get(position).getImgUrl())
                .bitmapTransform(new CropCircleTransformation(ConstanceUtils.CONTEXT))
                .into(holder.imageView);

        holder.gexing.setText(datas.get(position).getDescription());

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
