package com.waibao.team.tuyou.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.NewVoteListAdapter;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.VoteDto;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

public class NewVoteActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.list_content)
    MyRecyclerView listContent;

    private EditText etTitle;
    private List<String> data = new ArrayList<>();
    private NewVoteListAdapter adapter;
    private String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        gid = getIntent().getStringExtra("gid");
        initView();
    }

    private void initView() {
        ToolBarBuilder builder = new ToolBarBuilder(this, toolbar);
        builder.setTitle("发布投票").build();
        data.add("");
        data.add("");
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listContent.setLayoutManager(manager);
        adapter = new NewVoteListAdapter(data);
        listContent.setAdapter(adapter);

        listContent.setLoadingMoreEnabled(false);
        listContent.setItemAnimator(null);
        View header = View.inflate(this, R.layout.vote_list_header, null);
        View footer = View.inflate(this, R.layout.vote_list_footer, null);
        etTitle = (EditText) header.findViewById(R.id.et_title);
        LinearLayout ll = (LinearLayout) footer.findViewById(R.id.add_list);
        clickListen onclicklistener = new clickListen();
        ll.setOnClickListener(onclicklistener);
        listContent.addHeaderView(header);
        listContent.addFootView(footer);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_create_vote;
    }

    @OnClick(R.id.bt_publish)
    public void onClick() {
        if ("".equals(etTitle.getText().toString())) {
            ToastUtil.showToast("请填写完整投票标题");
            return;
        }
        String vote_menu = "";
        String vote_total = "";
        for (int i = 0; i < data.size(); i++) {
            String s = data.get(i).toString();
            if ("".equals(s)) {
                ToastUtil.showToast("请填写完整投票选项");
                return;
            }
            vote_menu += s + ";";
            vote_total += "0;";
        }

        VoteDto voteDto = new VoteDto();
        voteDto.setContent(vote_menu);
        voteDto.setTitle(etTitle.getText().toString());
        voteDto.setTotal(vote_total);
        voteDto.setCount(0);
        voteDto.setGroup_id(gid);
        voteDto.setUid(UserUtil.user.getId());


        OkHttpUtils.get().url(Config.IP + "/group_addVote")
                .addParams("voteDto", JsonUtils.getJsonStringformat(voteDto))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络访问出错");
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("LLLLL", "onBefore: "+ request.url().toString());
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String messageInfo = jsonObject.get("messageInfo").toString();
                    if ("成功".equals(messageInfo)) {
                        setResult(RESULT_OK);
                        finish();
                    }
                    ToastUtil.showToast(messageInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class clickListen implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_list:
                    data.add("");
                    adapter.notifyItemInserted(data.size());
            }
        }
    }
}
