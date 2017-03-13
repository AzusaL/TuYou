package com.waibao.team.tuyou.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.RecommendFriendsAdapter;
import com.waibao.team.tuyou.callback.RecommendFriendsCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.DisplayUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

public class RecommendFriendsActivity extends BaseActivity implements android.support.v7.widget.Toolbar.OnMenuItemClickListener {


    @Bind(R.id.recommend_friends_toolbar)
    android.support.v7.widget.Toolbar Toolbar;
    @Bind(R.id.recommend_friends_list_1)
    MyRecyclerView FriendsRv_1;
    @Bind(R.id.recommend_friends_list_2)
    MyRecyclerView FriendsRv_2;
    @Bind(R.id.tuijian_people_title)
    TextView tuijianPeopleTitle;
    @Bind(R.id.arrow_down)
    View arrowDown;
    @Bind(R.id.tuijian_people_root)
    RelativeLayout tuijianpeopele_root;

    private boolean is_down = false;
    private PopupWindow popupWindow_xiala;
    private PopupWindow popupWindow_Search;
    private List<UserDto> datas_Friends_1 = new ArrayList<>();
    private List<UserDto> datas_Friends_2 = new ArrayList<>();
    private RecommendFriendsAdapter adapter_Friends_1;
    private RecommendFriendsAdapter adapter_Friends_2;
    private int page_1 = 0;
    private int page_2 = 0;
    private String age = "不限";
    private String sexTyp = "0";
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

