package com.waibao.team.tuyou.main_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.PersonPageActivity;
import com.waibao.team.tuyou.activity.RecommendFoodActivity;
import com.waibao.team.tuyou.activity.RecommendFriendsActivity;
import com.waibao.team.tuyou.activity.RecommendMsgActivity;
import com.waibao.team.tuyou.activity.RecommendTuanActivity;
import com.waibao.team.tuyou.activity.TrainsQueryActivity;
import com.waibao.team.tuyou.activity.TripGroupActivity;
import com.waibao.team.tuyou.adapter.FlashAdapter;
import com.waibao.team.tuyou.adapter.HomeRvPeopleAdapter;
import com.waibao.team.tuyou.adapter.HomeRvTuanAdapter;
import com.waibao.team.tuyou.callback.HomeGroupCallback;
import com.waibao.team.tuyou.callback.HomePeopleCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.database.SharedPreferenceData;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.event.LoginEvent;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.DisplayUtil;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.WrapScrollRview;
import com.waibao.team.tuyou.widget.pageIndicator.LinePageIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import okhttp3.Call;
import okhttp3.Request;


/**
 * Created by Azusa on 2016/5/6.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.tuijian_tuan_rv)
    WrapScrollRview tuijian_tuan_rv;
    @Bind(R.id.tuijian_people_rv)
    WrapScrollRview tuijianPeopleRv;
    @Bind(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.flash_view)
    AutoScrollViewPager flashView;
    @Bind(R.id.Home_scrollView)
    NestedScrollView scrollView;
    @Bind(R.id.indicator)
    LinePageIndicator indicator;
    @Bind(R.id.btn_hotel)
    TextView btnHotel;
    @Bind(R.id.btn_traintickets)
    TextView btnTraintickets;
    @Bind(R.id.btn_mentickets)
    TextView btnMentickets;
    @Bind(R.id.btn_airtickets)
    TextView btnAirtickets;
    @Bind(R.id.seemore_tuan)
    RelativeLayout seemoreTuan;
    @Bind(R.id.seemore_people)
    RelativeLayout seemorePeople;

    private View view;
    private List<GroupDto> datas = new ArrayList<>();
    private List<UserDto> datas2 = new ArrayList<>();
    private HomeRvPeopleAdapter adapter2;
    private HomeRvTuanAdapter adapter;

    private int ScrollY;

    private MyHandler mHandler;

    @OnClick({R.id.btn_foods, R.id.btn_hotel, R.id.btn_traintickets, R.id.btn_mentickets, R.id.btn_airtickets, R.id.seemore_tuan, R.id.seemore_people})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_hotel:
                intent = new Intent(getActivity(), RecommendMsgActivity.class);
                intent.putExtra("what", "hotel");
                startActivity(intent);
                break;
            case R.id.btn_traintickets:
                intent = new Intent(getActivity(), TrainsQueryActivity.class);
                intent.putExtra("what", 1);
                startActivity(intent);
                break;
            case R.id.btn_mentickets:
                intent = new Intent(getActivity(), RecommendMsgActivity.class);
                intent.putExtra("what", "tourist");
                startActivity(intent);
                break;
            case R.id.btn_airtickets:
                intent = new Intent(getActivity(), TrainsQueryActivity.class);
                intent.putExtra("what", 2);
                startActivity(intent);
                break;
            case R.id.seemore_tuan:
                intent = new Intent(getActivity(), RecommendTuanActivity.class);
                startActivity(intent);
                break;
            case R.id.seemore_people:
                intent = new Intent(getActivity(), RecommendFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_foods:
                intent = new Intent(getActivity(), RecommendFoodActivity.class);
                startActivity(intent);
                break;
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<HomeFragment> fragmentWeakReference;

        public MyHandler(HomeFragment homeFragment) {
            fragmentWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            HomeFragment homeFragment = fragmentWeakReference.get();
            if (homeFragment != null) {
                if (msg.what == 1) {
//                    for (int i = 0; i < 6; i++) {
//                        homeFragment.datas.add("------" + i);
//                    }
                } else {
                    homeFragment.mSwipeRefresh.setRefreshing(false);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mHandler = new MyHandler(this);
        initView();
        return view;
    }

    private void requestDatas(int pos) {
        switch (pos) {
            case 1:
                OkHttpUtils.get().url(Config.IP + "/recommend_groupRecommend")
                        .build().execute(new HomeGroupCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.showToast("网络访问出错11111111");
                        mHandler.sendEmptyMessage(2);
                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
//                        Log.e("LLLLL", "onBefore: " + request.url().toString());
                    }

                    @Override
                    public void onResponse(List<GroupDto> response) {
                        if (null != response) {
                            datas.clear();
                            datas.addAll(response);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            case 2:
                OkHttpUtils.get().url(Config.IP + "/recommend_peopleRecommend")
                        .build().execute(new HomePeopleCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.showToast("网络访问出错2");
                        mHandler.sendEmptyMessage(2);
                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        Log.e("LLLLL", "onBefore: " + request.url().toString());
                    }

                    @Override
                    public void onResponse(List<UserDto> response) {
                        if (null != response) {
                            datas2.clear();
                            datas2.addAll(response);
                            adapter2.notifyDataSetChanged();
                        }
                        mHandler.sendEmptyMessage(2);
                    }
                });
                break;
        }
    }

    private void initView() {
        //设置下拉刷新颜色和下拉监听
        mSwipeRefresh.setColorSchemeResources(R.color.green1, R.color.green2, R.color.green3);
        mSwipeRefresh.setOnRefreshListener(this);

        List<String> flashdatas = new ArrayList<String>();
        for (int i = 0; i < 6; i++) {
            flashdatas.add("------" + i);
        }

        FlashAdapter flashadapter = new FlashAdapter(flashdatas, getContext());
        flashView.setAdapter(flashadapter);
        flashView.setInterval(2000);
        flashView.setBorderAnimation(true);
        flashView.startAutoScroll(2000);
        flashView.setScrollDurationFactor(2);
        indicator.setViewPager(flashView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        tuijian_tuan_rv.setLayoutManager(layoutManager);
        tuijianPeopleRv.setLayoutManager(layoutManager2);

        adapter2 = new HomeRvPeopleAdapter(datas2);
        adapter = new HomeRvTuanAdapter(datas);
        tuijian_tuan_rv.setAdapter(adapter);
        tuijianPeopleRv.setAdapter(adapter2);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(getActivity(), TripGroupActivity.class);
                intent.putExtra("dto", JsonUtils.getJsonStringformat(datas.get(Position)));
                startActivity(intent);
            }
        });
        adapter2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(getActivity(), PersonPageActivity.class);
                intent.putExtra("uId", datas2.get(Position).getId());
                startActivity(intent);
            }
        });

        scrollView.requestChildFocus(flashView, null);
    }

    // 自动下拉刷新
    public void autorefresh() {
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });

        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (null != scrollView) {
                scrollView.smoothScrollTo(0, ScrollY);
            }
        } else {
            ScrollY = null != scrollView ? scrollView.getScrollY() : (int) DisplayUtil.dip2px(1);
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autorefresh();
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                int a = (int) DisplayUtil.dip2px(1);
                scrollView.smoothScrollTo(0, a);
            }
        });
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        requestDatas(1);
        requestDatas(2);
    }
}
