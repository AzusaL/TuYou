package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.DisplayUtil;
import com.waibao.team.tuyou.utils.overlayutil.BikingRouteOverlay;
import com.waibao.team.tuyou.utils.overlayutil.DrivingRouteOverlay;
import com.waibao.team.tuyou.utils.overlayutil.OverlayManager;
import com.waibao.team.tuyou.utils.overlayutil.TransitRouteOverlay;
import com.waibao.team.tuyou.utils.overlayutil.WalkingRouteOverlay;
import com.waibao.team.tuyou.widget.ToolBarBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoutePlanActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.view_car)
    View viewCar;
    @Bind(R.id.rl_car)
    RelativeLayout rlCar;
    @Bind(R.id.view_bus)
    View viewBus;
    @Bind(R.id.rl_bus)
    RelativeLayout rlBus;
    @Bind(R.id.view_walk)
    View viewWalk;
    @Bind(R.id.rl_walk)
    RelativeLayout rlWalk;
    @Bind(R.id.map)
    MapView mapView;
    @Bind(R.id.view_biking)
    View viewBiking;
    @Bind(R.id.rl_biking)
    RelativeLayout mRlBiking;
    @Bind(R.id.tv_distance)
    TextView mTvDistance;
    @Bind(R.id.tv_duration)
    TextView mTvDuration;
    private List<View> views = new ArrayList<>();
    private int currentPosition = 0;

    private BaiduMap mBaidumap = null;
    private OverlayManager routeOverlay = null;

    // 搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private PlanNode stNode, enNode;
    private String city = ConstanceUtils.LOCATION_CITY;

    private View star, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        new ToolBarBuilder(this, toolbar).setCanBack(false).setTitle("").build();
        double lat, lng;
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", -1);
        lng = intent.getDoubleExtra("lng", -1);
        mBaidumap = mapView.getMap();
        mBaidumap.setTrafficEnabled(true);
        stNode = PlanNode.withLocation(new LatLng(ConstanceUtils.LOCATION_LATITUDE, ConstanceUtils.LOCATION_LONGITUDE));
        enNode = PlanNode.withLocation(new LatLng(lat, lng));
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));

        views.add(viewCar);
        views.add(viewBus);
        views.add(viewWalk);
        views.add(viewBiking);

        star = View.inflate(ConstanceUtils.CONTEXT, R.layout.view_start_address, null);
        end = View.inflate(ConstanceUtils.CONTEXT, R.layout.view_end_address, null);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_hotel_detail;
    }

    @OnClick({R.id.rl_biking, R.id.btn_back, R.id.rl_car, R.id.rl_bus, R.id.rl_walk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.rl_car:
                setLayoutParams(0);
                mBaidumap.clear();
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode).to(enNode));
                break;
            case R.id.rl_bus:
                setLayoutParams(1);
                mBaidumap.clear();
                mSearch.transitSearch((new TransitRoutePlanOption())
                        .from(stNode).city(city).to(enNode));
                break;
            case R.id.rl_walk:
                setLayoutParams(2);
                mBaidumap.clear();
                mSearch.walkingSearch((new WalkingRoutePlanOption())
                        .from(stNode).to(enNode));
                break;
            case R.id.rl_biking:
                setLayoutParams(3);
                mBaidumap.clear();
                mSearch.bikingSearch((new BikingRoutePlanOption())
                        .from(stNode).to(enNode));
                break;
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            WalkingRouteLine routeLine = result.getRouteLines().get(0);
            overlay.setData(routeLine);
            overlay.addToMap();
            overlay.zoomToSpan();
            routeOverlay.addToMap();
            mTvDistance.setText("距离我" + routeLine.getDistance() / 1000 + "KM");
            mTvDuration.setText("耗时" + getTimeDuration(routeLine.getDuration()));
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
            mBaidumap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            TransitRouteLine routeLine = result.getRouteLines().get(0);
            overlay.setData(routeLine);
            overlay.addToMap();
            overlay.zoomToSpan();

            mTvDistance.setText("距离我" + routeLine.getDistance() / 1000 + "KM");
            mTvDuration.setText("耗时" + getTimeDuration(routeLine.getDuration()));
        }
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            DrivingRouteLine routeLine = result.getRouteLines().get(0);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            mTvDistance.setText("距离我" + routeLine.getDistance() / 1000 + "KM");
            mTvDuration.setText("耗时" + getTimeDuration(routeLine.getDuration()));
        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RoutePlanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaidumap);
            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            BikingRouteLine routeLine = bikingRouteResult.getRouteLines().get(0);
            overlay.setData(bikingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            mTvDistance.setText("距离我" + routeLine.getDistance() / 1000 + "KM");
            mTvDuration.setText("耗时" + getTimeDuration(routeLine.getDuration()));
        }
    }

    // 定制RouteOverly
    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromView(star);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromView(end);
        }
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromView(star);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromView(end);
        }
    }

    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromView(star);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromView(end);
        }
    }

    private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromView(star);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromView(end);
        }
    }

    private String getTimeDuration(int time) {
        String duration = "";
        if (time <= 60 * 60) {
            duration = (time / 60) + "分钟";
        } else {
            int h = time / 3600;
            int m = time % 3600;
            Log.e("main", "getTimeDuration: " + m);
            duration = h + "小时";
            if (m > 60) {
                duration = duration + m / 60 + "分钟";
            }
        }
        return duration;
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mapView.onDestroy();
        super.onDestroy();
    }

    private void setLayoutParams(int position) {
        int big = (int) DisplayUtil.dip2px(32);
        int small = (int) DisplayUtil.dip2px(20);

        if (position == currentPosition) {
            return;
        }

        ViewGroup.LayoutParams layoutParamsbig = views.get(position).getLayoutParams();
        layoutParamsbig.width = big;
        layoutParamsbig.height = big;
        views.get(position).setLayoutParams(layoutParamsbig);

        ViewGroup.LayoutParams layoutParamssmall = views.get(currentPosition).getLayoutParams();
        layoutParamssmall.width = small;
        layoutParamssmall.height = small;
        views.get(currentPosition).setLayoutParams(layoutParamssmall);

        currentPosition = position;
    }
}