        ToolBarBuilder builder = new ToolBarBuilder(this, Toolbar);
        builder.setTitle("").build();
        initView();
        requestDatas(1);
        requestDatas(2);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_friends;
    }

    private void requestDatas(int pos) {
        switch (pos) {
            case 1:
                OkHttpUtils.get().url(Config.IP + "/recommend_journeyKmRecommend")
                        .addParams("age", age)
                        .addParams("credit", credit)
                        .addParams("sex", sexTyp)
                        .addParams("status", status)
                        .addParams("firstResult", page_1 + "")
                        .build().execute(new RecommendFriendsCallback(1) {
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
                    public void onResponse(List<UserDto> response) {
                        if (page_1 == 0) {
                            datas_Friends_1.clear();
                        }
                        if (null != response) {
                            datas_Friends_1.addAll(response);
                            page_1 += response.size();
                        } else {
                            FriendsRv_1.noMoreLoading();
                        }
                        if (null != FriendsRv_1) {
                            adapter_Friends_1.notifyDataSetChanged();
                            FriendsRv_1.loadMoreComplete();
                        }
                    }
                });
                break;
            case 2:
                OkHttpUtils.get().url(Config.IP + "/recommend_journeyTimesRecommend")
                        .addParams("age", age)
                        .addParams("credit", credit)
                        .addParams("sex", sexTyp)
                        .addParams("status", status)
                        .addParams("firstResult", page_2 + "")
                        .build().execute(new RecommendFriendsCallback(2) {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.showToast("网络访问出错");
                    }

                    @Override
                    public void onResponse(List<UserDto> response) {
                        if (page_2 == 0) {
                            datas_Friends_2.clear();
                        }
                        if (null != response) {
                            datas_Friends_2.addAll(response);
                            page_2 += response.size();
                        } else {
                            FriendsRv_2.noMoreLoading();
                        }
                        if (null != FriendsRv_2) {
                            adapter_Friends_2.notifyDataSetChanged();
                            FriendsRv_2.loadMoreComplete();
                        }
                    }
                });
                break;
        }
    }

    private void initView() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        FriendsRv_1.setLayoutManager(layoutManager1);
        FriendsRv_2.setLayoutManager(layoutManager2);

        adapter_Friends_1 = new RecommendFriendsAdapter(datas_Friends_1, 1);
        adapter_Friends_2 = new RecommendFriendsAdapter(datas_Friends_2, 2);
        adapter_Friends_1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(RecommendFriendsActivity.this, PersonPageActivity.class);
                intent.putExtra("uId", datas_Friends_1.get(Position).getId());
                startActivity(intent);
            }
        });
        adapter_Friends_2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(RecommendFriendsActivity.this, PersonPageActivity.class);
                intent.putExtra("uId", datas_Friends_2.get(Position).getId());
                startActivity(intent);
            }
        });
        FriendsRv_1.setAdapter(adapter_Friends_1);
        FriendsRv_2.setAdapter(adapter_Friends_2);

        initPopuptWindows();

        FriendsRv_1.setLoadingMoreEnabled(true);
        FriendsRv_2.setLoadingMoreEnabled(true);
        FriendsRv_1.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                requestDatas(1);
            }
        });
        FriendsRv_2.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                requestDatas(2);
            }
        });
    }

    protected void initPopuptWindows() {
        // TODO Auto-generated method stub
        View popupWindow_xiala_view = getLayoutInflater().inflate(R.layout.pop_tuijiandaren_xiala, null, false);
        final View popupWindow_search_view = getLayoutInflater().inflate(R.layout.pop_search, null, false);
        popupWindow_xiala = new PopupWindow(popupWindow_xiala_view, 450, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow_Search = new PopupWindow(popupWindow_search_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow_Search.setOutsideTouchable(true);
        popupWindow_xiala.setAnimationStyle(R.style.AnimationFade);
        popupWindow_Search.setAnimationStyle(R.style.AnimationFade);
        ((RadioButton) popupWindow_search_view.findViewById(R.id.status_not_start)).setText("单身");
        ((RadioButton) popupWindow_search_view.findViewById(R.id.status_going)).setText("有对象");
        popupWindow_search_view.findViewById(R.id.status_over).setVisibility(View.GONE);
        // 点击其他地方消失
        popupWindow_xiala_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow_xiala != null && popupWindow_xiala.isShowing()) {
                    rotateArrow();
                    popupWindow_xiala.dismiss();
                }
                return false;
            }
        });
        popupWindow_search_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getY() > popupWindow_search_view.getHeight() || event.getY() < 0) {//触点在PopupWindow内
                    if (popupWindow_Search != null && popupWindow_Search.isShowing()) {
                        popupWindow_Search.dismiss();
                    }
                }
                return false;
            }
        });

        popupWindow_xiala_view.findViewById(R.id.licheng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_xiala.dismiss();
                rotateArrow();
                tuijianPeopleTitle.setText("里程达人");
                toggleToPage(1);
            }
        });
        popupWindow_xiala_view.findViewById(R.id.chuxing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_xiala.dismiss();
                rotateArrow();
                tuijianPeopleTitle.setText("出行达人");
                toggleToPage(2);
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
                if (FriendsRv_1.getVisibility() == View.VISIBLE) {
                    page_1 = 0;
                    requestDatas(1);
                } else if (FriendsRv_2.getVisibility() == View.VISIBLE) {
                    page_2 = 0;
                    requestDatas(2);
                }
                popupWindow_Search.dismiss();
            }
        });
    }

    @OnClick({R.id.tuijian_people_title_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tuijian_people_title_rl:
                popupWindow_xiala.showAtLocation(tuijianpeopele_root, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, (int) DisplayUtil.dip2px(56));
                rotateArrow();
                break;
        }
    }

    private void rotateArrow() {
        ObjectAnimator.ofFloat(arrowDown, "rotation", is_down ? 180 : 0, is_down ? 360 : 180).setDuration(200).start();
        is_down = !is_down;
    }

    private void toggleToPage(int pos) {
        if (pos == 1) {
            FriendsRv_2.setVisibility(View.GONE);
            FriendsRv_1.setVisibility(View.VISIBLE);
        } else if (pos == 2) {
            FriendsRv_1.setVisibility(View.GONE);
            FriendsRv_2.setVisibility(View.VISIBLE);
        }
        sex_rg.check(R.id.sex_default);
        age_rg.check(R.id.age_default);
        credit_rg.check(R.id.credit_defalut);
        status_rg.check(R.id.status_default);
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
                sexTyp = "0";
                break;
            case R.id.sex_male:
                sexTyp = "2";
                break;
            case R.id.sex_female:
                sexTyp = "1";
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
        }
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
            popupWindow_Search.showAtLocation(tuijianpeopele_root, Gravity.TOP, 0, (int) DisplayUtil.dip2px(72));
        }
        return super.onOptionsItemSelected(item);
    }
}
