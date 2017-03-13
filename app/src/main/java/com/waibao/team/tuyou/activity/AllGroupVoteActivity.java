package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.AllVoteRvAdapter;
import com.waibao.team.tuyou.callback.AllVoteCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.VoteDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

public class AllGroupVoteActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.vote_rv)
    RecyclerView voteRv;

    private List<VoteDto> datas = new ArrayList<>();
    private AllVoteRvAdapter adapter = new AllVoteRvAdapter(datas);
    private String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        gid = getIntent().getStringExtra("gid");
        initView();
        initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_all_group_vote;
    }

    private void initView() {
        ToolBarBuilder builder = new ToolBarBuilder(this, toolbar);
        builder.setTitle("所有投票").build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        voteRv.setLayoutManager(layoutManager);
        voteRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(AllGroupVoteActivity.this, VoteActivity.class);
                intent.putExtra("VoteDto", JsonUtils.getJsonStringformat(datas.get(Position)));
                startActivityForResult(intent, 200);
            }
        });
    }

    private void initData() {
        OkHttpUtils.get().url(Config.IP + "/group_getVoteDtoList")
                .addParams("gid", gid)
                .build().execute(new AllVoteCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络访问出错");
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("LLLLL", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(List<VoteDto> response) {
                if (null != response) {
                    datas.clear();
                    datas.addAll(response);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick(R.id.fabBtn)
    public void onClick() {
        Intent intent = new Intent(this, NewVoteActivity.class);
        intent.putExtra("gid", gid);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Log.e("LLLLL", "onActivityResult: 新建重载数据");
            initData();
        } else if (requestCode == 200) {
            Log.e("LLLLL", "onActivityResult: 投票重载数据");
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
