package com.waibao.team.tuyou.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by Azusa on 2016/5/6.
 */
public class RecentSearchRvAdapter extends RecyclerView.Adapter<RecentSearchRvAdapter.ViewHolder> {

    private List<String> datas;
    private OnItemClickListener onItemClickListener;

    public RecentSearchRvAdapter(List<String> datas) {
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_recentsearch_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_key.setText(datas.get(position));

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

        public TextView tv_key;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_key = (TextView) itemView.findViewById(R.id.tv_key);
        }
    }
}
