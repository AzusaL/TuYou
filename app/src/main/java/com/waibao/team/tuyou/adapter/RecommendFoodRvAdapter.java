package com.waibao.team.tuyou.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.dto.FoodDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ImgsUtil;
import com.waibao.team.tuyou.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/5/6.
 */
public class RecommendFoodRvAdapter extends RecyclerView.Adapter<RecommendFoodRvAdapter.ViewHolder> {

    private List<FoodDto> datas;
    private Activity mActivity;
    private OnItemClickListener onItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        onItemClickListener = itemClickListener;
    }

    public RecommendFoodRvAdapter(List<FoodDto> datas, Activity activity) {
        this.datas = datas;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_recommend_food_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Random random = new Random();
        Glide.with(mActivity)
                .load(ImgsUtil.imgFoods[random.nextInt(ImgsUtil.imgFoods.length - 2)])
                .into(holder.mImgFooddhead);

        holder.mTvFoodname.setText(datas.get(position).getName()+"（"+datas.get(position).getTags()+"）");

        if(StringUtil.isEmpty(datas.get(position).getPhone())){
            holder.mTvPhone.setText("暂无");
        }else {
            holder.mTvPhone.setText(datas.get(position).getPhone());
        }

        if(StringUtil.isEmpty(datas.get(position).getProduct_rating())){
            holder.mTvRating.setText("评分：1.0");
        }else {
            holder.mTvRating.setText("评分："+datas.get(position).getProduct_rating());
        }

        if(StringUtil.isEmpty(datas.get(position).getAvg_price())||datas.get(position).getAvg_price().equals("0")){
            holder.mTvPrice.setText("￥1.0起");
        }else {
            holder.mTvPrice.setText("￥"+datas.get(position).getAvg_price()+"起");
        }

        holder.mTvAddress.setText(datas.get(position).getCity()+datas.get(position).getArea()+datas.get(position).getAddress());

        String star = datas.get(position).getStars();
        int count = 0;
        if(star.startsWith("0")){
            count=0;
        }else if(star.startsWith("1")){
            count=1;
        }else if(star.startsWith("2")){
            count = 2;
        }else if(star.startsWith("3")){
            count = 3;
        }else if(star.startsWith("4")){
            count = 4;
        }else {
            count = 5;
        }
        if(count==0){
            holder.mRlStar.setVisibility(View.GONE);
        }else {
            holder.mRlStar.setVisibility(View.VISIBLE);
            for(int i = 0; i < count; i++) {
                holder.stars.get(i).setBackgroundResource(R.drawable.icon_star_light);
            }
            for(int i = count; i < 5; i++) {
                holder.stars.get(i).setBackgroundResource(R.drawable.ic_star_grey);
            }
        }
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

        @Bind(R.id.img_fooddhead)
        ImageView mImgFooddhead;
        @Bind(R.id.tv_foodname)
        TextView mTvFoodname;
        @Bind(R.id.view_star1)
        View mViewStar1;
        @Bind(R.id.view_star2)
        View mViewStar2;
        @Bind(R.id.view_star3)
        View mViewStar3;
        @Bind(R.id.view_star4)
        View mViewStar4;
        @Bind(R.id.view_star5)
        View mViewStar5;
        @Bind(R.id.rl_star)
        LinearLayout mRlStar;
        @Bind(R.id.tv_rating)
        TextView mTvRating;
        @Bind(R.id.tv_phone)
        TextView mTvPhone;
        @Bind(R.id.rl_phone)
        LinearLayout mRlPhone;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_address)
        TextView mTvAddress;
        public List<View> stars;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            stars = new ArrayList<>();
            stars.add(mViewStar1);
            stars.add(mViewStar2);
            stars.add(mViewStar3);
            stars.add(mViewStar4);
            stars.add(mViewStar5);
        }
    }

}
