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
import com.waibao.team.tuyou.dto.JournalDto;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/5/28.
 */
public class SearchJournalsLvAdapter extends BaseAdapter {

    private List<JournalDto> datas;
    private Activity mActivity;

    public SearchJournalsLvAdapter(List<JournalDto> datas, Activity activity) {
        this.datas = datas;
        this.mActivity = activity;
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
        final ViewHolder holder;
        if (null == convertView) {
            convertView = View.inflate(ConstanceUtils.CONTEXT, R.layout.item_searchtravels_lv, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        List<String> imgs = StringUtil.getList(datas.get(position).getImgUrl());
        Glide.with(mActivity)
                .load(Config.Pic + imgs.get(0))
                .into(holder.mImg);

        holder.mTvName.setText(datas.get(position).getNickname());

        holder.mTvTitle.setText(datas.get(position).getTitle());

        holder.mTvContent.setText(datas.get(position).getContent());

        holder.mTvLookmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mTvContent.setMaxLines(10000);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.img)
        ImageView mImg;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_lookmore)
        TextView mTvLookmore;
        @Bind(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
