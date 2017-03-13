package com.waibao.team.tuyou.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.dto.PathWayDto;
import com.waibao.team.tuyou.event.AddPathWayEvent;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/5/22.
 */
public class NewTripGroupPathWayLvAdapter extends BaseAdapter{

    private List<PathWayDto> datas;

    public NewTripGroupPathWayLvAdapter(List<PathWayDto> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = View.inflate(ConstanceUtils.CONTEXT, R.layout.item_pathwats_lv, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(StringUtil.isEmpty(datas.get(position).getAddress())){
            holder.tvCity.setText("时间");
        }else {
            holder.tvCity.setText(datas.get(position).getAddress());
        }

        if(StringUtil.isEmpty(datas.get(position).getTime())){
            holder.tvTime.setText("途经地地址");
        }else {
            holder.tvTime.setText(datas.get(position).getTime());
        }


        holder.rlStartime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AddPathWayEvent(1,position));
            }
        });

        holder.rlAddpathway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new AddPathWayEvent(2,position));
            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.viewdel)
        View btnDel;
        @Bind(R.id.tv_city)
        TextView tvCity;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.rl_startime)
        RelativeLayout rlStartime;
        @Bind(R.id.rl_addpathway)
        RelativeLayout rlAddpathway;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
