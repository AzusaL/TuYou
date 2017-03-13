package com.waibao.team.tuyou.tripgrounp_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.SelectLabelGvAdapter;
import com.waibao.team.tuyou.adapter.TimeLineRvAdapter;
import com.waibao.team.tuyou.adapter.WaysImgGvAdapter;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.Label;
import com.waibao.team.tuyou.dto.PathWayDto;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.widget.WrapHeightGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Azusa on 2016/5/6.
 */
public class WaysFragment extends Fragment {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.tv_waytime)
    TextView mTvWaytime;
    @Bind(R.id.tv_groupintroction)
    TextView mTvGroupintroction;
    @Bind(R.id.gridview)
    WrapHeightGridView mGridview;
    @Bind(R.id.limi_gv)
    WrapHeightGridView limiGv;
    private View view;
    private GroupDto mGroupDto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tripgroup_ways, container, false);
        ButterKnife.bind(this, view);
        getData();
        initView();
        return view;
    }

    private void getData() {
        String json = getArguments().getString("dto");
        mGroupDto = JsonUtils.getObjectfromString(json, GroupDto.class);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager);

        List<String> ways = StringUtil.getList(mGroupDto.getWay());
        List<String> times = StringUtil.getList(mGroupDto.getWayTime());
        List<PathWayDto> datas = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            datas.add(new PathWayDto(ways.get(i), times.get(i)));
        }
        TimeLineRvAdapter adapter = new TimeLineRvAdapter(datas);
        recyclerview.setAdapter(adapter);

        //初始化图片Gv
        List<String> imgs = StringUtil.getList(mGroupDto.getImgUrl());

        WaysImgGvAdapter gvAdapter = new WaysImgGvAdapter(imgs);
        mGridview.setAdapter(gvAdapter);

        mTvGroupintroction.setText(mGroupDto.getDescription());

        mTvAddress.setText(ways.get(ways.size() - 1));

        mTvWaytime.setText(times.get(times.size() - 1).substring(0, 4));

        List<Label> limiDatas = new ArrayList<>();
        String sex;
        if(mGroupDto.getSexType().equals("0")){
            sex = "不限";
        }else if(mGroupDto.getSexType().equals("1")){
            sex = "女";
        }else {
            sex = "男";
        }
        limiDatas.add(new Label(("性别："+sex),false,1));
        limiDatas.add(new Label(("年龄："+mGroupDto.getAge()),false,1));
        limiDatas.add(new Label(("信誉："+mGroupDto.getCredit()+"+"),false,1));
        limiGv.setAdapter(new SelectLabelGvAdapter(limiDatas));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
