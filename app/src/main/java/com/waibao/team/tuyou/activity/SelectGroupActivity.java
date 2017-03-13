package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.SelectGroupRvAdapter;
import com.waibao.team.tuyou.callback.HomeGroupCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.SelectGroupDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

public class SelectGroupActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerview)
    MyRecyclerView recyclerview;

    private List<SelectGroupDto> datas = new ArrayList<>();
    private SelectGroupRvAdapter adapter;
    private AlertDialog dialog;
    private String TAG = "SelectGroupActivity";
    private boolean isCancelReq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {
        new ToolBarBuilder(this, toolbar).setTitle("请选择一个行程").build();
        dialog = new MyDialog().showLodingDialog(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        adapter = new SelectGroupRvAdapter(datas, this);
        recyclerview.setAdapter(adapter);
        recyclerview.setLoadingMoreEnabled(true);

        recyclerview.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                getData(datas.size());
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                for (int i = 0; i < datas.size(); i++) {
                    if(datas.get(i).isCheck()){
                        datas.get(i).setCheck(false);
                    }
                }
                datas.get(position).setCheck(true);
                adapter.notifyDataSetChanged();
            }
        });
        getData(0);
    }

    private void getData(int firstResult) {
        OkHttpUtils.get().url(Config.IP + "/user_getAllMyGroup")
                .addParams("uid", UserUtil.user.getId())
                .addParams("firstResult", firstResult + "")
                .tag(TAG)
                .build().execute(new HomeGroupCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                dialog.cancel();
                recyclerview.setNoMore(true);
                adapter.notifyDataSetChanged();
                ToastUtil.showNetError();
            }

            @Override
            public void onResponse(List<GroupDto> response) {
                dialog.cancel();
                if (response == null || response.isEmpty()) {
                    recyclerview.setNoMore(true);
                    adapter.notifyDataSetChanged();
                    return;
                }
                for (int i = 0; i < response.size(); i++) {
                    if (i == 0) {
                        datas.add(new SelectGroupDto(true, response.get(i)));
                    } else {
                        datas.add(new SelectGroupDto(false, response.get(i)));
                    }
                }
                adapter.notifyDataSetChanged();
                recyclerview.loadMoreComplete();
            }
        });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_group;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        isCancelReq = true;
        OkHttpUtils.getInstance().cancelTag(TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 点击toolbar上的发送按钮
        if (item.getItemId() == R.id.action_select) {
            int position = 0;
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).isCheck()) {
                    position = i;
                    break;
                }
            }
            Intent intent = getIntent();
            intent.putExtra("way",datas.get(position).getDto().getWay());
            intent.putExtra("time",datas.get(position).getDto().getWayTime());
            intent.putExtra("gid",datas.get(position).getDto().getId());
            setResult(1,intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
