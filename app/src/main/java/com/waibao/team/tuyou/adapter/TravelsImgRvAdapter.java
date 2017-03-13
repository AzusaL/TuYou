package com.waibao.team.tuyou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.List;

/**
 * Created by Azusa on 2016/5/6.
 */
public class TravelsImgRvAdapter extends RecyclerView.Adapter<TravelsImgRvAdapter.ViewHolder> {

    private List<String> datas;
    private int widthType;
    private OnItemClickListener onItemClickListener;

    public TravelsImgRvAdapter(List<String> datas, int widthType) {
        this.datas = datas;
        this.widthType = widthType;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (widthType == 1) {
            view = View.inflate(parent.getContext(), R.layout.item_travelsimg, null);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_travelsimg_small, null);
        }
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(ConstanceUtils.CONTEXT)
                .load(Config.Pic + datas.get(position))
                .into(holder.img);

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

        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
