package com.waibao.team.tuyou.utils;

import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Delete_exe on 2016/5/13.
 */
public class ToastUtil {
    private static Toast mToast;

    public static void showToast(String text, int gravity) {
        if (null != mToast) {
            mToast.cancel();
            mToast = Toast.makeText(ConstanceUtils.CONTEXT, text, Toast.LENGTH_SHORT);
        } else {
            mToast = Toast.makeText(ConstanceUtils.CONTEXT, text, Toast.LENGTH_SHORT);
        }
        mToast.setGravity(gravity, 0, 200);
        mToast.show();
    }

    public static void showToast(String s) {
        showToast(s, Gravity.BOTTOM);
    }
    public static void showToast_center(String s) {
        showToast(s, Gravity.CENTER);
    }
    public static void showNetError(){
        showToast("网络链接出错");
    }
}
