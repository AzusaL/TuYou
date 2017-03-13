package com.waibao.team.tuyou.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.JournalCommentDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.DateUtil;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Azusa on 2016/5/6.
 */
public class CommentRvAdapter extends RecyclerView.Adapter<CommentRvAdapter.ViewHolder> {

    private List<JournalCommentDto> datas;
    private Activity mActivity;
    private OnItemClickListener onItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public CommentRvAdapter(List<JournalCommentDto> datas, Activity activity) {
        this.datas = datas;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_comments, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(mActivity)
                .load(Config.Pic + datas.get(position).getImgUrl())
                .bitmapTransform(new CropCircleTransformation(mActivity))
                .into(holder.imgHead);

        holder.tvName.setText(datas.get(position).getNickname());

        holder.tvContent.setText(datas.get(position).getContent());

        holder.tvTime.setText(DateUtil.getStringbyDate(datas.get(position).getCommentDate(), DateUtil.dateFormatYMDHMS));

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

        public ImageView imgHead;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvContent;


        public ViewHolder(View itemView) {
            super(itemView);
            imgHead = (ImageView) itemView.findViewById(R.id.img_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_username);
            tvContent = (TextView) itemView.findViewById(R.id.tv_comment);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
