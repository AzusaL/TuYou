package com.waibao.team.tuyou.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.waibao.team.tuyou.R;

/**
 * Created by Azusa on 2016/5/5.
 */
public class CircleProgressbar extends RelativeLayout {
    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    int backgroundColor = Color.parseColor("#7ac194");

    public CircleProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);

    }

    protected void setAttributes(AttributeSet attrs) {
        int minnum = getResources().getDimensionPixelOffset(R.dimen.progressbar_widthandhight);
        setMinimumHeight(minnum);
        setMinimumWidth(minnum);

        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML,
                "background", -1);
        if (bacgroundColor != -1) {
            setBackgroundColor(getResources().getColor(bacgroundColor));
        } else {
            int background = attrs.getAttributeIntValue(ANDROIDXML,
                    "background", -1);
            if (background != -1)
                setBackgroundColor(background);
            else
                setBackgroundColor(Color.parseColor("#7ac194"));
        }

        setMinimumHeight(minnum);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAnimation(canvas);
        invalidate();
    }

    int arcD = 1;
    int arcO = 0;
    float rotateAngle = 0;
    int limite = 0;

    private void drawAnimation(Canvas canvas) {
        if (arcO == limite)
            arcD += 6;
        if (arcD >= 290 || arcO > limite) {
            arcO += 6;
            arcD -= 6;
        }
        if (arcO > limite + 290) {
            limite = arcO;
            arcO = limite;
            arcD = 1;
        }
        rotateAngle += 4;
        canvas.rotate(rotateAngle, getWidth() / 2, getHeight() / 2);

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(),
                canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO, arcD,
                true, paint);
        Paint transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(getResources().getColor(
                android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(
                PorterDuff.Mode.CLEAR));
        temp.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2)
                - getResources().getDimensionPixelOffset(R.dimen.drawCircle), transparentPaint);

        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    public void setBackgroundColor(int color) {
        super.setBackgroundColor(getResources().getColor(
                android.R.color.transparent));
        this.backgroundColor = color;
    }

}
