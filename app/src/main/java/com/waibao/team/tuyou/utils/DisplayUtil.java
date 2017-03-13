package com.waibao.team.tuyou.utils;

/**
 * Created by Delete_exe on 2016/5/12.
 */
public class DisplayUtil {
    public static float dip2px(float dpValue) {
        final float scale = ConstanceUtils.CONTEXT.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    public static float px2dip(float pxValue) {
        final float scale = ConstanceUtils.CONTEXT.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    public static float px2sp(float pxValue) {
        final float fontScale = ConstanceUtils.CONTEXT.getResources().getDisplayMetrics().scaledDensity;
        return pxValue / fontScale + 0.5f;
    }

    public static float sp2px(float spValue) {
        final float fontScale = ConstanceUtils.CONTEXT.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }
}
