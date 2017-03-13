package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.TabFragmentPagerAdapter;
import com.waibao.team.tuyou.app.Application;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.database.SharedPreferenceData;
import com.waibao.team.tuyou.dto.CityDto;
import com.waibao.team.tuyou.event.LoginEvent;
import com.waibao.team.tuyou.main_fragment.HomeFragment;
import com.waibao.team.tuyou.main_fragment.JournalsFragment;
import com.waibao.team.tuyou.main_fragment.MeFragment;
import com.waibao.team.tuyou.main_fragment.MyGroupFragment;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.service.LocationService;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.TabItemView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Request;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.vp_body)
    ViewPager viewpager;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    @Bind(R.id.fabBtn)
    FloatingActionButton fabBtn;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;
    @Bind(R.id.btn_location)
    Button btnLocation;

    private LocationService locationService;  //定位服务
    private List<Fragment> fragmentsList;
    private List<String> titles;
    private List<Integer> imgsId;
    private TabItemView[] tabItem = new TabItemView[4];
    private int currentPage = 0; //当前页面的position
    private MyHandler mHandler;

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> activityWeakReference;

        public MyHandler(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = activityWeakReference.get();
            if (activity != null) {
                if (msg.what == 100) {
                    activity.upLoadLocMsg();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mHandler = new MyHandler(this);
        new CityParseThread().start();
        if (!UserUtil.userisLogin && SharedPreferenceData.getInstance().getIsLogined(this)) {
            autoLogin();
        }
        initViewPager();
        JMessageClient.registerEventReceiver(this);
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        final Map<String, String> map = SharedPreferenceData.getInstance().getUsernamePwd(this);
        final String name = map.get("name");
        final String pwd = map.get("pwd");
        OkHttpUtils.get().url(Config.IP + "/user_login")
                .addParams("loginName", name)
                .addParams("passwd", pwd)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络连接出错");
            }

            @Override
            public void onResponse(String response) {
                String messageInfo;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    messageInfo = jsonObject.get("messageInfo").toString();

                    if (messageInfo.equals("成功")) {
                        ObjectMapper mapper = new ObjectMapper();
                        String userJson = jsonObject.get("userDto").toString();
                        UserUtil.user = mapper.readValue(userJson, User.class);
                        UserUtil.userisLogin = true;
                        EventBus.getDefault().post(new LoginEvent(true));
                        mHandler.sendEmptyMessageDelayed(100, 5000);
                        loginJpush(name, pwd);
                    } else if (messageInfo.equals("用户名不存在") || messageInfo.equals("密码不正确")
                            || messageInfo.equals("服务器出错，请稍后重试")) {
                        ToastUtil.showToast(messageInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonParseException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loginJpush(final String name, final String pwd) {
        JPushInterface.setAlias(ConstanceUtils.CONTEXT, UserUtil.user.getId(), null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                JMessageClient.login(name, pwd, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.e("J自动登录", "gotResult: " + i);
                    }
                });
            }
        }).start();
    }

    //解析城市json数据和城市航班信息
    public static class CityParseThread extends Thread {
        @Override
        public void run() {
            super.run();
            StringBuffer stringBufferCity = new StringBuffer();
            StringBuffer stringBufferAir = new StringBuffer();
            String line;
            String line2;
            try {
                InputStream im = ConstanceUtils.resources.openRawResource(R.raw.city);
                BufferedReader read = new BufferedReader(new InputStreamReader(im));
                while ((line = read.readLine()) != null) {
                    stringBufferCity.append(line);
                }
                JSONObject jsonObject = new JSONObject(stringBufferCity.toString());
                String data = jsonObject.getString("result");
                ObjectMapper mapper = new ObjectMapper();
                ConstanceUtils.cityDtos = mapper.readValue(data, new TypeReference<List<CityDto>>() {
                });
                ConstanceUtils.citys = new String[378];
                for (int i = 0; i < ConstanceUtils.cityDtos.size(); i++) {
                    ConstanceUtils.citys[i] = ConstanceUtils.cityDtos.get(i).getCityName();
                }

                InputStream im2 = ConstanceUtils.resources.openRawResource(R.raw.airplanecode);
                BufferedReader readAir = new BufferedReader(new InputStreamReader(im2));
                while ((line2 = readAir.readLine()) != null) {
                    stringBufferAir.append(line2);
                }

                JSONObject jsonObject2 = new JSONObject(stringBufferAir.toString());
                JSONArray jsonArray = jsonObject2.getJSONArray("cities");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject subObject = (JSONObject) jsonArray.get(i);
                    ConstanceUtils.airCitys[i] = subObject.getString("name") + "," + subObject.getString("code");
                }
                im.close();
                im2.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initViewPager() {
        tabs = (TabLayout) findViewById(R.id.tabs);
        titles = new ArrayList<>();
        titles.add("热门");
        titles.add("游记");
        titles.add("行团");
        titles.add("我的");

        imgsId = new ArrayList<>();
        imgsId.add(R.drawable.ic_hot);
        imgsId.add(R.drawable.ic_img);
        imgsId.add(R.drawable.ic_airplanemode_on_white);
        imgsId.add(R.drawable.ic_persion);

        for (int i = 0; i < 4; i++) {
            tabItem[i] = new TabItemView(MainActivity.this, titles.get(i), imgsId.get(i), 0xffffffff, i);
            TabLayout.Tab tab = tabs.newTab().setCustomView(tabItem[i]);
            tabs.addTab(tab);
        }

        viewpager = (ViewPager) findViewById(R.id.vp_body);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPage = tab.getPosition();
                viewpager.setCurrentItem(tab.getPosition(), false);
                ((TabItemView) tab.getCustomView()).toggle(true);

                //游记页面显示fab
                if (tab.getPosition() == 1 || tab.getPosition() == 2) {
                    fabBtn.setClickable(true);
                    fabBtn.show();
                    if (tab.getPosition() == 1) {
                        fabBtn.setImageResource(R.drawable.ic_edit_white);
                    } else {
                        fabBtn.setImageResource(R.drawable.ic_add_white);
                    }
                } else {
                    fabBtn.setClickable(false);
                    fabBtn.hide();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TabItemView) tab.getCustomView()).toggle(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        fragmentsList = new ArrayList<>();
        Fragment fg1 = new HomeFragment();
        Fragment fg2 = new JournalsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("what", 1);
        fg2.setArguments(bundle);
        Fragment fg3 = new MyGroupFragment();
        fg3.setArguments(bundle);
        Fragment fg4 = new MeFragment();

        fragmentsList.add(fg1);
        fragmentsList.add(fg2);
        fragmentsList.add(fg3);
        fragmentsList.add(fg4);
        viewpager.setAdapter(new TabFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentsList, titles));
        viewpager.setCurrentItem(0);

        final TabLayout.TabLayoutOnPageChangeListener listener =
                new TabLayout.TabLayoutOnPageChangeListener(tabs);
        viewpager.setOnPageChangeListener(listener);
        viewpager.setOffscreenPageLimit(4);

        //首页隐藏fab
        fabBtn.hide();
        fabBtn.setClickable(false); //首页时设置为不可点击，用于区分是哪个页面

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        JMessageClient.unRegisterEventReceiver(this);
    }

    public void onEvent(NotificationClickEvent event) {
        if (event.getMessage().getTargetType().equals(ConversationType.group)) {
//        Intent notificationIntent = new Intent(ConstanceUtils.CONTEXT, ConversationGroupActivity.class);
//        startActivity(notificationIntent);//跳转到群聊页面
            Log.e("LLLLL", "onEvent: group");
        } else if (event.getMessage().getTargetType().equals(ConversationType.single)) {
//        Intent notificationIntent = new Intent(ConstanceUtils.CONTEXT, ConversationActivity.class);
//        startActivity(notificationIntent);//自跳转到私聊页面
            Log.e("LLLLL", "onEvent: single");
        }
    }

    @OnClick({R.id.fabBtn, R.id.rl_search, R.id.btn_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabBtn:
                if (1 == currentPage) {
                    Intent intent = new Intent(MainActivity.this, NewJournalActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, NewTripGroupActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_location:
                Intent intent2 = new Intent(MainActivity.this, NearbyMsgActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((Application) getApplication()).locationService;
        //获取locationservice实例
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();

    }


    /*****
     * 定位结果回调
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append(location.getCity() + " "); // 返回的城市
                sb.append(location.getDistrict() + " "); // 区
                sb.append(location.getStreet() + " "); // 街道
                sb.append(location.getLocationDescribe()); // 详细地址

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // gps定位成功
                    ConstanceUtils.LOCATION_MSG = sb.toString();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位成功
                    ConstanceUtils.LOCATION_MSG = sb.toString();
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位成功，离线定位结果也是有效的
                    ConstanceUtils.LOCATION_MSG = sb.toString();
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    // 服务端网络定位失败
                    ToastUtil.showToast("无法获取当前定位");
                    return;
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    // 网络不通导致定位失败，请检查网络是否通畅
                    ToastUtil.showToast("无法获取当前定位");
                    return;
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    // 无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    ToastUtil.showToast("无法获取当前定位");
                    return;
                }
                //获取经纬度
                ConstanceUtils.LOCATION_LATITUDE = location.getLatitude();
                ConstanceUtils.LOCATION_LONGITUDE = location.getLongitude();

                ConstanceUtils.LOCATION_CITY = location.getCity();

                locationService.unregisterListener(mListener); //定位成功后注销定位监听
                locationService.stop(); //定位成功后停止定位服务

//                Log.e("main", "onReceiveLocation: " + ConstanceUtils.LOCATION_MSG);
            }
        }

    };

    private void upLoadLocMsg() {
        OkHttpUtils.get().url(Config.IP + "/user_setUserLatAndLng")
                .addParams("uid", UserUtil.user.getId())
                .addParams("lat", ConstanceUtils.LOCATION_LATITUDE + "")
                .addParams("lng", ConstanceUtils.LOCATION_LONGITUDE + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showNetError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(String response) {
//                Log.e("main", "onResponse: " + response);
            }
        });

    }


}
