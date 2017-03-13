package com.waibao.team.tuyou.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/6/5.
 */
public class LabelGvAdapter extends BaseAdapter {

    private List<String> datas;

    public LabelGvAdapter(List<String> datas) {
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
            convertView = View.inflate(ConstanceUtils.CONTEXT, R.layout.item_label_gv, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == datas.size() - 1) {
            holder.tvLabel.setText("+");
            holder.tvLabel.setTextSize(20);
        }else {
            holder.tvLabel.setTextSize(13);
            holder.tvLabel.setText(datas.get(position));
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_label)
        TextView tvLabel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
