package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.callback.HomeGroupCallback;
import com.waibao.team.tuyou.callback.UsersCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Request;

public class NearbyMsgActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bmapView)
    MapView mMapView;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.rl_change)
    RelativeLayout rlChange;

    private BaiduMap mBaiduMap;
    private List<UserDto> data = new ArrayList<>();
    private List<GroupDto> gdata = new ArrayList<>();
    private AlertDialog dialog;
    private MyHandler handler;
    private String json;
    private boolean isFirstClik = true;

    @OnClick(R.id.rl_change)
    public void onClick() {
        spinner.performClick();
    }

    private static class MyHandler extends Handler {
        private WeakReference<NearbyMsgActivity> activityWeakReference;

        public MyHandler(NearbyMsgActivity activity) {
            activityWeakReference = new WeakReference<NearbyMsgActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NearbyMsgActivity activity = activityWeakReference.get();
            if (activity != null) {
                if (msg.what == 100) {
                    Intent intent = new Intent(activity, TripGroupActivity.class);
                    intent.putExtra("dto", activity.json);
                    activity.startActivity(intent);
                    activity.dialog.cancel();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        handler = new MyHandler(this);
        initView();
        getUserData();
    }

    private void initView() {
        new ToolBarBuilder(this, toolbar).setTitle("").build();

        dialog = new MyDialog().showLodingDialog(this);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(6).build()));
        mBaiduMap.setMyLocationEnabled(true);

        spinner.setAdapter(new ArrayAdapter(ConstanceUtils.CONTEXT, R.layout.item_simpleselectitem, R.id.tv_item1, new String[]{"附近的人", "附近的行程"}));
        // 分类选择器点击监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                view.setVisibility(View.GONE);
                mBaiduMap.clear();
                if (position == 0) {
                    tvTitle.setText("附近的人");
                    dialog.show();
                    getUserData();
                } else {
                    tvTitle.setText("附近的行程");
                    dialog.show();
                    getGroupData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getGroupData() {
        OkHttpUtils.get().url(Config.IP + "/group_getAllGroup")
                .build().execute(new HomeGroupCallback() {
            @Override
            public void onError(Call call, Exception e) {
                dialog.cancel();
                ToastUtil.showNetError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(List<GroupDto> response) {
                dialog.cancel();
                gdata.addAll(response);
                initGroupOverlay();
                setMarker();
            }
        });

    }

    private void initGroupOverlay() {
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(ConstanceUtils.LOCATION_LATITUDE, ConstanceUtils.LOCATION_LONGITUDE));
        mBaiduMap.animateMapStatus(u);
        for (int i = 0; i < gdata.size(); i++) {
            Log.e("main", "initGroupOverlay: " + gdata.get(i).getLat() + "-----" + gdata.get(i).getLng());
            final int position = i;
            double lat = gdata.get(i).getLat();
            double lng = gdata.get(i).getLng();
            final LatLng p1 = new LatLng(lat, lng);

            List<String> imgs = StringUtil.getList(gdata.get(i).getImgUrl());
            String imgUrl = imgs.get(0);

            final View view_marker = View.inflate(this, R.layout.view_bmap_overlaymarker_group, null);

            final ImageView img = (ImageView) view_marker
                    .findViewById(R.id.img);

            TextView tv_way = (TextView) view_marker
                    .findViewById(R.id.tv_way);


            List<String> ways = StringUtil.getList(gdata.get(i).getWay());
            tv_way.setText(ways.get(0) + "-" + ways.get(ways.size() - 1));

            Glide.with(this)
                    .load(Config.Pic + imgUrl)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            img.setImageBitmap(resource);
                            OverlayOptions ooA = new MarkerOptions()
                                    .position(p1)
                                    .icon(BitmapDescriptorFactory.fromView(view_marker))
                                    .zIndex(9).draggable(false);
                            Marker marker_temp = (Marker) (mBaiduMap.addOverlay(ooA));
                            marker_temp.setTitle("" + position);
                            marker_temp.setToTop();
                        }
                    });

        }
    }

    private void setMarker() {

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                if (!isFirstClik) {
                    return true;
                }
                isFirstClik = false;
                dialog.show();
                // 这个字段用于标识数据
                final String id = marker.getTitle();
                Intent intent;
                if (id.startsWith("user")) {
                    intent = new Intent(NearbyMsgActivity.this, PersonPageActivity.class);
                    intent.putExtra("uId", id.replace("user", ""));
                    startActivity(intent);
                    dialog.cancel();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            json = JsonUtils.getJsonStringformat(gdata.get(Integer.valueOf(id)));
                            handler.sendEmptyMessageDelayed(100, 500);
                        }
                    }).start();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFirstClik = true;
    }

    public void initUserOverlay() {

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(ConstanceUtils.LOCATION_LATITUDE, ConstanceUtils.LOCATION_LONGITUDE));
        mBaiduMap.animateMapStatus(u);
        for (int i = 0; i < data.size(); i++) {
            if (i == 20) {
                break;
            }
            final int position = i;
            double lat = data.get(i).getLat();
            double lng = data.get(i).getLng();
            final LatLng p1 = new LatLng(lat, lng);

            String avatar = Config.Pic + data.get(i).getImgUrl();
            String nick = data.get(i).getNickname();

            String sex = data.get(i).getSex();

            final View view_marker = View.inflate(this, R.layout.view_bmap_overlaymarker, null);


            ImageView iv_sex = (ImageView) view_marker
                    .findViewById(R.id.view_sex);

            final ImageView iv_avatar = (ImageView) view_marker
                    .findViewById(R.id.img_userhead);

            TextView tv_name = (TextView) view_marker
                    .findViewById(R.id.tv_name);

            if (sex == null || sex.equals("1")) {
                iv_sex.setBackgroundResource(R.drawable.ic_sex_girl);
            } else {
                iv_sex.setBackgroundResource(R.drawable.ic_sex_boy);
            }

            tv_name.setText(nick);

            Glide.with(this)
                    .load(avatar)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            iv_avatar.setImageDrawable(resource);
                            OverlayOptions ooA = new MarkerOptions()
                                    .position(p1)
                                    .icon(BitmapDescriptorFactory.fromView(view_marker))
                                    .zIndex(9).draggable(false);
                            Marker marker_temp = (Marker) (mBaiduMap.addOverlay(ooA));
                            marker_temp.setTitle("user" + data.get(position).getId());
                            marker_temp.setToTop();
                        }
                    });

        }

    }

    private void getUserData() {
        OkHttpUtils.get().url(Config.IP + "/user_getAllUserLatAndLng")
                .build().execute(new UsersCallback() {
            @Override
            public void onError(Call call, Exception e) {
                dialog.cancel();
                ToastUtil.showNetError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(List<UserDto> response) {
                dialog.cancel();
                data.addAll(response);
                initUserOverlay();
                setMarker();
            }
        });

    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_nearby_msg;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        ButterKnife.unbind(this);
    }
}
