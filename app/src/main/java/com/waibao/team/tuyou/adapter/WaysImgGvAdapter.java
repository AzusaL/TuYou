package com.waibao.team.tuyou.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/5/22.
 */
public class WaysImgGvAdapter extends BaseAdapter {

    private List<String> img_urls;

    public WaysImgGvAdapter(List<String> img_urls) {
        this.img_urls = img_urls;
    }

    @Override
    public int getCount() {
        return img_urls.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = View.inflate(ConstanceUtils.CONTEXT, R.layout.item_newtripgroupimg_gv, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(ConstanceUtils.CONTEXT)
                .load(Config.Pic + img_urls.get(position))
                .into(holder.mImg);

        holder.btn.setVisibility(View.GONE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.img)
        ImageView mImg;
        @Bind(R.id.btn_delete)
        Button btn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
