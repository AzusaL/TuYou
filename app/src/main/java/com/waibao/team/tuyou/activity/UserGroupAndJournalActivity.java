package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.main_fragment.JournalsFragment;
import com.waibao.team.tuyou.main_fragment.MyGroupFragment;
import com.waibao.team.tuyou.widget.ToolBarBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserGroupAndJournalActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.framelayout)
    FrameLayout framelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_group_and_journal;
    }

    private void getData() {
        Intent intent = getIntent();
        String uId = intent.getStringExtra("uId");
        int type = intent.getIntExtra("type", -1);
        String name = intent.getStringExtra("name");

        String title;
        if (type == 1) {
            title = name + "的所有游记";
        } else {
            title = name + "的所有行程";
        }
        new ToolBarBuilder(this, toolbar).setTitle(title).build();


        Bundle bundle = new Bundle();
        bundle.putString("uId", uId);


        if (type == 1) {
            Fragment fgJournal = new JournalsFragment();
            bundle.putInt("what",2);
            fgJournal.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fgJournal)
                    .commit();
        } else {
            Fragment fgGroup = new MyGroupFragment();
            bundle.putInt("what",1);
            bundle.putString("isNeed","need");
            fgGroup.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fgGroup)
                    .commit();
        }

    }
}
