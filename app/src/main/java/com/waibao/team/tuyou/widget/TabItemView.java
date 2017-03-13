package com.waibao.team.tuyou.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;

/**
 * Created by Delete_exe on 2016/5/6.
 */
public class TabItemView extends RelativeLayout {
    private TextView textView;
    private View image;

    //    PropertyValuesHolder inpvhX = PropertyValuesHolder.ofFloat(ALPHA, 0f, 1f);
    PropertyValuesHolder inpvhY = PropertyValuesHolder.ofFloat(SCALE_X, 0f, 1f);
    PropertyValuesHolder inpvhZ = PropertyValuesHolder.ofFloat(SCALE_Y, 0f, 1f);

    //    PropertyValuesHolder outpvhX = PropertyValuesHolder.ofFloat(ALPHA, 1f, 0f);
    PropertyValuesHolder outpvhY = PropertyValuesHolder.ofFloat(SCALE_X, 1f, 0f);
    PropertyValuesHolder outpvhZ = PropertyValuesHolder.ofFloat(SCALE_Y, 1f, 0f);


    public TabItemView(Context context, CharSequence text, int backResId, int textcolor, int position) {
        super(context);
        View converView = View.inflate(context, R.layout.tabitemview, null);

        textView = (TextView) converView.findViewById(R.id.tab_tx);
        image = converView.findViewById(R.id.tab_img);
        textView.setText(text);
        textView.setTextColor(textcolor);
        image.setBackgroundResource(backResId);
        addView(converView);
        if (0 == position) {
            ObjectAnimator.ofPropertyValuesHolder(image, inpvhY, inpvhZ).setDuration(1).start();
            ObjectAnimator.ofPropertyValuesHolder(textView, outpvhY, outpvhZ).setDuration(1)
                    .start();
        } else {
            ObjectAnimator.ofPropertyValuesHolder(textView, inpvhY, inpvhZ).setDuration(1).start();
            ObjectAnimator.ofPropertyValuesHolder(image, outpvhY, outpvhZ).setDuration(1)
                    .start();
        }
    }

    public void toggle(boolean isToSelect) {
        if (isToSelect) {
            ObjectAnimator.ofPropertyValuesHolder(image, inpvhY, inpvhZ).setDuration(250).start();
            ObjectAnimator.ofPropertyValuesHolder(textView, outpvhY, outpvhZ).setDuration(250)
                    .start();
        } else {
            ObjectAnimator.ofPropertyValuesHolder(textView, inpvhY, inpvhZ).setDuration(250).start();
            ObjectAnimator.ofPropertyValuesHolder(image, outpvhY, outpvhZ).setDuration(250)
                    .start();
        }
    }


}
