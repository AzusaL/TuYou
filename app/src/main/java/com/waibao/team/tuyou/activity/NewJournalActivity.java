package com.waibao.team.tuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.NewTripGroupImgGvAdapter;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.JournalDto;
import com.waibao.team.tuyou.event.AddPhotoEvent;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.FilesUtil;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.waibao.team.tuyou.widget.WrapHeightGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

public class NewJournalActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.edit_title)
    TextInputEditText editTitle;
    @Bind(R.id.edit_detail)
    TextInputEditText editDetail;
    @Bind(R.id.gv_img)
    WrapHeightGridView mGridView;
    @Bind(R.id.rl_select_tripgroup)
    RelativeLayout rlSelectTripgroup;
    @Bind(R.id.tv_tripgroup)
    TextView mTvTripgroup;
    @Bind(R.id.tv_pathway)
    TextView mTvPathway;

    private NewTripGroupImgGvAdapter gvAdapter;
    private ArrayList<String> imgdatas = new ArrayList<>(); //显示图片的url集合
    private ArrayList<String> upload_imgdatas = new ArrayList<>(); //上传图片的url集合

    private AlertDialog mDialog;  //加载对话框
    private MyHandler handler;
    private JournalDto journalDto;
    private String jsonData;
    private String groupId;

    private String ways;
    private String time;

    private static class MyHandler extends Handler {
        private WeakReference<NewJournalActivity> activityWeakReference;

        public MyHandler(NewJournalActivity activity) {
            activityWeakReference = new WeakReference<NewJournalActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NewJournalActivity activity = activityWeakReference.get();
            if (activity != null) {
                if (msg.what == ConstanceUtils.MESSAGE_OK) {
                    activity.sendJournal();//子线程处理好图片后，上传图片
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        handler = new MyHandler(this);
        initView();
        setListener();
    }

    private void initView() {
        new ToolBarBuilder(this, toolbar).setTitle("写游记").build();

        //初始化图片Gv
        imgdatas.add("");
        gvAdapter = new NewTripGroupImgGvAdapter(imgdatas);
        mGridView.setAdapter(gvAdapter);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectImgEvent(AddPhotoEvent event) {
        imgdatas.clear();
        imgdatas.add("");
        imgdatas.addAll(event.getImgurl());
        gvAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {
                    Intent intent = new Intent(NewJournalActivity.this, PhotoSelectActivity.class);
                    intent.putExtra("pic_count", 9); //最多可添加9张
                    intent.putExtra("isneedcutimg", false);  //不需要裁切图片
                    imgdatas.remove(0);  //去掉第一个无用url，将剩下的URL传给选择图片界面显示已经选择的图片
                    intent.putStringArrayListExtra("selectimglist", imgdatas);
                    startActivity(intent);
                    imgdatas.add(0, "");  //重新添加回添加图片的按钮
                }
            }
        });
    }

    private boolean judgeEmpty() {
        if (StringUtil.isEmpty(editTitle.getText().toString())) {
            ToastUtil.showToast("标题不能为空！");
            return false;
        } else if (StringUtil.isEmpty(editDetail.getText().toString())) {
            ToastUtil.showToast("内容不能为空！");
            return false;
        } else if (imgdatas.size() == 1) {
            ToastUtil.showToast("未添加图片！");
            return false;
        } else if (StringUtil.isEmpty(ways)) {
            ToastUtil.showToast("未选择行程！");
            return false;
        }
        return true;
    }

    private void sendJournal() {
        PostFormBuilder builder = OkHttpUtils.post().url(Config.IP + "/user_postJournal")
                .addParams("uid", UserUtil.user.getId())
                .addParams("journalDto", jsonData);

        for (int i = 0; i < upload_imgdatas.size(); i++) {
            builder.addFile("files", "img.jpg", new File(upload_imgdatas.get(i).replace("file://", "")));
        }

        builder.build().connTimeOut(300000000).readTimeOut(300000000).writeTimeOut(300000000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.showToast("网络请求出错");
                        if (null != mDialog && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if (null != mDialog && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String messageInfo = jsonObject.get("messageInfo").toString();
                            if ("成功".equals(messageInfo)) {
                                ToastUtil.showToast("发表成功！");
                                finish();
                            } else {
                                ToastUtil.showToast(messageInfo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_journal;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 点击toolbar上的发送按钮
        if (item.getItemId() == R.id.action_send) {
            if (judgeEmpty()) {
                mDialog = new MyDialog().showLodingDialog(this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        upload_imgdatas.addAll(imgdatas);
                        upload_imgdatas.remove(0);  //去掉第一个放置添加按钮的无效url
                        upload_imgdatas = FilesUtil.scaleFile(upload_imgdatas);
                        journalDto = new JournalDto(editDetail.getText().toString(),
                                editTitle.getText().toString(), groupId, ways);
                        jsonData = JsonUtils.getJsonStringformat(journalDto);
                        handler.sendEmptyMessage(ConstanceUtils.MESSAGE_OK);
                    }
                }).start();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.rl_select_tripgroup, R.id.rl_select_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_select_tripgroup:
                Intent intent = new Intent(NewJournalActivity.this, SelectGroupActivity.class);
                startActivityForResult(intent, 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 1) {
            ways = data.getStringExtra("way");
            time = data.getStringExtra("time");

            groupId = data.getStringExtra("gid");

            List<String> way = StringUtil.getList(ways);
            List<String> times = StringUtil.getList(time);
            mTvTripgroup.setText(way.get(0) + "-" + way.get(way.size() - 1)
                    + "  " + times.get(0) + "-" +
                    times.get(times.size() - 1));

        }
    }
}
