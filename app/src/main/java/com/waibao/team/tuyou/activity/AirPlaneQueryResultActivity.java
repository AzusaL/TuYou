package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.AirPlanesRvAdapter;
import com.waibao.team.tuyou.adapter.TrainsMsgRvAdapter;
import com.waibao.team.tuyou.callback.AirPlanesCallback;
import com.waibao.team.tuyou.callback.TrainListCallback;
import com.waibao.team.tuyou.dto.AirPlaneDto;
import com.waibao.team.tuyou.dto.TrainListBean;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

public class AirPlaneQueryResultActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerview)
    MyRecyclerView mRecyclerview;

    private String starAddress;
    private String endAddress;
    private String time;
    private AlertDialog mDialog;
    private int page = 1;
    private AirPlanesRvAdapter mRvAdapter;

    private List<AirPlaneDto> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mDialog = new MyDialog().showLodingDialog(this);
        initData();
        initView();

    }

    private void initView() {
        new ToolBarBuilder(this, mToolbar).setTitle(time).build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);
        mRvAdapter = new AirPlanesRvAdapter(datas);
        mRecyclerview.setAdapter(mRvAdapter);

        if (StringUtil.isEmpty(starAddress) || StringUtil.isEmpty(endAddress)) {
            ToastUtil.showToast("未搜到相应的航线");
            mDialog.cancel();
        } else {
            getData();
        }

        mRecyclerview.setLoadingMoreEnabled(true);
        mRecyclerview.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                getData();
            }
        });

    }

    private void getData() {
        OkHttpUtils.get().url("http://apis.haoservice.com/plan/InternationalFlightQueryByCity")
                .addParams("key", "06185804e980426088180904791f3aef")
                .addParams("dep", starAddress)
                .addParams("arr", endAddress)
                .addParams("date", time)
                .addParams("pageNum", page + "")
                .build().execute(new AirPlanesCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showNetError();
                if (page == 1) {
                    mDialog.cancel();
                } else {
                    mRecyclerview.loadMoreComplete();
                }
            }

            @Override
            public void onResponse(List<AirPlaneDto> response) {
                datas.addAll(response);
                mRvAdapter.notifyDataSetChanged();
                mRecyclerview.loadMoreComplete();
                if (page == 1) {
                    mDialog.cancel();
                }
                page++;
            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        String star = intent.getStringExtra("starAddress");
        String end = intent.getStringExtra("endAddress");


        for (int i = 0; i < ConstanceUtils.airCitys.length; i++) {
            String s = ConstanceUtils.airCitys[i];
            if (s.startsWith(star)) {
                starAddress = s.substring(s.indexOf(",") + 1, s.length());
            }
            if (s.startsWith(end)) {
                endAddress = s.substring(s.indexOf(",") + 1, s.length());
            }
        }
        time = intent.getStringExtra("time");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_train_query_result;
    }
}
