package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.waibao.team.tuyou.dto.FriendDto;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.FriendsRvAdapter;
import com.waibao.team.tuyou.callback.FriendsCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
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

public class MyFriendsActivity extends BaseActivity {

    @Bind(R.id.Friends_toolbar)
    Toolbar FriendsToolbar;
    @Bind(R.id.myFriends_list)
    RecyclerView myFriends;

    private List<FriendDto> datas = new ArrayList<>();
    private FriendsRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        ToolBarBuilder builder = new ToolBarBuilder(this, FriendsToolbar);
        builder.setTitle("我的好友").setCanBack(true).build();
        initView();
        initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_friends;
    }

    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myFriends.setLayoutManager(layoutManager);

        adapter = new FriendsRvAdapter(datas);
        myFriends.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(MyFriendsActivity.this, ConversationActivity.class);
                intent.putExtra("userLoginName", datas.get(Position).getLoginName());
                intent.putExtra("userName", datas.get(Position).getNickname());
                intent.putExtra("img_head",datas.get(Position).getImgUrl());
                intent.putExtra("uImgUrl",datas.get(Position).getImgUrl());
                startActivity(intent);
            }
        });
    }

    private void initData() {
//        if (UserUtil.user.getLoginName().equals("123456")) {
//            datas.add("654321");
//            datas.add("110110");
//            datas.add("120120");
//        } else if (UserUtil.user.getLoginName().equals("654321")) {
//            datas.add("123456");
//            datas.add("110110");
//            datas.add("120120");
//        } else if (UserUtil.user.getLoginName().equals("110110")) {
//            datas.add("123456");
//            datas.add("654321");
//            datas.add("120120");
//        } else if (UserUtil.user.getLoginName().equals("120120")) {
//            datas.add("123456");
//            datas.add("654321");
//            datas.add("110110");
//        }
        OkHttpUtils.get().url(Config.IP + "/friend_getAllFriend")
                .addParams("uid", UserUtil.user.getId())
                .addParams("firstResult", "0")
                .build().execute(new FriendsCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("连接出错有问题");
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("LLLLL", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(List<FriendDto> response) {
                datas.addAll(response);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
