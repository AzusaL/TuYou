package com.waibao.team.tuyou.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/5/22.
 */
public class NewTripGroupImgGvAdapter extends BaseAdapter {

    private List<String> img_urls;

    public NewTripGroupImgGvAdapter(List<String> img_urls) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = View.inflate(ConstanceUtils.CONTEXT, R.layout.item_newtripgroupimg_gv, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (0 == position) {
            holder.mDelete.setVisibility(View.GONE);
            Glide.with(ConstanceUtils.CONTEXT)
                    .load(R.drawable.add_photo)
                    .into(holder.mImg);
        } else {
            holder.mDelete.setVisibility(View.VISIBLE);
            Glide.with(ConstanceUtils.CONTEXT)
                    .load(img_urls.get(position))
                    .into(holder.mImg);
        }

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_urls.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.img)
        ImageView mImg;
        @Bind(R.id.btn_delete)
        Button mDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
