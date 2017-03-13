package com.waibao.team.tuyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class WrapHeightGridView extends GridView {

	public WrapHeightGridView(Context context) {
		this(context, null);
	}

	public WrapHeightGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOverScrollMode(View.OVER_SCROLL_NEVER);
		setFocusable(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightSpec);
	}
}
