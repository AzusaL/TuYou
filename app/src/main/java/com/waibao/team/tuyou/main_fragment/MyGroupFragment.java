package com.waibao.team.tuyou.main_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.LoginActivity;
import com.waibao.team.tuyou.activity.TripGroupActivity;
import com.waibao.team.tuyou.adapter.MyGroupRvAdapter;
import com.waibao.team.tuyou.callback.HomeGroupCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.event.LoginEvent;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Azusa on 2016/5/6.
 */
public class MyGroupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.grounps_recyclerview)
    MyRecyclerView recyclerview;
    @Bind(R.id.grounps_swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefresh;
    private View view;
    private List<GroupDto> datas;
    private MyGroupRvAdapter adapter;
    private boolean isCancelReq = false;
    private boolean isInited = false;
    private boolean isGotoLogin = false;
    private String TAG = "MyGroupFragment";
    private int what; //what=1是首页自己的团页面，=2是自己收藏的团页面
    private String url;
    private String uId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mygrounps, container, false);
        what = getArguments().getInt("what");
        String isNeedInit = getArguments().getString("isNeed");
        if (what == 1) {
            url = "/user_getAllMyGroup";
            uId = getArguments().getString("uId");
            if (!StringUtil.isEmpty(isNeedInit)) {
                initView();
            }
        } else {
            url = "/user_getCollection";
            uId = UserUtil.user.getId();
            initView();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        what = getArguments().getInt("what");
        if (isVisibleToUser && !isInited && what == 1) {
            if (!UserUtil.userisLogin) {
                isGotoLogin = true;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                return;
            }
            uId = UserUtil.user.getId();
            initView();
            isInited = true;
        }
    }

    @Subscribe
    public void onEvent_Login(LoginEvent loginEvent) {
        if (loginEvent.isLogin()) {
            datas.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGotoLogin) {
            initView();
            isInited = true;
            isGotoLogin = false;
        }
        if (what == 1 && isInited) {
            uId = UserUtil.user.getId();
            if (uId == null) {
                datas.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        ButterKnife.bind(this, view);
        //设置下拉刷新颜色和下拉监听
        mSwipeRefresh.setColorSchemeResources(R.color.green1, R.color.green2, R.color.green3);
        mSwipeRefresh.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        datas = new ArrayList<>();
        adapter = new MyGroupRvAdapter(datas, this);
        recyclerview.setAdapter(adapter);

        recyclerview.setLoadingMoreEnabled(true);
        recyclerview.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                getAllData(datas.size(), 1);
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(getActivity(), TripGroupActivity.class);
                intent.putExtra("dto", JsonUtils.getJsonStringformat(datas.get(Position)));
                startActivity(intent);
            }
        });
        autorefresh();
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

    //获得自己参加的所有团
    private void getAllData(int firstresult, final int what) {
        OkHttpUtils.get().url(Config.IP + url)
                .addParams("uid", uId)
                .addParams("firstResult", firstresult + "")
                .addParams("type", "1")
                .build().execute(new HomeGroupCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                if (what == 0) {
                    mSwipeRefresh.setRefreshing(false);
                } else {
                    recyclerview.setNoMore(true);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(List<GroupDto> response) {
                if (response == null || response.isEmpty()) {
                    if (what == 0) {
                        datas.clear();
                        mSwipeRefresh.setRefreshing(false);
                    } else {
                        recyclerview.setNoMore(true);
                    }
                    adapter.notifyDataSetChanged();
                    return;
                }

                if (what == 0) {
                    datas.clear();
                    mSwipeRefresh.setRefreshing(false);
                }
                datas.addAll(response);
                adapter.notifyDataSetChanged();
                recyclerview.loadMoreComplete();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        isCancelReq = true;
        OkHttpUtils.getInstance().cancelTag(TAG);
    }

    @Override
    public void onRefresh() {
        recyclerview.setNoMore(false);
        getAllData(0, 0);
    }
}
