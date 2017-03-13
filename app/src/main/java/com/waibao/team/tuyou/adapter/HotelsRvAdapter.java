package com.waibao.team.tuyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.dto.HotelMsgDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ImgsUtil;

import java.util.List;
import java.util.Random;

/**
 * Created by Azusa on 2016/5/6.
 */
public class HotelsRvAdapter extends RecyclerView.Adapter<HotelsRvAdapter.ViewHolder> {

    private List<HotelMsgDto> datas;
    private Fragment fragment;
    private OnItemClickListener onItemClickListener;

    public HotelsRvAdapter(List<HotelMsgDto> datas, Fragment fragment) {
        this.datas = datas;
        this.fragment = fragment;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_hotelrv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_hotelname.setText(datas.get(position).getName());
        holder.tv_phone.setText("电话:" + datas.get(position).getPhone());
        holder.tv_address.setText(datas.get(position).getAddress());
        holder.tv_value.setText("￥" + datas.get(position).getPrice() + "起");
        holder.tv_score.setText("评分：" + datas.get(position).getRating());

        Random random = new Random();
        Glide.with(fragment)
                .load(ImgsUtil.img[random.nextInt(ImgsUtil.img.length - 2)])
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

        public ImageView img;       //酒店图片
        public TextView tv_hotelname; //酒店名字
        public TextView tv_score;  //酒店评分
        public TextView tv_value;   //价格
        public TextView tv_phone;   //联系号码
        public TextView tv_address;    //位置

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_hotels);
            tv_hotelname = (TextView) itemView.findViewById(R.id.tv_hotelname);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            tv_value = (TextView) itemView.findViewById(R.id.tv_value);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_juli);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }
}
