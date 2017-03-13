package com.waibao.team.tuyou.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.waibao.team.tuyou.dto.MessageDto;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.MessageRvAdapter;
import com.waibao.team.tuyou.callback.MessageCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

public class MyMessageActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.message_rv)
    RecyclerView messageRv;


    private List<MessageDto> msg_list = new ArrayList<>();
    private MessageRvAdapter adapter = new MessageRvAdapter(msg_list);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        ToolBarBuilder builder = new ToolBarBuilder(this, toolbar);
        builder.setTitle("消息列表").build();

        initView();
        initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_message;
    }

    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        messageRv.setLayoutManager(layoutManager);
        messageRv.setAdapter(adapter);
        messageRv.setItemAnimator(new DefaultItemAnimator());
    }

    public void initData() {
        OkHttpUtils.get().url(Config.IP + "/user_getAllMessage")
                .addParams("uid", UserUtil.user.getId())
                .build().execute(new MessageCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络连接出错");
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("LLLLL", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(List<MessageDto> response) {
                msg_list.clear();
                if (null != response) {
                    msg_list.addAll(response);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}
