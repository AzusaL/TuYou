package com.waibao.team.tuyou.widget;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.listener.BackClickListener;
import com.waibao.team.tuyou.utils.DisplayUtil;

/**
 * Created by Azusa on 2016/5/5.
 */
public class ToolBarBuilder {
    private AppCompatActivity activity;
    private Toolbar toolbar;
    private String title = "";
    private int default_elevation = 5;  //默认显示5dp阴影
    private boolean default_backbtn_visiable = true; //默认显示返回按钮
    private int default_textcolor = 0xffffffff; //默认标题颜色为白色
    private BackClickListener backClickListener;

    public ToolBarBuilder(AppCompatActivity activity, Toolbar toolbar) {
        this.activity = activity;
        this.toolbar = toolbar;
    }

    //设置toolbar
    public void build() {
        toolbar.setTitle(title);// 要在setSupportActionBar之前调用才会有效
        toolbar.setTitleTextColor(default_textcolor);
        toolbar.setBackgroundColor(0xff56bc7b);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(DisplayUtil.dip2px(default_elevation));
        }
        toolbar.setMinimumHeight((int) DisplayUtil.dip2px(56));
        activity.setSupportActionBar(toolbar);

        if (default_backbtn_visiable) {
            toolbar.setNavigationIcon(R.drawable.back_menu);//显示返回按钮
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (backClickListener != null) {
                        backClickListener.back();
                    } else {
                        activity.finish();
                    }
                }
            });
        }
    }

    //设置标题
    public ToolBarBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    //设置是否显示返回按钮
    public ToolBarBuilder setCanBack(boolean flag) {
        this.default_backbtn_visiable = flag;
        return this;
    }

    //设置标题颜色
    public ToolBarBuilder setTextColor(int color) {
        this.default_textcolor = color;
        return this;
    }

    //设置阴影大小
    public ToolBarBuilder setElevation(int size) {
        this.default_elevation = size;
        return this;
    }

    //设置返回按钮点击回调接口
    public ToolBarBuilder setBackClickListener(BackClickListener listener) {
        this.backClickListener = listener;
        return this;
    }

}
