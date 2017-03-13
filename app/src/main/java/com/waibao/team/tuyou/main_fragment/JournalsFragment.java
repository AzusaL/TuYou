package com.waibao.team.tuyou.main_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.TravelsRvAdapter;
import com.waibao.team.tuyou.callback.JournalsCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.JournalDto;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Azusa on 2016/5/6.
 */
public class JournalsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.travels_recyclerview)
    MyRecyclerView recyclerview;
    @Bind(R.id.travels_swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefresh;

    private String TAG = "JournalsFragment";
    private boolean isCancelReq = false;
    private View view;
    private List<JournalDto> datas;
    private TravelsRvAdapter mRvAdapter;
    private int what;  //what=1显示平台所有游记，=2显示用户自己的所有游记,=3显示团内所有用户的游记,=4显示用户自己收藏的游记
    private boolean isInited = false;
    private String uId = ""; //用户ID
    private String gId = ""; //用户ID


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_journals, container, false);
        ButterKnife.bind(this, view);
        what = getArguments().getInt("what");
        if (what == 2) {
            uId = getArguments().getString("uId");
            initView();
        } else if (what == 3) {
            gId = getArguments().getString("gId");
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInited) {
            initView();
            isInited = true;
        }
    }

    private void initView() {
        //设置下拉刷新颜色和下拉监听
        mSwipeRefresh.setColorSchemeResources(R.color.green1, R.color.green2, R.color.green3);
        mSwipeRefresh.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        datas = new ArrayList<>();
        mRvAdapter = new TravelsRvAdapter(datas, getActivity());
        recyclerview.setAdapter(mRvAdapter);

        recyclerview.setLoadingMoreEnabled(true);
        recyclerview.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                if (what == 3) {
                    recyclerview.noMoreLoading();
                    return;
                } else if (what == 2) {
                    getJustUsersData(datas.size(), 1);
                } else {
                    getAllData(datas.size(), 1);
                }
            }
        });

        autorefresh();
    }

    //获取所有用户的游记
    private void getAllData(int firstresult, final int what) {
        OkHttpUtils.get().url(Config.IP + "/recommend_getAppAllJournal")
                .addParams("firstResult", firstresult + "")
                .tag(TAG)
                .build().execute(new JournalsCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                mSwipeRefresh.setRefreshing(false);
                recyclerview.noMoreLoading();
                ToastUtil.showNetError();
            }

            @Override
            public void onResponse(List<JournalDto> response) {
                if (response == null || response.isEmpty()) {
                    if (what == 0) {
                        mSwipeRefresh.setRefreshing(false);
                    }
                    recyclerview.noMoreLoading();
                    mRvAdapter.notifyDataSetChanged();
                    return;
                }
                if (what == 0) {
                    datas.clear();
                    mSwipeRefresh.setRefreshing(false);
                }
                datas.addAll(response);
                mRvAdapter.notifyDataSetChanged();
                recyclerview.loadMoreComplete();
            }
        });
    }


    //获取用户收藏的游记
    private void getUserCollecedData(int firstresult, final int what) {
        OkHttpUtils.get().url(Config.IP + "/user_getCollection")
                .addParams("firstResult", firstresult + "")
                .addParams("uid", UserUtil.user.getId())
                .addParams("type", "0")
                .tag(TAG)
                .build().execute(new JournalsCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                mSwipeRefresh.setRefreshing(false);
                recyclerview.noMoreLoading();
                ToastUtil.showNetError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: "+request.url().toString());
            }

            @Override
            public void onResponse(List<JournalDto> response) {
                if (response == null || response.isEmpty()) {
                    if (what == 0) {
                        mSwipeRefresh.setRefreshing(false);
                    }
                    recyclerview.noMoreLoading();
                    mRvAdapter.notifyDataSetChanged();
                    return;
                }
                if (what == 0) {
                    datas.clear();
                    mSwipeRefresh.setRefreshing(false);
                }
                datas.addAll(response);
                mRvAdapter.notifyDataSetChanged();
                recyclerview.loadMoreComplete();
            }
        });
    }

    //获取用户自己的游记
    public void getJustUsersData(int firstresult, final int what) {
        OkHttpUtils.get().url(Config.IP + "/user_getMyJournal")
                .addParams("uid", uId)
                .addParams("firstResult", firstresult + "")
                .tag(TAG)
                .build().execute(new JournalsCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                mSwipeRefresh.setRefreshing(false);
                recyclerview.noMoreLoading();
                ToastUtil.showNetError();
            }

            @Override
            public void onResponse(List<JournalDto> response) {
                if (response == null || response.isEmpty()) {
                    if (what == 0) {
                        mSwipeRefresh.setRefreshing(false);
                    }
                    recyclerview.noMoreLoading();
                    mRvAdapter.notifyDataSetChanged();
                    return;
                }
                if (what == 0) {
                    mSwipeRefresh.setRefreshing(false);
                    datas.clear();
                }
                datas.addAll(response);
                mRvAdapter.notifyDataSetChanged();
                recyclerview.loadMoreComplete();
            }
        });
    }

    //获取团内所有游记
    private void getGroupsData() {
        OkHttpUtils.get().url(Config.IP + "/group_getGroupAllJournal")
                .addParams("gid", gId)
                .tag(TAG)
                .build().execute(new JournalsCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                mSwipeRefresh.setRefreshing(false);
                recyclerview.noMoreLoading();
                ToastUtil.showNetError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(List<JournalDto> response) {
                if (response == null || response.isEmpty()) {
                    mSwipeRefresh.setRefreshing(false);
                    recyclerview.loadMoreComplete();
                    return;
                }
                mSwipeRefresh.setRefreshing(false);
                datas.clear();
                datas.addAll(response);
                recyclerview.loadMoreComplete();
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        recyclerview.setNoMore(false);
        if (what == 1) {
            getAllData(0, 0);
        } else if (what == 2) {
            getJustUsersData(0, 0);
        } else if (what == 3) {
            getGroupsData();
        } else {
            getUserCollecedData(0, 0);
        }
    }
}
