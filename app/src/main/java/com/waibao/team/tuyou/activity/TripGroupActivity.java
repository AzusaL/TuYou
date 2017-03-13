package com.waibao.team.tuyou.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.TabFragmentPagerAdapter;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.main_fragment.JournalsFragment;
import com.waibao.team.tuyou.tripgrounp_fragment.PartnerFragment;
import com.waibao.team.tuyou.tripgrounp_fragment.StatisticsFragment;
import com.waibao.team.tuyou.tripgrounp_fragment.WaysFragment;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.TabItemView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Request;

public class TripGroupActivity extends AppCompatActivity {

    @Bind(R.id.img_background)
    ImageView imgBackground;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.action_a)
    FloatingActionButton btn_chat;
    @Bind(R.id.action_b)
    FloatingActionButton btn_guanzhu;
    @Bind(R.id.action_c)
    FloatingActionButton btn_addpartner;
    @Bind(R.id.multiple_actions)
    FloatingActionsMenu multipleActions;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.img_head)
    ImageView imgHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_way)
    TextView tvWay;
    @Bind(R.id.btn_pintuan)
    Button btnPintuan;
    @Bind(R.id.tv_time)
    TextView tvTime;

    private List<Fragment> fragmentsList;
    private List<String> titles;
    private List<Integer> imgsId;
    private TabItemView[] tabItem = new TabItemView[4];
    private String jsonGroupdto;
    private GroupDto mGroupDto; //团信息
    private boolean isCancelReq = false;
    private String TAG = "TripGroupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_group);
        ButterKnife.bind(this);

        initData();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_menu);//显示返回按钮
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViewPager();
        initHead();
    }

    private void initHead() {
        //团长头像
        Glide.with(this)
                .load(Config.Pic + mGroupDto.getUImgUrl())
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imgHead);
        //团长名字
        tvName.setText(mGroupDto.getUNickname());

        //团路径
        List<String> ways = StringUtil.getList(mGroupDto.getWay());
        tvWay.setText(ways.get(0) + "-" + ways.get(ways.size() - 1));

        //时间和路程
        List<String> times = StringUtil.getList(mGroupDto.getWayTime());
        tvTime.setText(times.get(0).substring(5).replace("-", "/") + "-" + times.get(times.size() - 1).substring(5).replace("-", "/")
                + " 里程" + mGroupDto.getKm() + "km");

    }

    private void initData() {
        jsonGroupdto = getIntent().getStringExtra("dto");
        mGroupDto = JsonUtils.getObjectfromString(jsonGroupdto, GroupDto.class);
    }

    private void initViewPager() {
        tabs = (TabLayout) findViewById(R.id.tabs);
        titles = new ArrayList<>();
        titles.add("途经地");
        titles.add("结伴");
        titles.add("游记");
        titles.add("统计");

        imgsId = new ArrayList<>();
        imgsId.add(R.drawable.ic_img_green);
        imgsId.add(R.drawable.ic_group_green);
        imgsId.add(R.drawable.ic_adress_green);
        imgsId.add(R.drawable.ic_tongji_green);

        for (int i = 0; i < 4; i++) {
            tabItem[i] = new TabItemView(TripGroupActivity.this, titles.get(i), imgsId.get(i), 0xff56bc7b, i);
            TabLayout.Tab tab = tabs.newTab().setCustomView(tabItem[i]);
            tabs.addTab(tab);
        }

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
                ((TabItemView) tab.getCustomView()).toggle(true);
                if(multipleActions.isExpanded()){
                    multipleActions.collapse();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TabItemView) tab.getCustomView()).toggle(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        fragmentsList = new ArrayList<>();
        Fragment fg1 = new WaysFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("dto", jsonGroupdto);
        fg1.setArguments(bundle1);
        Fragment fg2 = new PartnerFragment();
        Fragment fg3 = new JournalsFragment();
        Fragment fg4 = new StatisticsFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("what", 3);
        bundle.putString("gId", mGroupDto.getId());
        bundle.putString("dto", jsonGroupdto);
        fg3.setArguments(bundle);
        fg2.setArguments(bundle);
        fg4.setArguments(bundle);

        fragmentsList.add(fg1);
        fragmentsList.add(fg2);
        fragmentsList.add(fg3);
        fragmentsList.add(fg4);

        viewpager.setAdapter(new TabFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentsList, titles));
        viewpager.setCurrentItem(0);

        final TabLayout.TabLayoutOnPageChangeListener listener =
                new TabLayout.TabLayoutOnPageChangeListener(tabs);
        viewpager.setOnPageChangeListener(listener);
        viewpager.setOffscreenPageLimit(4);

    }


    //加入团
    private void joinInTripGroup() {
        OkHttpUtils.get().url(Config.IP + "/group_addGroup")
                .addParams("gid", mGroupDto.getId())
                .addParams("uid", UserUtil.user.getId())
                .tag(TAG)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                ToastUtil.showToast("网络链接出错！");
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(String response) {
                String result;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    result = jsonObject.getString("messageInfo");
                    if (result.equals("成功")) {
                        ToastUtil.showToast("加入成功！");
                        List<String> userlist = new ArrayList<>();
                        userlist.add(UserUtil.user.getLoginName());
                        JMessageClient.addGroupMembers(mGroupDto.getTalkId(), userlist, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    ToastUtil.showToast("Jpush加入团成功");
                                }
                            }
                        });
                    } else {
                        ToastUtil.showToast("请求失败，请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //收藏团
    private void collectTripGroup() {
        OkHttpUtils.get().url(Config.IP + "/user_collection")
                .addParams("uid", UserUtil.user.getId())
                .addParams("type", "1")
                .addParams("fid", mGroupDto.getId())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if (isCancelReq) {
                    return;
                }
                ToastUtil.showToast("网络链接出错！");
            }

            @Override
            public void onResponse(String response) {
                String result;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    result = jsonObject.getString("messageInfo");
                    if (result.equals("成功")) {
                        ToastUtil.showToast("收藏成功！");
                    } else {
                        ToastUtil.showToast("请求失败，请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.img_head, R.id.action_a, R.id.action_b, R.id.action_c, R.id.btn_pintuan})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.img_head:
                intent = new Intent(this, PersonPageActivity.class);
                intent.putExtra("uId", mGroupDto.getUid());
                startActivity(intent);
                break;

            //聊天
            case R.id.action_a:
                intent = new Intent(this, ConversationGroupActivity.class);
                intent.putExtra("JgroupID", mGroupDto.getTalkId());
                intent.putExtra("groupJson", JsonUtils.getJsonStringformat(mGroupDto));
                startActivity(intent);
                multipleActions.toggle();
                break;
            //收藏
            case R.id.action_b:
                collectTripGroup();
                multipleActions.toggle();
                break;
            //加入团
            case R.id.action_c:
                int agemin, agemax;
                if (StringUtil.isEmpty(UserUtil.user.getIdentity())) {
                    ToastUtil.showToast("您还没有通过身份验证");
                } else if (UserUtil.user.getCredit() < mGroupDto.getCredit()) {
                    ToastUtil.showToast("您的信誉度不够");
                } else if (!mGroupDto.getSexType().equals("0") && UserUtil.user.getSex().equals(mGroupDto.getSexType())) {
                    ToastUtil.showToast("您的性别不符合团内要求");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TripGroupActivity.this);
                    builder.setTitle("结伴").setMessage("确认加入该行程？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    joinInTripGroup();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                }
                multipleActions.toggle();
                break;
            case R.id.btn_pintuan:
                intent = new Intent(this, SelectGroupActivity.class);
                startActivityForResult(intent, 111);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == 1) {
            String own_gid = data.getStringExtra("gid");
            final Dialog loadingDialog = new MyDialog().showLodingDialog(this);
            OkHttpUtils.get().url(Config.IP + "/group_requestUnion")
                    .addParams("request_gid", own_gid)
                    .addParams("request_uid", UserUtil.user.getId())
                    .addParams("requested_gid", mGroupDto.getId())
                    .addParams("requested_uid", mGroupDto.getUid())
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e) {
                    ToastUtil.showNetError();
                    loadingDialog.dismiss();
                }

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String messageInfo = jsonObject.get("messageInfo").toString();
                        ToastUtil.showToast(messageInfo);
                        loadingDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        isCancelReq = true;
        OkHttpUtils.getInstance().cancelTag(TAG);
    }
}
