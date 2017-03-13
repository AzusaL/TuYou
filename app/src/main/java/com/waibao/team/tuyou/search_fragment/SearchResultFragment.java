package com.waibao.team.tuyou.search_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.PersonPageActivity;
import com.waibao.team.tuyou.activity.SearchDetailActivity;
import com.waibao.team.tuyou.activity.TripGroupActivity;
import com.waibao.team.tuyou.adapter.SearchFriendsLvAdapter;
import com.waibao.team.tuyou.adapter.SearchJournalsLvAdapter;
import com.waibao.team.tuyou.adapter.SearchTripGroupsLvAdapter;
import com.waibao.team.tuyou.callback.SearchResultCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.JournalDto;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.WrapHeightListView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Azusa on 2016/5/6.
 */
public class SearchResultFragment extends Fragment {
    @Bind(R.id.lv_friend)
    WrapHeightListView mLvFriend;
    @Bind(R.id.rl_morefriend)
    RelativeLayout mRlMorefriend;
    @Bind(R.id.lv_tuan)
    WrapHeightListView mLvTuan;
    @Bind(R.id.rl_moretuan)
    RelativeLayout mRlMoretuan;
    @Bind(R.id.lv_journals)
    WrapHeightListView mLvJournals;
    @Bind(R.id.rl_morejournals)
    RelativeLayout mRlMorejournals;
    @Bind(R.id.tv_moreuser)
    TextView tvMoreuser;
    @Bind(R.id.tv_moretuan)
    TextView tvMoretuan;
    @Bind(R.id.tv_morejournals)
    TextView tvMorejournals;
    private View view;
    private String key;  //搜索关键字
    private AlertDialog dialog;
    private String TAG = "SearchResultFragment";
    private boolean isCabcelReq = false;

    private List<User> friendsData = new ArrayList<>();
    private List<JournalDto> journalsData = new ArrayList<>();
    private List<GroupDto> groupsData = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_result, container, false);
        ButterKnife.bind(this, view);

        getData();
        setListener();
        return view;
    }

    private void setListener() {
        mLvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PersonPageActivity.class);
                intent.putExtra("uId",friendsData.get(position).getId());
                startActivity(intent);
            }
        });

        mLvTuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),TripGroupActivity.class);
                intent.putExtra("dto", JsonUtils.getJsonStringformat(groupsData.get(position)));
                startActivity(intent);
            }
        });

    }

    private void getData() {
        dialog = new MyDialog().showLodingDialog(getActivity());
        key = getArguments().getString("key");

        OkHttpUtils.get().url(Config.IP + "/recommend_searchAll")
                .addParams("name", key).tag(TAG)
                .build().execute(new SearchResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCabcelReq) {
                    return;
                }
                dialog.cancel();
                ToastUtil.showNetError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: "+request.url().toString());
            }

            @Override
            public void onResponse(HashMap<String, Object> response) {
                friendsData = (List<User>) response.get("user");
                journalsData = (List<JournalDto>) response.get("journal");
                groupsData = (List<GroupDto>) response.get("group");


                if (friendsData.isEmpty()) {
                    tvMoreuser.setText("暂无相关用户");
                } else {
                    mLvFriend.setAdapter(new SearchFriendsLvAdapter(friendsData, SearchResultFragment.this));
                }

                if (journalsData.isEmpty()) {
                    tvMorejournals.setText("暂无相关的游记");
                } else {
                    mLvJournals.setAdapter(new SearchJournalsLvAdapter(journalsData, getActivity()));
                }

                if (groupsData.isEmpty()) {
                    tvMorejournals.setText("暂无相关的行程");
                } else {
                    mLvTuan.setAdapter(new SearchTripGroupsLvAdapter(groupsData, getActivity()));
                }

                dialog.cancel();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.rl_morefriend, R.id.rl_moretuan, R.id.rl_morejournals})
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), SearchDetailActivity.class);
        intent.putExtra("key", key);
        switch (view.getId()) {
            case R.id.rl_morefriend:
                intent.putExtra("type", 0);
                break;
            case R.id.rl_morejournals:
                intent.putExtra("type", 1);
                break;
            case R.id.rl_moretuan:
                intent.putExtra("type", 2);
                break;
        }
        startActivity(intent);
    }
}
