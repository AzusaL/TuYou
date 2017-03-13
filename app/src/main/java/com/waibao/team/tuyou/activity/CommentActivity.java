package com.waibao.team.tuyou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.CommentRvAdapter;
import com.waibao.team.tuyou.callback.CommentsCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.JournalCommentDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyDialog;
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

public class CommentActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.rl_send)
    RelativeLayout rlSend;
    @Bind(R.id.rv_comment)
    MyRecyclerView recyclerView;
    @Bind(R.id.edit_comment)
    TextInputEditText editComment;
    private String TAG = "CommentActivity";
    private AlertDialog mDialog;
    private boolean isCancelReq;
    private List<JournalCommentDto> datas = new ArrayList<>();
    private CommentRvAdapter adapter;
    private String jId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        new ToolBarBuilder(this, toolbar).setTitle("评论").build();

        jId = getIntent().getStringExtra("id");
        mDialog = new MyDialog().showLodingDialog(this);
        adapter = new CommentRvAdapter(datas, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        getData(0, 0);

        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setLoadingListener(new MyRecyclerView.LoadingListener() {
            @Override
            public void onLoadMore() {
                getData(datas.size(), 1);
            }
        });
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int Position) {
                Intent intent = new Intent(CommentActivity.this,PersonPageActivity.class);
                intent.putExtra("uId",datas.get(Position).getUid());
                startActivity(intent);
            }
        });
    }

    private void addComment() {
        OkHttpUtils.get().url(Config.IP + "/user_commentJournal")
                .addParams("jid", jId)
                .addParams("uid", UserUtil.user.getId())
                .addParams("content", editComment.getText().toString())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                mDialog.cancel();
                ToastUtil.showToast("网络连接出错");
            }

            @Override
            public void onResponse(String response) {
                mDialog.cancel();
                String result;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    result = jsonObject.getString("messageInfo");
                    if (result.equals("成功")) {
                        ToastUtil.showToast("评论成功！");
                        editComment.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0) ;
                        recyclerView.setNoMore(false);
                        getData(0, 0);
                    } else {
                        ToastUtil.showToast("请求失败，请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取评论
    private void getData(int firstResult, final int what) {
        OkHttpUtils.get().url(Config.IP + "/user_getAllJournalComment")
                .addParams("jid", jId)
                .addParams("firstResult", firstResult + "")
                .tag(TAG)
                .build().execute(new CommentsCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                mDialog.cancel();
                ToastUtil.showToast("服务器出错");
            }

            @Override
            public void onResponse(List<JournalCommentDto> response) {
                mDialog.cancel();
                if (response == null || response.isEmpty()) {
                    recyclerView.noMoreLoading();
                    return;
                }
                if (what == 0) {
                    datas.clear();
                }
                datas.addAll(response);
                adapter.notifyDataSetChanged();
                recyclerView.loadMoreComplete();
            }
        });
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_comment;
    }

    @OnClick(R.id.btn_send)
    public void onClick() {
        if (!UserUtil.userisLogin) {
            ToastUtil.showToast("请先登陆");
            Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (StringUtil.isEmpty(editComment.getText().toString())) {
            ToastUtil.showToast("评论内容不能为空");
        } else {
            mDialog.show();
            addComment();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        isCancelReq = true;
        OkHttpUtils.getInstance().cancelTag(TAG);
    }
}
