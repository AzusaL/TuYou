package com.waibao.team.tuyou.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Delete_exe on 2016/5/22.
 */
public class FlashAdapter extends PagerAdapter {
    private List<String> ImageUris;
    private List<ImageView> imageViews = new ArrayList<>();
    private int im[] = {R.drawable.fj2, R.drawable.fj3, R.drawable.fj4, R.drawable.fj5};

    public FlashAdapter(List<String> imageUris, Context context) {
        ImageUris = imageUris;
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(ConstanceUtils.CONTEXT)
                    .load(im[i])
                    .into(imageView);
            imageViews.add(imageView);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position));//添加页卡
        return imageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
