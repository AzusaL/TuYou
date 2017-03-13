package com.waibao.team.tuyou.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.recommendmsg_fragment.HotelFragment;
import com.waibao.team.tuyou.recommendmsg_fragment.TouristSpotFragment;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.widget.searchView.MaterialSearchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecommendMsgActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.framelayout)
    FrameLayout frameLayout;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.toolbar_container)
    FrameLayout toolbarContainer;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.rl_city)
    RelativeLayout rlCity;

    private String what;
    private String sortType = "level";  //排序
    private String city = ConstanceUtils.LOCATION_CITY.replace("市","");  //城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        what = getIntent().getStringExtra("what");
        toolbar.setBackgroundColor(0xff56bc7b);
        setSupportActionBar(toolbar);
        initSearchView();
        refreshPage();
    }

    private void initSearchView() {
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setSuggestions(ConstanceUtils.citys);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                city = query;
                refreshPage();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void refreshPage() {
        tvCity.setText(city);
        Fragment fragment;
        if(what.equals("hotel")){
            fragment = new HotelFragment();
        }else {
            fragment = new TouristSpotFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("sortType", sortType);
        bundle.putString("city", city);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment)
                .commit();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_msg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(what.equals("hotel")){
            getMenuInflater().inflate(R.menu.menu_sort, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_price) {
            sortType = "price";
            refreshPage();
        }
        if (item.getItemId() == R.id.action_rating) {
            sortType = "total_score";
            refreshPage();
        }
        if (item.getItemId() == R.id.action_together) {
            sortType = "level";
            refreshPage();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            finish();
        }
    }

    @OnClick({R.id.btn_back, R.id.rl_city})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.rl_city:
                searchView.showSearch(true);
                break;
        }
    }
}
