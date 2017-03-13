package com.waibao.team.tuyou.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
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
import com.waibao.team.tuyou.adapter.NewTripGroupPathWayLvAdapter;
import com.waibao.team.tuyou.callback.DistanceCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.PathWayDto;
import com.waibao.team.tuyou.event.AddPathWayEvent;
import com.waibao.team.tuyou.event.AddPhotoEvent;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.FilesUtil;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.DatePickerFragment;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.WrapHeightGridView;
import com.waibao.team.tuyou.widget.WrapHeightListView;
import com.waibao.team.tuyou.widget.searchView.MaterialSearchView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import okhttp3.Call;

public class NewTripGroupActivity extends BaseActivity implements DatePickerFragment.DateSet {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.edit_detail)
    TextInputEditText mEditDetail;
    @Bind(R.id.gv_img)
    WrapHeightGridView mGridView;
    @Bind(R.id.rl_startime)
    RelativeLayout mRlStartime;
    @Bind(R.id.rl_staraddress)
    RelativeLayout mRlStaraddress;
    @Bind(R.id.rl_endtime)
    RelativeLayout mRlEndtime;
    @Bind(R.id.rl_endaddress)
    RelativeLayout mRlEndaddress;
    @Bind(R.id.lv_pathway)
    WrapHeightListView mLv_pathWay;
    @Bind(R.id.rl_addpathway)
    RelativeLayout mRl_AddPathWay;
    @Bind(R.id.tv_staraddress)
    TextView tvStaraddress;
    @Bind(R.id.tv2)
    TextView tvStartime;
    @Bind(R.id.tv_endaddress)
    TextView tvEndaddress;
    @Bind(R.id.tv3)
    TextView tvEndtime;
    @Bind(R.id.viewadd)
    View viewadd;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.tv_sexlimi)
    TextView tvSexlimi;
    @Bind(R.id.rl_sex)
    RelativeLayout rlSex;
    @Bind(R.id.tv_agelimi)
    TextView tvAgelimi;
    @Bind(R.id.rl_age)
    RelativeLayout rlAge;
    @Bind(R.id.tv_creditlimi)
    TextView tvCreditlimi;
    @Bind(R.id.rl_credit)
    RelativeLayout rlCredit;

    private NewTripGroupImgGvAdapter gvAdapter;
    private NewTripGroupPathWayLvAdapter lvAdapter;
    private List<PathWayDto> data;
    private MyDialog myDialog = new MyDialog();
    private ArrayList<String> imgdatas = new ArrayList<>(); //显示图片的url集合
    private ArrayList<String> upload_imgdatas = new ArrayList<>(); //上传图片的url集合
    private int whoselecttime;  //判断是选择出发时间 还是到达时间
    private int whoselectAddress; //判断是选择出发地址，还是目的地地址
    private int selectPathwaytimePosition; // 更改途径地时间的position
    private int selectPathwayaddressPosition; // 更改途径地地址的position
    private AlertDialog lodingDialog;  //加载中对话框

    private int sexPosition;
    private int agePosition;
    private int creditPosition;

    private float distance = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ConstanceUtils.MESSAGE_OK) {
                launchGroup();//子线程处理好图片后，上传图片
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        setListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectImgEvent(AddPhotoEvent event) {
        imgdatas.clear();
        imgdatas.add("");
        imgdatas.addAll(event.getImgurl());
        gvAdapter.notifyDataSetChanged();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectAddressEvent(AddPathWayEvent event) {
        if (event.getWhat() == 1) {
            whoselecttime = 2;
            selectPathwaytimePosition = event.getPosition();
            showDatePick();
        } else {
            whoselectAddress = 2;
            selectPathwayaddressPosition = event.getPosition();
            searchView.showSearch();
        }
    }

    private void setListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == position) {
                    Intent intent = new Intent(NewTripGroupActivity.this, PhotoSelectActivity.class);
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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_new_trip_group;
    }

    private void initView() {
        mToolbar.setTitle("添加行程");
        mToolbar.setTitleTextColor(0xffffffff);
        mToolbar.setBackgroundColor(0xff56bc7b);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_menu);//显示返回按钮
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化图片Gv
        List<String> datas = new ArrayList<>();
        imgdatas.add("");
        gvAdapter = new NewTripGroupImgGvAdapter(imgdatas);
        mGridView.setAdapter(gvAdapter);

        //初始化途径地Lv
        data = new ArrayList<>();
        lvAdapter = new NewTripGroupPathWayLvAdapter(data);
        mLv_pathWay.setAdapter(lvAdapter);

        initSearchView();
    }

    private void initSearchView() {
        searchView.setCursorDrawable(R.drawable.color_cursor_white);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setSuggestions(ConstanceUtils.citys);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (whoselectAddress == 0) {
                    tvStaraddress.setText(query);
                } else if (whoselectAddress == 1) {
                    tvEndaddress.setText(query);
                } else {
                    data.get(selectPathwayaddressPosition).setAddress(query);
                    lvAdapter.notifyDataSetChanged();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    //时间选择器
    private void showDatePick() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", "");
        bundle.putBoolean("clear", true);
        newFragment.setArguments(bundle);
        newFragment.setCancelable(true);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        getSupportFragmentManager().executePendingTransactions();
    }

    //时间选择器的回调接口
    @Override
    public void dateSetResult(String date, boolean clear) {
        if (whoselecttime == 0) {
            tvStartime.setText(date);
        } else if (whoselecttime == 1) {
            tvEndtime.setText(date);
        } else {
            data.get(selectPathwaytimePosition).setTime(date);
            lvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
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
            if (UserUtil.userisLogin) {
                lodingDialog = myDialog.showLodingDialog(this);
                if (judgeEmpty()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            upload_imgdatas.addAll(imgdatas);
                            upload_imgdatas.remove(0);  //去掉第一个放置添加按钮的无效url
                            upload_imgdatas = FilesUtil.scaleFile(upload_imgdatas);
                            getDistance(tvStaraddress.getText().toString(), tvEndaddress.getText().toString());
                        }
                    }).start();
                } else {
                    lodingDialog.cancel();
                }
            } else {
                Intent intent = new Intent(NewTripGroupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean judgeEmpty() {
        if (StringUtil.isEmpty(mEditDetail.getText().toString())) {
            ToastUtil.showToast("未输入行程详情");
            return false;
        } else if (imgdatas.size() < 2) {
            ToastUtil.showToast("未添加图片");
            return false;
        } else if (StringUtil.isEmpty(tvStartime.getText().toString()) || tvStartime.getText().toString().equals("出发时间")) {
            ToastUtil.showToast("未选择出发时间");
            return false;
        } else if (StringUtil.isEmpty(tvEndtime.getText().toString()) || tvEndtime.getText().toString().equals("离开时间")) {
            ToastUtil.showToast("未选择离开出发时间");
            return false;
        } else if (!judgePathwayData()) {
            ToastUtil.showToast("未选择途经地时间或地点");
            return false;
        } else {
            return true;
        }
    }

    private boolean judgePathwayData() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getAddress().equals("途经地") || StringUtil.isEmpty(data.get(i).getTime())) {
                return false;
            }
        }
        return true;
    }

    @OnClick({R.id.rl_sex, R.id.rl_age, R.id.rl_credit, R.id.rl_startime, R.id.rl_staraddress, R.id.rl_endtime, R.id.rl_endaddress, R.id.rl_addpathway})
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (view.getId()) {
            case R.id.rl_startime:
                whoselecttime = 0;
                showDatePick();
                break;
            case R.id.rl_staraddress:
                whoselectAddress = 0;
                searchView.showSearch();
                break;
            case R.id.rl_endtime:
                whoselecttime = 1;
                showDatePick();
                break;
            case R.id.rl_endaddress:
                whoselectAddress = 1;
                searchView.showSearch();
                break;
            case R.id.rl_addpathway:
                PathWayDto pathWayDto = new PathWayDto();
                pathWayDto.setAddress("途经地");
                data.add(pathWayDto);
                lvAdapter.notifyDataSetChanged();
                break;
            case R.id.rl_sex:
                sexPosition = 0;
                builder.setTitle("性别限制").setSingleChoiceItems(R.array.sex, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexPosition = which;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sexPosition == 0) {
                            tvSexlimi.setText("不限");
                        } else if (sexPosition == 1) {
                            tvSexlimi.setText("女");
                        } else {
                            tvSexlimi.setText("男");
                        }
                    }
                }).show();
                break;
            case R.id.rl_age:
                agePosition = 0;
                builder.setTitle("年龄限制").setSingleChoiceItems(R.array.age, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        agePosition = which;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (agePosition == 0) {
                            tvAgelimi.setText("不限");
                        } else if (agePosition == 1) {
                            tvAgelimi.setText("18~22");
                        } else if (agePosition == 2) {
                            tvAgelimi.setText("23~26");
                        } else if (agePosition == 3) {
                            tvAgelimi.setText("27~35");
                        } else if (agePosition == 4) {
                            tvAgelimi.setText("35+");
                        }
                    }
                }).show();
                break;
            case R.id.rl_credit:
                creditPosition = 0;
                builder.setTitle("信誉限制").setSingleChoiceItems(R.array.credit, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        creditPosition = which;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (creditPosition == 0) {
                            tvCreditlimi.setText("不限");
                        } else if (creditPosition == 1) {
                            tvCreditlimi.setText("50以上");
                        } else if (creditPosition == 2) {
                            tvCreditlimi.setText("70以上");
                        } else if (creditPosition == 3) {
                            tvCreditlimi.setText("80以上");
                        } else if (creditPosition == 4) {
                            tvCreditlimi.setText("90以上");
                        }
                    }
                }).show();
                break;
        }
    }

    private String getPathWayString() {
        String PathWay = "";
        PathWay += tvStaraddress.getText() + ";";
        for (int i = 0; i < data.size(); i++) {
            PathWay += data.get(i).getAddress();
            PathWay += ";";
        }
        PathWay += tvEndaddress.getText().toString() + ";";
        return PathWay;
    }

    private String getPathTimeString() {
        String PathTime = "";
        PathTime += tvStartime.getText().toString() + ";";
        for (int i = 0; i < data.size(); i++) {
            PathTime += data.get(i).getTime();
            PathTime += ";";
        }
        PathTime += tvEndtime.getText().toString() + ";";
        return PathTime;
    }

    private void launchGroup() {
        JMessageClient.createGroup("" + System.currentTimeMillis(), "null", new CreateGroupCallback() {
            @Override
            public void gotResult(int responseCode, String responseMsg, long talkId) {
                if (responseCode == 0) {
                    int sexType;
                    if (tvSexlimi.getText().toString().equals("不限")) {
                        sexType = 0;
                    } else if (tvSexlimi.getText().toString().equals("女")) {
                        sexType = 1;
                    } else {
                        sexType = 2;
                    }
                    GroupDto groupDto = new GroupDto();
                    groupDto.setTalkId(talkId);
                    groupDto.setAge(tvAgelimi.getText().toString());
                    groupDto.setCollectionCount(0);
                    groupDto.setWay(getPathWayString());
                    groupDto.setDescription(mEditDetail.getText().toString());
                    groupDto.setWayTime(getPathTimeString());
                    groupDto.setSexType(sexType + "");
                    groupDto.setKm(distance);
                    groupDto.setLng(ConstanceUtils.LOCATION_LONGITUDE);
                    groupDto.setLat(ConstanceUtils.LOCATION_LATITUDE);
                    if (tvCreditlimi.getText().toString().equals("不限")) {
                        groupDto.setCredit(0);
                    } else {
                        groupDto.setCredit(Integer.valueOf(tvCreditlimi.getText().toString().replace("以上", "")));
                    }
                    launchGroupToServer(JsonUtils.getJsonStringformat(groupDto));
                } else if (null != lodingDialog && lodingDialog.isShowing()) {
                    lodingDialog.dismiss();
                }
            }
        });
    }

    private void getDistance(String starAddress, String endAddress) {
        OkHttpUtils.get().url("http://api.map.baidu.com/direction/v1/routematrix")
                .addParams("origins", starAddress)
                .addParams("destinations", endAddress)
                .addParams("output", "json")
                .addParams("mcode", Config.BMAP_MCODE)
                .addParams("ak", Config.BMAP_AK)
                .build().execute(new DistanceCallback() {
            @Override
            public void onError(Call call, Exception e) {
                handler.sendEmptyMessage(ConstanceUtils.MESSAGE_OK);
            }

            @Override
            public void onResponse(Float response) {
                distance = response;
                Log.e("LLLLLL", "onResponse: LLLLLL");
                handler.sendEmptyMessage(ConstanceUtils.MESSAGE_OK);
            }
        });
    }

    private void launchGroupToServer(String jsonString) {
        PostFormBuilder builder = OkHttpUtils.post().url(Config.IP + "/group_launchGroup")
                .addParams("uid", UserUtil.user.getId())
                .addParams("groupDto", jsonString);

        for (int i = 0; i < upload_imgdatas.size(); i++) {
            builder.addFile("files", "img.jpg", new File(upload_imgdatas.get(i).replace("file://", "")));
        }

        builder.build().connTimeOut(300000000).readTimeOut(300000000).writeTimeOut(300000000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.showToast("网络请求出错");
                        if (null != lodingDialog && lodingDialog.isShowing()) {
                            lodingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if (null != lodingDialog && lodingDialog.isShowing()) {
                            lodingDialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String messageInfo = jsonObject.get("messageInfo").toString();
                            if ("成功".equals(messageInfo)) {
                                ToastUtil.showToast("团创建成功");
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
    public void onBackPressed() {
        super.onBackPressed();
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            finish();
        }
    }
}
