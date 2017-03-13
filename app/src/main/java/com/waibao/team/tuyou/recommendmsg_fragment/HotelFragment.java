package com.waibao.team.tuyou.recommendmsg_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.RoutePlanActivity;
import com.waibao.team.tuyou.adapter.HotelsRvAdapter;
import com.waibao.team.tuyou.callback.HotelMsgCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.HotelMsgDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Azusa on 2016/6/3.
 */
public class HotelFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.holtels_recyclerview)
    MyRecyclerView mRecyclerview;
    @Bind(R.id.holtels_swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefresh;
    private View view;
    private HotelsRvAdapter adapter;
    private List<HotelMsgDto> datas;
    private String cityKey;  //城市
    private String sortType;  //排序方式
    private String sortRule;  //排序规则（高到低或低到高）
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hotel, container, false);
        ButterKnife.bind(this, view);
        cityKey = getArguments().getString("city");
        sortType = getArguments().getString("sortType");
        if (sortType.equals("price")) {
            sortRule = "1";
        } else {
            sortRule = "0";
        }
        initView();
        autorefresh();
        return view;
    }

    private void getData(final int what) {
        OkHttpUtils.get().url("http://api.map.baidu.com/place/v2/search")
                .addParams("q", "酒店")
                .addParams("region", cityKey)
                .addParams("output", "json")
                .addParams("page_num", page + "")
                .addParams("ak", Config.BMAP_AK)
                .addParams("mcode", Config.BMAP_MCODE)
                .addParams("output", "json")
                .addParams("scope", "2")
                .addParams("filter", "industry_type:hotel|sort_name:" + sortType + "|sort_rule:"+sortRule)
                .build().execute(new HotelMsgCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(List<HotelMsgDto> response) {
                page++;
                if (0 == what) {
                    datas.clear();
                    datas.addAll(response);
                    adapter.notifyDataSetChanged();
                    mSwipeRefresh.setRefreshing(false);
                    return;
                }
                datas.addAll(response);
                adapter.notifyDataSetChanged();
                mRecyclerview.loadMoreComplete();
            }
        });

    }

    private void initView() {
        mSwipeRefresh.setColorSchemeResources(R.color.green1, R.color.green2, R.color.green3);
        mSwipeRefresh.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);

        datas = new ArrayList<>();
        adapter = new HotelsRvAdapter(datas, this);
        mRecyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(getActivity(), RoutePlanActivity.class);
                intent.putExtra("lat", datas.get(Position).getLat());
                intent.putExtra("lng", datas.get(Position).getLng());
                startActivity(intent);
            }
        });

        mRecyclerview.setLoadingMoreEnabled(true);
        mRecyclerview.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                getData(1);
            }
        });
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
        mRecyclerview.setNoMore(false);
        page = 0;
        getData(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
