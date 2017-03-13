package com.waibao.team.tuyou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/5/28.
 */
public class SearchTripGroupsLvAdapter extends BaseAdapter {

    private List<GroupDto> datas;
    private Activity activity;

    public SearchTripGroupsLvAdapter(List<GroupDto> datas, Activity activity) {
        this.datas = datas;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = View.inflate(ConstanceUtils.CONTEXT, R.layout.item_homefragment_rv_tuan, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GroupDto groupDto = datas.get(position);
        Glide.with(activity).load(Config.Pic + groupDto.getImgUrl().split(";")[0]).into(holder.tuan_img);
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

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tuijian_tuan_img)
        ImageView tuan_img;
        @Bind(R.id.tuijian_tuan_way)
        TextView tuan_way;
        @Bind(R.id.tuijian_tuan_time)
        TextView tuan_time;
        @Bind(R.id.tuijian_tuan_jieshao)
        TextView tv;
        @Bind(R.id.tuijian_tuan_peoplenum)
        TextView tuan_peoplenum;
        @Bind(R.id.tuijian_tuan_collectionnum)
        TextView tuan_collectionnum;
        @Bind(R.id.tuijian_tuan_username)
        TextView tuan_uname;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
