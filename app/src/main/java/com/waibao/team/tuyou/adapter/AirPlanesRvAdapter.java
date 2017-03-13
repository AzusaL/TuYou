package com.waibao.team.tuyou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.dto.AirPlaneDto;
import com.waibao.team.tuyou.dto.SeatInfosBean;
import com.waibao.team.tuyou.dto.TrainListBean;
import com.waibao.team.tuyou.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by Azusa on 2016/5/6.
 */
public class AirPlanesRvAdapter extends RecyclerView.Adapter<AirPlanesRvAdapter.ViewHolder> {

    private List<AirPlaneDto> datas;

    public AirPlanesRvAdapter(List<AirPlaneDto> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_airplanes_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setText(datas.get(position).getName());
        holder.tv_startime.setText(datas.get(position).getDep_time().substring(11,16));
        holder.tv_endtime.setText(datas.get(position).getArr_time().substring(11,16));
        holder.tv_duration.setText(datas.get(position).getFly_time());
        holder.tv_staraddress.setText(datas.get(position).getDep());
        holder.tv_endaddress.setText(datas.get(position).getArr());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name; //车次
        public TextView tv_startime;  //发车时间
        public TextView tv_endtime;   //到达时间
        public TextView tv_duration;   //耗时多长
        public TextView tv_staraddress;    //起点地址
        public TextView tv_endaddress;    //终点地址

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_startime = (TextView) itemView.findViewById(R.id.tv_startime);
            tv_endtime = (TextView) itemView.findViewById(R.id.tv_endtime);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            tv_staraddress = (TextView) itemView.findViewById(R.id.tv_staraddress);
            tv_endaddress = (TextView) itemView.findViewById(R.id.tv_endaddress);
        }
    }
}
