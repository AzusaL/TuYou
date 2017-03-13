package com.waibao.team.tuyou.app;

import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.waibao.team.tuyou.service.LocationService;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Azusa on 2016/5/6.
 */
public class Application extends android.app.Application {

    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        ConstanceUtils.CONTEXT = getApplicationContext();
        ConstanceUtils.resources = getResources();
        JMessageClient.init(getApplicationContext());
        JPushInterface.setDebugMode(true);

        //初始化百度定位和地图
        locationService = new LocationService(ConstanceUtils.CONTEXT);
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }
}
