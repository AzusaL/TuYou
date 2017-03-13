package com.waibao.team.tuyou.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.listener.PhotoRvItemClickLitener;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Azusa on 2016/3/12.
 */
public class PhotoSelectRvAdapter extends RecyclerView.Adapter<PhotoSelectRvAdapter.ViewHolder> {

    //图片url集合
    private ArrayList<String> url_datas;
    private HashMap<String, Boolean> check_position; //key为图片url，值为是否被选中
    private PhotoRvItemClickLitener recyclerViewItemClickLitener;

    public void setOnItemClickLitener(PhotoRvItemClickLitener onItemClickLitener) {
        this.recyclerViewItemClickLitener = onItemClickLitener;
    }

    public PhotoSelectRvAdapter(ArrayList<String> datas, ArrayList<String> selectdatas) {
        this.url_datas = datas;
        check_position = new HashMap<>();
        for (int i = 0; i < datas.size(); i++) {
            check_position.put(datas.get(i), false);
        }
        for (int i = 0; i < selectdatas.size(); i++) {
            check_position.put(selectdatas.get(i).replace("file://",""), true);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.photo_gridview_item, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //position 0显示照相机图标，其他position显示图片
        if (0 == position) {
            holder.img.setVisibility(View.GONE);
            holder.img_takephoto.setVisibility(View.VISIBLE);
            holder.checkview.setVisibility(View.GONE);
        } else {
            if (check_position.get(url_datas.get(position)) == null) {
                check_position.put(url_datas.get(position), false);
            }
            holder.checkview.setVisibility(View.VISIBLE);
            //设置是否为选中状态
            if (check_position.get(url_datas.get(position))) {
                holder.checkview.setVisibility(View.VISIBLE);
                holder.img.setColorFilter(Color.parseColor("#50000000"));
            } else {
                holder.checkview.setVisibility(View.GONE);
                holder.img.setColorFilter(Color.parseColor("#00000000"));
            }
            holder.img.setVisibility(View.VISIBLE);
            holder.img_takephoto.setVisibility(View.GONE);

            //设置图片显示
            Glide.with(ConstanceUtils.CONTEXT)
                    .load("file://" + url_datas.get(position))
                    .override(200,200)
                    .into(holder.img);
        }

        if (recyclerViewItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != 0) {
                        //判断是否已经被点击过,改变不同状态下的显示
                        if (check_position.get(url_datas.get(position))) {
                            holder.checkview.setVisibility(View.GONE);
                            holder.img.setColorFilter(Color.parseColor("#00000000"));
                        } else {
                            holder.checkview.setVisibility(View.VISIBLE);
                            holder.img.setColorFilter(Color.parseColor("#50000000"));
                        }
                        check_position.put(url_datas.get(position),
                                !check_position.get(url_datas.get(position)));
                    }
                    //点击事件回调接口
                    recyclerViewItemClickLitener.onItemClick(v, position, "file://" + url_datas.get(position),
                            check_position.get(url_datas.get(position)));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return url_datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img; //相册图片
        public ImageView img_takephoto;  //显示在第一个位置的照相机的图标
        public View checkview; //是否选中不同状态的View

        public ViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.gridview_img);
            img_takephoto = (ImageView) view.findViewById(R.id.img_takephoto);
            checkview = view.findViewById(R.id.item_check);

        }
    }
}
