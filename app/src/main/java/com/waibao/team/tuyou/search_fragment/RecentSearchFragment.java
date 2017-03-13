package com.waibao.team.tuyou.search_fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.RecentSearchRvAdapter;
import com.waibao.team.tuyou.database.SharedPreferenceData;
import com.waibao.team.tuyou.event.SearchEvent;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Azusa on 2016/5/6.
 */
public class RecentSearchFragment extends Fragment {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.fabBtn)
    FloatingActionButton fabBtn;
    private View view;
    private List<String> datas;
    private RecentSearchRvAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent_search, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        datas = SharedPreferenceData.getInstance().getSearchList(ConstanceUtils.CONTEXT);
        adapter = new RecentSearchRvAdapter(datas);
        recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                EventBus.getDefault().post(new SearchEvent(datas.get(Position)));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.fabBtn)
    public void onClick() {
        SharedPreferenceData.getInstance().clearSearchtext(ConstanceUtils.CONTEXT);
        datas.clear();
        adapter.notifyDataSetChanged();
    }
}
