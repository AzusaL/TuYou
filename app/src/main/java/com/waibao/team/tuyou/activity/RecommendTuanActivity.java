package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.HomeRvTuanAdapter;
import com.waibao.team.tuyou.callback.HomeGroupCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.DisplayUtil;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Delete_exe on 2016/6/1.
 */
public class RecommendTuanActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    @Bind(R.id.recommend_tuan_toolbar)
    Toolbar toolbar;
    @Bind(R.id.recommend_tuan_list)
    MyRecyclerView tuanRv;
    @Bind(R.id.tuanSearch_root)
    RelativeLayout tuanSearchRoot;

    private PopupWindow tuanWindow_Search;
    private List<GroupDto> datas = new ArrayList<>();
    private HomeRvTuanAdapter adapter = new HomeRvTuanAdapter(datas);
    private int page = 0;
    private String age = "不限";
    private String sexType = "0";
    private String credit = "0";
    private String status = "-1";
    private RadioGroup age_rg;
    private RadioGroup sex_rg;
    private RadioGroup credit_rg;
    private RadioGroup status_rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        ToolBarBuilder builder = new ToolBarBuilder(this, toolbar);
        builder.setTitle("推荐行团").build();

        initView();
        requestData();
        initPopuptWindow();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_tuan;
    }

    private void requestData() {
        OkHttpUtils.get().url(Config.IP + "/recommend_searchGroup")
                .addParams("age", age)//默认为不限
                .addParams("sexType", sexType)//0不限，1女性，2男性
                .addParams("credit", credit)//0不限
                .addParams("status", status)//不限不用发，0未开始，1进行中，2已结束
                .addParams("firstResult", page + "")
                .build().execute(new HomeGroupCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络访问出错");
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("LLLLL", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(List<GroupDto> response) {
                if (page == 0) {
                    datas.clear();
                }
                if (response != null) {
                    page += response.size();
                    datas.addAll(response);
                }
                tuanRv.loadMoreComplete();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tuanRv.setLayoutManager(layoutManager);

        tuanRv.setAdapter(adapter);
        tuanRv.setLoadingMoreEnabled(true);
        tuanRv.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                requestData();
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                ToastUtil.showToast(Position + "");
                Intent intent = new Intent(RecommendTuanActivity.this, TripGroupActivity.class);
                intent.putExtra("dto", JsonUtils.getJsonStringformat(datas.get(Position)));
                startActivity(intent);
            }
        });
    }

    protected void initPopuptWindow() {
        final View popupWindow_search_view = getLayoutInflater().inflate(R.layout.pop_search, null, false);
        tuanWindow_Search = new PopupWindow(popupWindow_search_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        tuanWindow_Search.setOutsideTouchable(true);
        tuanWindow_Search.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_search_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getY() > popupWindow_search_view.getHeight() || event.getY() < 0) {//触点在PopupWindow内
                    if (tuanWindow_Search != null && tuanWindow_Search.isShowing()) {
                        tuanWindow_Search.dismiss();
                    }
                }
                return false;
            }
        });
        age_rg = (RadioGroup) popupWindow_search_view.findViewById(R.id.age);
        sex_rg = (RadioGroup) popupWindow_search_view.findViewById(R.id.sex);
        credit_rg = (RadioGroup) popupWindow_search_view.findViewById(R.id.credit);
        status_rg = (RadioGroup) popupWindow_search_view.findViewById(R.id.status);
        popupWindow_search_view.findViewById(R.id.search_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchParams();
                tuanWindow_Search.dismiss();
                page = 0;
                requestData();
            }
        });
    }

    private void getSearchParams() {
        switch (age_rg.getCheckedRadioButtonId()) {
            case R.id.age_default:
                age = "不限";
                break;
            case R.id.age_18to22:
                age = "18-22";
                break;
            case R.id.age_23to26:
                age = "23-26";
                break;
            case R.id.age_27to35:
                age = "27-35";
                break;
            case R.id.age_35to:
                age = "35-999";
                break;
        }
        switch (sex_rg.getCheckedRadioButtonId()) {
            case R.id.sex_default:
                sexType = "0";
                break;
            case R.id.sex_male:
                sexType = "2";
                break;
            case R.id.sex_female:
                sexType = "1";
                break;
        }
        switch (credit_rg.getCheckedRadioButtonId()) {
            case R.id.credit_defalut:
                credit = "0";
                break;
            case R.id.credit_1:
                credit = "90";
                break;
            case R.id.credit_2:
                credit = "70";
                break;
        }
        switch (status_rg.getCheckedRadioButtonId()) {
            case R.id.status_default:
                status = "-1";
                break;
            case R.id.status_not_start:
                status = "0";
                break;
            case R.id.status_going:
                status = "1";
                break;
            case R.id.status_over:
                status = "2";
                break;
        }
//        sex_rg.check(R.id.sex_default);
//        age_rg.check(R.id.age_default);
//        credit_rg.check(R.id.credit_defalut);
//        status_rg.check(R.id.status_default);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            tuanWindow_Search.showAtLocation(tuanSearchRoot, Gravity.TOP, 0, (int) DisplayUtil.dip2px(72));
        }
        return super.onOptionsItemSelected(item);
    }
}
