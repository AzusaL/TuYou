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
import com.waibao.team.tuyou.dto.SeatInfosBean;
import com.waibao.team.tuyou.dto.TrainListBean;
import com.waibao.team.tuyou.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by Azusa on 2016/5/6.
 */
public class TrainsMsgRvAdapter extends RecyclerView.Adapter<TrainsMsgRvAdapter.ViewHolder> {

    private List<TrainListBean> datas;
    private OnItemClickListener onItemClickListener;

    public TrainsMsgRvAdapter(List<TrainListBean> datas) {
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_trains_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_trainnum.setText(datas.get(position).getTrainNo());
        holder.tv_startime.setText(datas.get(position).getStartTime());
        holder.tv_endtime.setText(datas.get(position).getEndTime());
        holder.tv_duration.setText(datas.get(position).getDuration());
        holder.tv_staraddress.setText(datas.get(position).getFrom());
        holder.tv_endaddress.setText(datas.get(position).getTo());

        List<SeatInfosBean> seatInfos = datas.get(position).getSeatInfos();
        holder.tv_price.setText("￥"+seatInfos.get(0).getSeatPrice());
        holder.tv_set.setText(seatInfos.get(0).getSeat());
        if (seatInfos.get(0).getRemainNum() > 0) {
            holder.tv_remainnum.setText(seatInfos.get(0).getRemainNum() + "张");
        } else {
            holder.tv_remainnum.setText("无票");
        }

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

        public TextView tv_trainnum; //车次
        public TextView tv_startime;  //发车时间
        public TextView tv_endtime;   //到达时间
        public TextView tv_duration;   //耗时多长
        public TextView tv_staraddress;    //起点地址
        public TextView tv_endaddress;    //终点地址
        public TextView tv_price;    //价格
        public TextView tv_set;    //车位等级
        public TextView tv_remainnum;    //剩余车位

        public ViewHolder(View itemView) {
            super(itemView);
            tv_trainnum = (TextView) itemView.findViewById(R.id.tv_trainnum);
            tv_startime = (TextView) itemView.findViewById(R.id.tv_startime);
            tv_endtime = (TextView) itemView.findViewById(R.id.tv_endtime);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            tv_staraddress = (TextView) itemView.findViewById(R.id.tv_staraddress);
            tv_endaddress = (TextView) itemView.findViewById(R.id.tv_endaddress);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_set = (TextView) itemView.findViewById(R.id.tv_set);
            tv_remainnum = (TextView) itemView.findViewById(R.id.tv_remainnum);
        }
    }
}
