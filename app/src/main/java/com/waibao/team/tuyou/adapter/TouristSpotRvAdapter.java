package com.waibao.team.tuyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.dto.TouristSpotDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by Azusa on 2016/5/6.
 */
public class TouristSpotRvAdapter extends RecyclerView.Adapter<TouristSpotRvAdapter.ViewHolder> {

    private List<TouristSpotDto> datas;
    private Fragment fragment;
    private OnItemClickListener onItemClickListener;

    public TouristSpotRvAdapter(List<TouristSpotDto> datas, Fragment fragment) {
        this.datas = datas;
        this.fragment = fragment;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_touristspot_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_hotelname.setText(datas.get(position).getTitle());
        holder.tv_address.setText(datas.get(position).getAddress());
        holder.tv_value.setText("￥"+datas.get(position).getPrice_min()+"起");
        holder.tv_score.setText(datas.get(position).getGrade()+" 景区");

        Glide.with(fragment)
                .load(datas.get(position).getImgurl())
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

        public ImageView img;       //景区图片
        public TextView tv_hotelname; //景区名字
        public TextView tv_score;  //景区评分
        public TextView tv_value;   //价格
        public TextView tv_address;    //位置

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_hotels);
            tv_hotelname = (TextView) itemView.findViewById(R.id.tv_hotelname);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            tv_value = (TextView) itemView.findViewById(R.id.tv_value);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }
}
