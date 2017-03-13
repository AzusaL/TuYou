package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.TrainsMsgRvAdapter;
import com.waibao.team.tuyou.adapter.TripgroupFriendsRvAdapter;
import com.waibao.team.tuyou.callback.TrainListCallback;
import com.waibao.team.tuyou.dto.TrainListBean;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

public class TrainQueryResultActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recyclerview)
    MyRecyclerView mRecyclerview;

    private String starAddress;
    private String endAddress;
    private String time;

    private List<TrainListBean> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initData();
        initView();

    }

    private void initView() {
        new ToolBarBuilder(this, mToolbar).setTitle(starAddress + "-" + endAddress+"  "+time).build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(layoutManager);

        getData();

    }

    private void getData() {
        OkHttpUtils.get().url("http://apis.baidu.com/qunar/qunar_train_service/s2ssearch")
                .addHeader("apikey", "ebaf8794972ef730bdf4d83fda947140")
                .addParams("version", "1.0")
                .addParams("from", starAddress)
                .addParams("to", endAddress)
                .addParams("date", time)
                .build().execute(new TrainListCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("服务器出错");
            }

            @Override
            public void onResponse(List<TrainListBean> response) {
                datas.addAll(response);
                mRecyclerview.setAdapter(new TrainsMsgRvAdapter(datas));
            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        starAddress = intent.getStringExtra("starAddress");
        endAddress = intent.getStringExtra("endAddress");
        time = intent.getStringExtra("time");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_train_query_result;
    }
}
