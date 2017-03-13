package com.waibao.team.tuyou.adapter;

import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.SelectGroupDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/5/6.
 */
public class SelectGroupRvAdapter extends RecyclerView.Adapter<SelectGroupRvAdapter.ViewHolder> {

    private List<SelectGroupDto> datas;
    private Activity context;
    private OnItemClickListener onItemClickListener;

    public SelectGroupRvAdapter(List<SelectGroupDto> datas, Activity context) {
        this.datas = datas;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_select_group_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        GroupDto groupDto = datas.get(position).getDto();

        List<String> imgs = StringUtil.getList(groupDto.getImgUrl());
        Glide.with(context)
                .load(Config.Pic + StringUtil.parseEmpty(imgs.get(0)))
                .into(holder.imgGrounps);
        List<String> ways = StringUtil.getList(groupDto.getWay());
        holder.tvAddress.setText(ways.get(0) + "-" + ways.get(ways.size() - 1));

        List<String> times = StringUtil.getList(groupDto.getWayTime());
        holder.tvTime.setText(times.get(0).substring(5).replace("-", "/") + "-"
                + times.get(times.size() - 1).substring(5).replace("-", "/")
                + " " + groupDto.getKm() + "km");

        if (datas.get(position).isCheck()) {
            holder.cbSelect.setChecked(true);
        } else {
            holder.cbSelect.setChecked(false);
        }

        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < datas.size(); i++) {
                    if(datas.get(i).isCheck()){
                        datas.get(i).setCheck(false);
                    }
                }
                datas.get(position).setCheck(true);
                notifyDataSetChanged();
            }
        });

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
        @Bind(R.id.img_grounps)
        ImageView imgGrounps;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.cb_select)
        AppCompatCheckBox cbSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
