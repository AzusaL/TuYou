package com.waibao.team.tuyou.adapter;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Azusa on 2016/5/28.
 */
public class SearchFriendsLvAdapter extends BaseAdapter {

    private List<User> datas;
    private Fragment fragment;

    public SearchFriendsLvAdapter(List<User> datas, Fragment fragment) {
        this.datas = datas;
        this.fragment = fragment;
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
            convertView = View.inflate(ConstanceUtils.CONTEXT, R.layout.item_search_lv_frienda, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvName.setText(datas.get(position).getNickname());
        Glide.with(fragment)
                .load(Config.Pic + datas.get(position).getImgUrl())
                .bitmapTransform(new CropCircleTransformation(ConstanceUtils.CONTEXT))
                .into(holder.mImgHead);

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.img_head)
        ImageView mImgHead;
        @Bind(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
