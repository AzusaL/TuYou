package com.waibao.team.tuyou.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.TabFragmentPagerAdapter;
import com.waibao.team.tuyou.main_fragment.JournalsFragment;
import com.waibao.team.tuyou.main_fragment.MyGroupFragment;
import com.waibao.team.tuyou.widget.TabItemView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyCollectionsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabs)
    TabLayout mTabs;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    private List<Fragment> fragmentsList;
    private List<String> titles;
    private List<Integer> imgsId;
    private TabItemView[] tabItem = new TabItemView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_my_collections;
    }

    private void initView() {
        new ToolBarBuilder(this, mToolbar).setElevation(0).setTitle("我的收藏").build();

        titles = new ArrayList<>();
        titles.add("行程");
        titles.add("游记");

        imgsId = new ArrayList<>();
        imgsId.add(R.drawable.ic_location_active);
        imgsId.add(R.drawable.ic_img_green);

        for (int i = 0; i < 2; i++) {
            tabItem[i] = new TabItemView(this, titles.get(i), imgsId.get(i), 0xff56bc7b, i);
            TabLayout.Tab tab = mTabs.newTab().setCustomView(tabItem[i]);
            mTabs.addTab(tab);
        }

        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewpager.setCurrentItem(tab.getPosition(), false);
                ((TabItemView) tab.getCustomView()).toggle(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TabItemView) tab.getCustomView()).toggle(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        fragmentsList = new ArrayList<>();
        Fragment fg3 = new JournalsFragment();
        Fragment fg2 = new MyGroupFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("what",4);
        fg2.setArguments(bundle);
        fg3.setArguments(bundle);

        fragmentsList.add(fg2);
        fragmentsList.add(fg3);
        mViewpager.setAdapter(new TabFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentsList, titles));
        mViewpager.setCurrentItem(0);

        final TabLayout.TabLayoutOnPageChangeListener listener =
                new TabLayout.TabLayoutOnPageChangeListener(mTabs);
        mViewpager.setOnPageChangeListener(listener);
//        mViewpager.setOffscreenPageLimit(2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
