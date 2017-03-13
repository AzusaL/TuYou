package com.waibao.team.tuyou.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.LabelGvAdapter;
import com.waibao.team.tuyou.adapter.SearchJournalsLvAdapter;
import com.waibao.team.tuyou.adapter.SearchTripGroupsLvAdapter;
import com.waibao.team.tuyou.adapter.SelectLabelGvAdapter;
import com.waibao.team.tuyou.callback.HomeGroupCallback;
import com.waibao.team.tuyou.callback.JournalsCallback;
import com.waibao.team.tuyou.callback.UserCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.JournalDto;
import com.waibao.team.tuyou.dto.Label;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.DateUtil;
import com.waibao.team.tuyou.utils.DisplayUtil;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.WrapHeightGridView;
import com.waibao.team.tuyou.widget.WrapHeightListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;

public class PersonPageActivity extends AppCompatActivity {

    @Bind(R.id.img_head)
    ImageView imgHead;
    @Bind(R.id.tv_times)
    TextView tvTimes;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.gv_label)
    WrapHeightGridView gvLabel;
    @Bind(R.id.tv_reputation)
    TextView tvReputation;
    @Bind(R.id.tv_statu)
    TextView tvStatu;
    @Bind(R.id.tv_hobby)
    TextView tvHobby;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.lv_tuan)
    WrapHeightListView lvTuan;
    @Bind(R.id.tv_moretuan)
    TextView tvMoretuan;
    @Bind(R.id.rl_moretuan)
    RelativeLayout rlMoretuan;
    @Bind(R.id.lv_journals)
    WrapHeightListView lvJournals;
    @Bind(R.id.tv_morejournals)
    TextView tvMorejournals;
    @Bind(R.id.rl_morejournals)
    RelativeLayout rlMorejournals;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_signname)
    TextView tvSignname;
    @Bind(R.id.bg_head)
    ImageView mBgHead;
    @Bind(R.id.tv_km)
    TextView tvKm;
    @Bind(R.id.fab_addfriend)
    FloatingActionButton fabAddfriend;

    private String TAG = "PersonPageActivity";
    private boolean isCancelReq = false;
    private String uId;
    private User mUser;
    private List<JournalDto> journalDtos = new ArrayList<>();
    private List<GroupDto> groupDtos = new ArrayList<>();
    private List<String> labels = new ArrayList<>();

    private AlertDialog dialog;
    private List<Label> selectLabels;
    private int selectLabelPosition = -1;
    private boolean isTurntoOther = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_page);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Glide.with(this).load(R.drawable.user_bg)
                .into(mBgHead);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTurntoOther) {
            getUserMsg();
        }
    }

    private void initData() {
        uId = getIntent().getStringExtra("uId");
        getUserMsg();
    }

    private void getUserMsg() {
        dialog = new MyDialog().showLodingDialog(this);
        OkHttpUtils.get().url(Config.IP + "/user_getUserDetail")
                .addParams("uid", uId)
                .tag(TAG).build().execute(new UserCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                ToastUtil.showNetError();
            }

            @Override
            public void onResponse(User response) {
                mUser = response;
                initView();
            }
        });
    }


    private void initView() {
        //用户名
        collapsingToolbar.setTitle(mUser.getNickname());

        //个性签名
        if (StringUtil.isEmpty(mUser.getDescription())) {
            tvSignname.setText("这个人很懒，没有个性签名。。。");
        } else {
            tvSignname.setText(mUser.getDescription());
        }

        tvTimes.setText("出行" + mUser.getTimes() + "次");

        tvKm.setText(mUser.getKm() + "KM");

        //头像
        Glide.with(this)
                .load(Config.Pic + mUser.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imgHead);

        labels = StringUtil.getList(mUser.getImpress());
        labels.add("");
        gvLabel.setAdapter(new LabelGvAdapter(labels));

        gvLabel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == labels.size() - 1) {
                    if (uId.equals(UserUtil.user.getId())) {
                        ToastUtil.showToast("不能给自己评价");
                    } else {
                        showLabsSelect();
                    }
                }
            }
        });

        //信誉
        tvReputation.setText(mUser.getCredit() + "%");
        //状态
        if (mUser.getStatus() == 0) {
            tvStatu.setText("单身");
        } else {
            tvStatu.setText("已婚");
        }
        //年龄
        tvAge.setText(mUser.getAge() + "");
        //性别
        if (mUser.getSex() == null || mUser.getSex().equals("2")) {
            tvAge.setBackgroundResource(R.drawable.male_shape);
        } else {
            tvAge.setBackgroundResource(R.drawable.female_shape);
        }

        //爱好
        tvHobby.setText(StringUtil.parseEmpty(mUser.getHobby()));
        //生日
        tvBirthday.setText(DateUtil.getStringByFormat(mUser.getBirthday(), DateUtil.dateFormatYMD));

        getgroupData();
        getJournalsData();
        selectLabels = new ArrayList<>();
        selectLabels.add(new Label("责任心强", false, 1));
        selectLabels.add(new Label("幽默", false, 2));
        selectLabels.add(new Label("帅哥", false, 3));
        selectLabels.add(new Label("美女", false, 4));
        selectLabels.add(new Label("话痨", false, 5));
        selectLabels.add(new Label("不守信", false, 6));
        selectLabels.add(new Label("热情", false, 7));
        selectLabels.add(new Label("严肃", false, 8));
        selectLabels.add(new Label("活泼", false, 9));
        selectLabels.add(new Label("懒惰", false, 10));

        if (uId.equals(UserUtil.user.getId())) {
            fabAddfriend.setImageResource(R.drawable.ic_edit_white);
        }
    }

    private void showLabsSelect() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        GridView gridView = new GridView(this);
        gridView.setColumnWidth(90);
        gridView.setNumColumns(3);
        final SelectLabelGvAdapter adapter = new SelectLabelGvAdapter(selectLabels);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectLabelPosition != -1) {
                    selectLabels.get(selectLabelPosition).setCheck(false);
                }
                selectLabelPosition = position;
                selectLabels.get(position).setCheck(true);
                adapter.notifyDataSetChanged();
            }
        });
        int magin = (int) DisplayUtil.dip2px(20);
        builder.setTitle("选择评价的标签").setIcon(R.drawable.ic_label_light)
                .setView(gridView, magin, magin, magin, 20)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectLabelPosition == -1) {
                            ToastUtil.showToast("您还没有选择任何标签！");
                        } else {
                            addUserLabel();
                        }
                    }
                }).show();

    }

    private void addUserLabel() {
        dialog.show();
        OkHttpUtils.get().url(Config.IP + "/user_addUserImpress")
                .addParams("impress_uid", UserUtil.user.getId())
                .addParams("impressed_uid", uId)
                .addParams("impress_id", selectLabels.get(selectLabelPosition).getId() + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showNetError();
                dialog.cancel();
            }

            @Override
            public void onResponse(String response) {
                dialog.cancel();
                String result;
                final AlertDialog.Builder builder = new AlertDialog.Builder(PersonPageActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    result = jsonObject.getString("messageInfo");
                    if (result.equals("成功")) {
                        ToastUtil.showToast("评价成功！");
                        getUserMsg();
                    } else if (result.equals("已评价")) {
                        ToastUtil.showToast("您已经评价过咯");
                    } else if (result.equals("不是伴友")) {
                        builder.setTitle("提示").setIcon(R.drawable.ic_suggestion).setMessage("只有结伴旅游的伴友才能互相评价。")
                                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取游记
    private void getJournalsData() {
        OkHttpUtils.get().url(Config.IP + "/user_getMyJournal")
                .addParams("uid", uId)
                .tag(TAG)
                .build().execute(new JournalsCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                dialog.cancel();
                ToastUtil.showNetError();
            }

            @Override
            public void onResponse(List<JournalDto> response) {
                dialog.cancel();
                if (response == null || response.isEmpty()) {
                    tvMorejournals.setText("还没有发表过游记");
                    return;
                }
                journalDtos.addAll(response);
                final List<JournalDto> data = new ArrayList<>();
                data.add(journalDtos.get(0));
                lvJournals.setAdapter(new SearchJournalsLvAdapter(data, PersonPageActivity.this));
            }
        });
    }

    //获取行程
    private void getgroupData() {
        OkHttpUtils.get().url(Config.IP + "/user_getAllMyGroup")
                .addParams("uid", uId)
                .addParams("firstResult", 0 + "")
                .build().execute(new HomeGroupCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                dialog.cancel();
                ToastUtil.showNetError();
            }

            @Override
            public void onResponse(List<GroupDto> response) {
                dialog.cancel();
                if (response == null || response.isEmpty()) {
                    tvMorejournals.setText("还没旅行过");
                    return;
                }
                groupDtos.addAll(response);
                final List<GroupDto> data = new ArrayList<>();
                data.add(groupDtos.get(0));
                lvTuan.setAdapter(new SearchTripGroupsLvAdapter(data, PersonPageActivity.this));
                lvTuan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(PersonPageActivity.this, TripGroupActivity.class);
                        intent.putExtra("dto", JsonUtils.getJsonStringformat(data.get(0)));
                        startActivity(intent);
                    }
                });
            }
        });
    }


    @OnClick({R.id.fab_addfriend, R.id.img_head, R.id.rl_moretuan, R.id.rl_morejournals})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_head:
                if (uId.equals(UserUtil.user.getId())) {
                    isTurntoOther = true;
                    Intent intent = new Intent(this, EditPersonalMsgActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rl_moretuan:
                if (!tvMoretuan.getText().equals("还没旅行过")) {
                    Intent intent = new Intent(PersonPageActivity.this, UserGroupAndJournalActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("uId", mUser.getId());
                    intent.putExtra("name", mUser.getNickname());
                    startActivity(intent);
                }
                break;
            case R.id.rl_morejournals:
                Intent intent = new Intent(PersonPageActivity.this, UserGroupAndJournalActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("uId", mUser.getId());
                intent.putExtra("name", mUser.getNickname());
                startActivity(intent);
                break;
            case R.id.fab_addfriend:
                if (uId.equals(UserUtil.user.getId())) {
                    isTurntoOther = true;
                    intent = new Intent(this, EditPersonalMsgActivity.class);
                    startActivity(intent);
                    break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("添加好友").setMessage("添加该用户为好友？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addFriend();
                                dialog.cancel();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
                break;
        }
    }

    private void addFriend() {
        OkHttpUtils.get().url(Config.IP + "/friend_addFriend")
                .addParams("request_uid", UserUtil.user.getId())
                .addParams("requested_uid", uId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络连接出错");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String messageInfo = jsonObject.get("messageInfo").toString();
                    if ("成功".equals(messageInfo)) {
                        ToastUtil.showToast_center("请求已发送");
                    } else if ("存在".equals(messageInfo)) {
                        ToastUtil.showToast_center("请求已发送，无须重复请求");
                    } else if ("服务器异常".equals(messageInfo)) {
                        ToastUtil.showToast(messageInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        isCancelReq = true;
        OkHttpUtils.getInstance().cancelTag(TAG);
    }
}
