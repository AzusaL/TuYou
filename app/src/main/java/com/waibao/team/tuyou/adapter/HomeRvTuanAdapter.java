package com.waibao.team.tuyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.StringUtil;

import java.util.List;

/**
 * Created by Azusa on 2016/5/6.
 */
public class HomeRvTuanAdapter extends RecyclerView.Adapter<HomeRvTuanAdapter.ViewHolder> {

    private List<GroupDto> datas;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public HomeRvTuanAdapter(List<GroupDto> datas) {
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.item_homefragment_rv_tuan, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        GroupDto groupDto = datas.get(position);
        Glide.with(context).load(Config.Pic + groupDto.getImgUrl().split(";")[0]).into(holder.tuan_img);
        holder.tv.setText(groupDto.getDescription());
        holder.tuan_uname.setText(groupDto.getUNickname());
        holder.tuan_peoplenum.setText(groupDto.getCurrent_people() + "");
        holder.tuan_collectionnum.setText(groupDto.getCollectionCount() + "");
        String[] ways = groupDto.getWay().split(";");
        final List<String> times = StringUtil.getList(datas.get(position).getWayTime());
        String str_way = ways[0] + "-" + ways[ways.length - 1];
        String str_time = times.get(0).substring(5).replace("-", "/") + "-"
                + times.get(times.size() - 1).substring(5).replace("-", "/");
        holder.tuan_way.setText(str_way);
        holder.tuan_time.setText(str_time);
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
        return null != datas ? datas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;
        private ImageView tuan_img;
        private TextView tuan_uname;
        private TextView tuan_peoplenum;
        private TextView tuan_way;
        private TextView tuan_time;
        private TextView tuan_collectionnum;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tuijian_tuan_jieshao);
            tuan_img = (ImageView) itemView.findViewById(R.id.tuijian_tuan_img);
            tuan_uname = (TextView) itemView.findViewById(R.id.tuijian_tuan_username);
            tuan_peoplenum = (TextView) itemView.findViewById(R.id.tuijian_tuan_peoplenum);
            tuan_way = (TextView) itemView.findViewById(R.id.tuijian_tuan_way);
            tuan_time = (TextView) itemView.findViewById(R.id.tuijian_tuan_time);
            tuan_collectionnum = (TextView) itemView.findViewById(R.id.tuijian_tuan_collectionnum);
        }
    }
}
