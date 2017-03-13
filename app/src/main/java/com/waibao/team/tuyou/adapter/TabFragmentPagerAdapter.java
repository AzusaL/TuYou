package com.waibao.team.tuyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Azusa on 2016/1/24.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentsList;
    private List<String> titles;

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentsList, List<String> titles) {
        super(fm);
        this.mFragmentsList = fragmentsList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
