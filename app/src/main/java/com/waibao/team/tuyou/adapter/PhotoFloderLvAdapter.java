package com.waibao.team.tuyou.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.dto.ImageFloder;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/3/11.
 */
public class PhotoFloderLvAdapter extends BaseAdapter {

    //图片url集合
    private ArrayList<ImageFloder> datas;

    public PhotoFloderLvAdapter(ArrayList<ImageFloder> datas) {
        this.datas = datas;
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
            convertView = View.inflate(ConstanceUtils.CONTEXT, R.layout.photo_floder_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //设置文件夹第一张图片显示
        Glide.with(ConstanceUtils.CONTEXT)
                .load("file://" + datas.get(position).getFirstImagePath())
                .override(120,120)
                .into(holder.img);

        //设置文件夹名
        holder.tv_flodername.setText(datas.get(position).getName().replace("/", ""));

        //设置文件夹内图片数量
        holder.tv_flodercount.setText("(" + datas.get(position).getCount() + ")");

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.floder_img)
        ImageView img;
        @Bind(R.id.tv_floder)
        TextView tv_flodername;
        @Bind(R.id.tv_foloadcount)
        TextView tv_flodercount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
