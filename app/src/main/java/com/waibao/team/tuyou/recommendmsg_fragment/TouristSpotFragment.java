package com.waibao.team.tuyou.recommendmsg_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.TouristSpotRvAdapter;
import com.waibao.team.tuyou.callback.TouristSpotMsgCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.TouristSpotDto;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.widget.MyDialog;
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
public class TouristSpotFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.holtels_recyclerview)
    MyRecyclerView mRecyclerview;
    @Bind(R.id.holtels_swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefresh;
    private View view;
    private TouristSpotRvAdapter adapter;
    private List<TouristSpotDto> datas;
    private String cityKey;  //城市
    private String cId;   //城市id
    private String pId;   //省份id

    private int page = 1;
    private AlertDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                page = 1;
                getData(-1);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hotel, container, false);
        ButterKnife.bind(this, view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        dialog = new MyDialog().showLodingDialog(getActivity());
        cityKey = getArguments().getString("city");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ConstanceUtils.cityDtos.size(); i++) {
                    if (cityKey.equals(ConstanceUtils.cityDtos.get(i).getCityName())) {
                        cId = ConstanceUtils.cityDtos.get(i).getCityId();
                        pId = ConstanceUtils.cityDtos.get(i).getProvinceId();
                        break;
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void getData(final int what) {
        OkHttpUtils.get().url("http://apis.haoservice.com/lifeservice/travel/scenery")
                .addParams("key", Config.TouristKey)
                .addParams("pid", pId)
                .addParams("cid", cId)
                .addParams("page", page + "")
                .build().execute(new TouristSpotMsgCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: "+request.url().toString());
            }

            @Override
            public void onResponse(List<TouristSpotDto> response) {
                page++;
                if (0 == what) {
                    datas.clear();
                    datas.addAll(response);
                    adapter.notifyDataSetChanged();
                    mSwipeRefresh.setRefreshing(false);
                    return;
                }
                if (-1 == what) {
                    dialog.cancel();
                    datas.addAll(response);
                    adapter.notifyDataSetChanged();
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
        adapter = new TouristSpotRvAdapter(datas, this);
        mRecyclerview.setAdapter(adapter);

        mRecyclerview.setLoadingMoreEnabled(true);
        mRecyclerview.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                getData(1);
            }
        });
    }

    @Override
    public void onRefresh() {
        mRecyclerview.setNoMore(false);
        page = 1;
        getData(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
