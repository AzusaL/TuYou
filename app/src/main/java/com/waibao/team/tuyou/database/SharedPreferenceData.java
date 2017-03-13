package com.waibao.team.tuyou.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.waibao.team.tuyou.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Azusa on 2015/12/26.
 */
public class SharedPreferenceData {
    public SharedPreferenceData() {
    }

    private static final SharedPreferenceData sharedPrefrenceData = new SharedPreferenceData();

    public static SharedPreferenceData getInstance() {
        return sharedPrefrenceData;
    }

    //保存用户名
    public void saveLoginName(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("loginname", name).commit();
        editor.clear();
    }

    //获得用户名
    public String getLoginName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        return sp.getString("loginname", "");
    }

    //保存用户是否已经登录
    public void saveIsLogined(Context context, boolean isLogined) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("IsLogined", isLogined).commit();
        editor.clear();
    }

    //获取用户是否已经登录
    public boolean getIsLogined(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        return sp.getBoolean("IsLogined", false);
    }

    //保存最后一次登录的用户名，密码
    public void saveUsernamePwd(Context context, String name, String pwd) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name).putString("pwd", pwd).commit();
        editor.clear();
    }

    //获取最后一次登录的用户名，密码
    public Map<String, String> getUsernamePwd(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        Map<String, String> map = new HashMap<>();
        map.put("name", sp.getString("name", ""));
        map.put("pwd", sp.getString("pwd", ""));
        return map;
    }

    //保存搜索记录
    public void saveSearchtext(Context context, String text) {
        ArrayList<String> list = getSearchList(context);
        if (list.contains(text)) {
            list.remove(list.indexOf(text));
        }
        list.add(0, text);
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("searchtext", JsonUtils.getJsonStringformat(list)).commit();
        editor.clear();
    }

    //获得用户搜索记录
    public ArrayList<String> getSearchList(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        return JsonUtils.getlistfromString(sp.getString("searchtext", ""));
    }

    //清除搜索记录
    public void clearSearchtext(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("searchtext", "").commit();
        editor.clear();
    }


    //保存是否打开无图模式配置
    public void saveImageConfig(Context context, boolean isImageDownload) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isImageDownlaod", isImageDownload).commit();
        editor.clear();
    }

    //获取是否打开无图模式配置
    public boolean isUserDownloadImage(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sharedpreference", Context.MODE_PRIVATE);
        return sp.getBoolean("isImageDownlaod", false);
    }
}