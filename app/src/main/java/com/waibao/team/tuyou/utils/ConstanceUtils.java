package com.waibao.team.tuyou.utils;

import android.content.Context;
import android.content.res.Resources;

import com.waibao.team.tuyou.dto.CityDto;

import java.util.List;

/**
 * Created by Azusa on 2016/5/6.
 */
public class ConstanceUtils {
    public static Context CONTEXT;
    public static final int MESSAGE_OK = 1;
    public static final int TAKEPHOTO_REQUESTCODE = 7;
    public static final int TAKEPHOTO_RESULTCODE = 27;
    public static final int CUT_PHOTO_REQUESTCODE = 8;
    public static final int CUT_PHOTO_RESULTCODE = 23;
    public static int screenWidth; //屏幕宽度
    public static int screenHight; //屏幕高度
    public static String LOCATION_MSG;  //用户当前位置文字话信息
    public static String LOCATION_CITY;  //用户当前城市
    public static double LOCATION_LONGITUDE;  //用户当前位置经度
    public static double LOCATION_LATITUDE;  //用户当前位置纬度
    public static List<CityDto> cityDtos;   //全国城市的城市dto（城市id，省份id，城市名）
    public static String[] citys;   //全国城市名
    public static String[] airCitys = new String[174];   //世界机场城市名
    public static Resources resources;  //资源列表
}
