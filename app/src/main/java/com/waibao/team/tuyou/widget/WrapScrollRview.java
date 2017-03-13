package com.waibao.team.tuyou.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Azusa on 2015/12/31.
 */
public class WrapScrollRview extends RecyclerView {

    public WrapScrollRview(Context context) {
        super(context);
    }

    public WrapScrollRview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapScrollRview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }


}
