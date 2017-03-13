package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.RecentSearchRvAdapter;
import com.waibao.team.tuyou.adapter.RecommendFoodRvAdapter;
import com.waibao.team.tuyou.callback.RecommendFoodsCallback;
import com.waibao.team.tuyou.dto.FoodDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class RecommendFoodActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerview)
    MyRecyclerView mRecyclerview;
    @Bind(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefresh;

    private int page = 1;
    private List<FoodDto> datas = new ArrayList<>();
    private RecommendFoodRvAdapter mAdapter;
    private String TAG = "RecommendFoodActivity";
    private boolean isCancelReq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        new ToolBarBuilder(this,mToolbar).setTitle("附近美食").build();
        //设置下拉刷新颜色和下拉监听
        mSwipeRefresh.setColorSchemeResources(R.color.green1, R.color.green2, R.color.green3);
        mSwipeRefresh.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);

        mAdapter = new RecommendFoodRvAdapter(datas, this);
        mRecyclerview.setAdapter(mAdapter);

        mRecyclerview.setLoadingMoreEnabled(true);
        mRecyclerview.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                getData(2);
            }
        });

        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(RecommendFoodActivity.this, RoutePlanActivity.class);
                intent.putExtra("lat", Double.valueOf(datas.get(Position).getLatitude()));
                intent.putExtra("lng", Double.valueOf(datas.get(Position).getLongitude()));
                startActivity(intent);
            }
        });
        autorefresh();
    }

    private void getData(final int what) {
        OkHttpUtils.get().url("http://apis.juhe.cn/catering/query")
                .addParams("key", "5026794a478307386c8111b88ce58802")
                .addParams("lng", ConstanceUtils.LOCATION_LONGITUDE + "")
                .addParams("lat", ConstanceUtils.LOCATION_LATITUDE + "")
                .addParams("page", page + "")
                .addParams("dtype", "json")
                .tag(TAG).build()
                .execute(new RecommendFoodsCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        if (isCancelReq) {
                            return;
                        }
                        mSwipeRefresh.setRefreshing(false);
                        mRecyclerview.noMoreLoading();
                    }

                    @Override
                    public void onResponse(List<FoodDto> response) {
                        if (response == null || response.isEmpty()) {
                            if (what == 1) {
                                mSwipeRefresh.setRefreshing(false);
                                mRecyclerview.noMoreLoading();
                                return;
                            }
                        }
                        page++;
                        if (what == 1) {
                            mSwipeRefresh.setRefreshing(false);
                            datas.clear();
                        }
                        datas.addAll(response);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerview.loadMoreComplete();
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_food;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
    public void onRefresh() {
        page = 1;
        getData(1);
    }
}
