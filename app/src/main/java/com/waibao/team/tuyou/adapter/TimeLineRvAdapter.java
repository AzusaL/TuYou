package com.waibao.team.tuyou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.dto.PathWayDto;

import java.util.List;

/**
 * Created by Azusa on 2016/5/6.
 */
public class TimeLineRvAdapter extends RecyclerView.Adapter<TimeLineRvAdapter.ViewHolder> {

    private List<PathWayDto> datas;

    public TimeLineRvAdapter(List<PathWayDto> datas) {
        this.datas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (0 == position) {
            holder.linetop.setVisibility(View.GONE);
        } else {
            holder.linetop.setVisibility(View.VISIBLE);
        }

        if (datas.size() - 1 == position) {
            holder.linebottom.setVisibility(View.GONE);
        } else {
            holder.linebottom.setVisibility(View.VISIBLE);
        }

        holder.mTvWay.setText(datas.get(position).getAddress()+"");

        holder.mTvTime.setText(datas.get(position).getTime().substring(5)+"");

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View linetop;
        private View linebottom;
        private TextView mTvWay;
        private TextView mTvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            linetop = itemView.findViewById(R.id.linetop);
            linebottom = itemView.findViewById(R.id.linebottom);
            mTvWay = (TextView) itemView.findViewById(R.id.tv_address);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
