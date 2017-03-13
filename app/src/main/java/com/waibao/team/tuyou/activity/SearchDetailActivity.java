package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.HomeRvTuanAdapter;
import com.waibao.team.tuyou.adapter.SearchFriendsRvAdapter;
import com.waibao.team.tuyou.adapter.TravelsRvAdapter;
import com.waibao.team.tuyou.callback.SearchResultCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.JournalDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class SearchDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    MyRecyclerView recyclerview;

    private String searchKey; //搜索的字符串
    private int searchType;  //搜索的类型

    private List<User> friendsData = new ArrayList<>();
    private List<JournalDto> journalsData = new ArrayList<>();
    private List<GroupDto> groupsData = new ArrayList<>();

    private SearchFriendsRvAdapter friendsAdapter;
    private TravelsRvAdapter journalsAdapter;
    private HomeRvTuanAdapter groupsAdapter;

    private AlertDialog dialog;
    private boolean isCancelReq = false;
    private String TAG = "SearchDetailActivity";
    private boolean isInited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();
        getData(0);
    }

    private void getData(int fistResult) {
        OkHttpUtils.get().url(Config.IP + "/recommend_searchOne")
                .addParams("type", searchType + "")
                .addParams("name", searchKey)
                .addParams("firstResult", fistResult + "")
                .tag(TAG)
                .build().execute(new SearchResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                if (!isInited) {
                    dialog.cancel();
                    isInited = true;
                }
                ToastUtil.showNetError();
            }

            @Override
            public void onResponse(HashMap<String, Object> response) {
                if (!isInited) {
                    dialog.cancel();
                    isInited = true;
                }
                switch (searchType) {
                    case 0:
                        List<User> users = (List<User>) response.get("user");
                        if (users.isEmpty()) {
                            recyclerview.setNoMore(true);
                            return;
                        }
                        friendsData.addAll(users);
                        friendsAdapter.notifyDataSetChanged();
                        recyclerview.loadMoreComplete();
                        break;
                    case 1:
                        List<JournalDto> journals = (List<JournalDto>) response.get("journal");
                        if (journals.isEmpty()) {
                            recyclerview.setNoMore(true);
                            return;
                        }
                        journalsData.addAll(journals);
                        journalsAdapter.notifyDataSetChanged();
                        recyclerview.loadMoreComplete();
                        break;
                    case 2:
                        List<GroupDto> groups = (List<GroupDto>) response.get("group");
                        if (groups.isEmpty()) {
                            recyclerview.setNoMore(true);
                            return;
                        }
                        groupsData.addAll(groups);
                        groupsAdapter.notifyDataSetChanged();
                        recyclerview.loadMoreComplete();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void initView() {
        Intent intent = getIntent();
        searchKey = intent.getStringExtra("key");
        searchType = intent.getIntExtra("type", -1);

        dialog = new MyDialog().showLodingDialog(this);
        new ToolBarBuilder(this, toolbar).setTitle(searchKey).build();

        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        recyclerview.setLoadingMoreEnabled(true);
        recyclerview.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                switch (searchType) {
                    case 0:
                        getData(friendsData.size());
                        break;
                    case 1:
                        getData(journalsData.size());
                        break;
                    case 2:
                        getData(groupsData.size());
                        break;
                    default:
                        break;
                }
            }
        });

        switch (searchType) {
            case 0:
                friendsAdapter = new SearchFriendsRvAdapter(friendsData);
                recyclerview.setAdapter(friendsAdapter);
                friendsAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View v, int Position) {
                        Intent intent = new Intent(SearchDetailActivity.this,PersonPageActivity.class);
                        intent.putExtra("uId",friendsData.get(Position).getId());
                        startActivity(intent);
                    }
                });
                break;
            case 1:
                journalsAdapter = new TravelsRvAdapter(journalsData, this);
                recyclerview.setAdapter(journalsAdapter);
                break;
            case 2:
                groupsAdapter = new HomeRvTuanAdapter(groupsData);
                recyclerview.setAdapter(groupsAdapter);

                groupsAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View v, int Position) {
                        Intent intent = new Intent(SearchDetailActivity.this,TripGroupActivity.class);
                        intent.putExtra("dto", JsonUtils.getJsonStringformat(groupsData.get(Position)));
                        startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
