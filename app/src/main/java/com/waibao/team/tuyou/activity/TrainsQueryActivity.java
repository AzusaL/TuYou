package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.utils.DateUtil;
import com.waibao.team.tuyou.utils.DisplayUtil;
import com.waibao.team.tuyou.widget.DatePickerFragment;
import com.waibao.team.tuyou.widget.ToolBarBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrainsQueryActivity extends BaseActivity implements DatePickerFragment.DateSet {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_staraddress)
    TextInputEditText mEditStaraddress;
    @Bind(R.id.edit_endaddress)
    TextInputEditText mEditEndaddress;
    @Bind(R.id.rl_time)
    RelativeLayout mRlTime;
    @Bind(R.id.btn_query)
    Button mBtnQuery;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.view_train)
    View mViewTrain;

    private int what; //=1为查询火车票，=2查询飞机票

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        what = getIntent().getIntExtra("what", -1);
        initView();
    }

    private void initView() {
        String title;
        if (what == 1) {
            title = "火车票查询";
            mViewTrain.setBackgroundResource(R.drawable.ic_train_circle);
        } else {
            title = "航班查询";
            ViewGroup.LayoutParams layoutParams = mViewTrain.getLayoutParams();
            layoutParams.width = (int) DisplayUtil.dip2px(68f);
            layoutParams.height = (int) DisplayUtil.dip2px(68f);
            mViewTrain.setLayoutParams(layoutParams);
            mViewTrain.setBackgroundResource(R.drawable.ic_airplane);
        }
        new ToolBarBuilder(this, mToolbar).setElevation(0).setTitle(title).build();
        mTvTime.setText(DateUtil.getCurrentDate(DateUtil.dateFormatYMD));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_trains_query;
    }

    @OnClick({R.id.rl_time, R.id.btn_query})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_time:
                showDatePick();
                break;
            case R.id.btn_query:
                Intent intent;
                if (what == 1) {
                    intent = new Intent(TrainsQueryActivity.this, TrainQueryResultActivity.class);
                } else {
                    intent = new Intent(TrainsQueryActivity.this, AirPlaneQueryResultActivity.class);
                }
                intent.putExtra("starAddress", mEditStaraddress.getText().toString());
                intent.putExtra("endAddress", mEditEndaddress.getText().toString());
                intent.putExtra("time", mTvTime.getText().toString());
                startActivity(intent);
                break;
        }
    }

    //时间选择器
    private void showDatePick() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", "");
        bundle.putBoolean("clear", false);
        newFragment.setArguments(bundle);
        newFragment.setCancelable(true);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void dateSetResult(String date, boolean clear) {
        mTvTime.setText(date);
    }
}
