package com.waibao.team.tuyou.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.database.SharedPreferenceData;
import com.waibao.team.tuyou.event.SearchEvent;
import com.waibao.team.tuyou.search_fragment.RecentSearchFragment;
import com.waibao.team.tuyou.search_fragment.SearchResultFragment;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.ToolBarBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @Bind(R.id.edit_search)
    TextInputEditText editSearch;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_clear)
    View btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        new ToolBarBuilder(this, toolbar).build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, new RecentSearchFragment())
                .commit();
    }

    private void search(String key) {
        Fragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, fragment)
                .commit();
        SharedPreferenceData.getInstance().saveSearchtext(ConstanceUtils.CONTEXT, key);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void searchEvent(SearchEvent event) {
        editSearch.setText(event.getKey());
        search(event.getKey());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
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
        // 点击toolbar上的发送按钮
        if (item.getItemId() == R.id.action_search) {
            if (StringUtil.isEmpty(editSearch.getText().toString())) {
                ToastUtil.showToast("请输入关键字");
            } else {
                search(editSearch.getText().toString());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.btn_clear)
    public void onClick() {
        editSearch.setText("");
    }
}
