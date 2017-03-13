package com.waibao.team.tuyou.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.event.AddPhotoEvent;
import com.waibao.team.tuyou.event.LoginEvent;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.DateUtil;
import com.waibao.team.tuyou.utils.DisplayUtil;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.DatePickerFragment;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;

public class EditPersonalMsgActivity extends BaseActivity implements DatePickerFragment.DateSet {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.img_head)
    ImageView imgHead;
    @Bind(R.id.rl_head)
    RelativeLayout rlHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.rl_name)
    RelativeLayout rlName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.rl_sex)
    RelativeLayout rlSex;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.rl_age)
    RelativeLayout rlAge;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.rl_birthday)
    RelativeLayout rlBirthday;
    @Bind(R.id.tv_hoby)
    TextView tvHoby;
    @Bind(R.id.rl_hoby)
    RelativeLayout rlHoby;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.rl_status)
    RelativeLayout rlStatus;
    @Bind(R.id.tv_signname)
    TextView mTvSignname;
    @Bind(R.id.rl_signname)
    RelativeLayout mRlSignname;

    private String imgUrl;
    private int sexPosition = 0;
    private int statusPosition = 0;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        new ToolBarBuilder(this, toolbar).setTitle("修改个人资料").build();
        User user = UserUtil.user;
        Glide.with(this)
                .load(Config.Pic + user.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(ConstanceUtils.CONTEXT))
                .into(imgHead);

        tvName.setText(user.getNickname());

        tvAge.setText(StringUtil.parseEmpty(user.getAge() + ""));

        if (StringUtil.isEmpty(user.getSex()) || user.getSex().equals("1")) {
            tvSex.setText("女");
        } else {
            tvSex.setText("男");
        }

        if (user.getBirthday() != null) {
            tvBirthday.setText(DateUtil.getStringByFormat(user.getBirthday(), DateUtil.dateFormatYMD));
        } else {
            tvBirthday.setText("");
        }

        tvHoby.setText(StringUtil.parseEmpty(user.getHobby()));

        if (user.getStatus() == 0) {
            tvStatus.setText("单身");
        } else {
            tvStatus.setText("已婚");
        }

        mTvSignname.setText(StringUtil.parseEmpty(user.getDescription()));

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_personal_msg;
    }

    @OnClick({R.id.rl_signname, R.id.rl_head, R.id.rl_name, R.id.rl_sex, R.id.rl_age, R.id.rl_birthday, R.id.rl_hoby, R.id.rl_status})
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (view.getId()) {
            case R.id.rl_head:
                Intent intent = new Intent(EditPersonalMsgActivity.this, PhotoSelectActivity.class);
                intent.putExtra("pic_count", 1);
                intent.putExtra("isneedcutimg", true);
                startActivity(intent);
                break;
            case R.id.rl_name:
                final int magin = (int) DisplayUtil.dip2px(20);
                final TextInputEditText editText = new TextInputEditText(this);
                editText.setText(tvName.getText().toString());
                builder.setTitle("昵称").setView(editText, magin, magin, magin, magin).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtil.isEmpty(editText.getText().toString())) {
                            ToastUtil.showToast("您输入的内容为空！");
                        } else {
                            tvName.setText(editText.getText().toString());
                        }
                    }
                }).show();
                break;
            case R.id.rl_sex:
                sexPosition = 0;
                builder.setTitle("性别").setSingleChoiceItems(R.array.sexselect, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexPosition = which;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (sexPosition == 0) {
                            tvSex.setText("男");
                        } else {
                            tvSex.setText("女");
                        }
                    }
                }).show();

                break;
            case R.id.rl_age:
                final int magin2 = (int) DisplayUtil.dip2px(20);
                final TextInputEditText editText2 = new TextInputEditText(this);
                editText2.setText(tvAge.getText().toString());
                editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setTitle("年龄").setView(editText2, magin2, magin2, magin2, magin2).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtil.isEmpty(editText2.getText().toString())) {
                            ToastUtil.showToast("您输入的内容为空！");
                        } else {
                            tvAge.setText(editText2.getText().toString());
                        }
                    }
                }).show();
                break;
            case R.id.rl_birthday:
                showDatePick();
                break;
            case R.id.rl_hoby:
                final int magin3 = (int) DisplayUtil.dip2px(20);
                final TextInputEditText editText3 = new TextInputEditText(this);
                editText3.setText(tvHoby.getText().toString());
                builder.setTitle("爱好").setView(editText3, magin3, magin3, magin3, magin3).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtil.isEmpty(editText3.getText().toString())) {
                            ToastUtil.showToast("您输入的内容为空！");
                        } else {
                            tvHoby.setText(editText3.getText().toString());
                        }
                    }
                }).show();
                break;
            case R.id.rl_status:
                statusPosition = 0;
                builder.setTitle("状态").setSingleChoiceItems(R.array.status, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        statusPosition = which;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (statusPosition == 0) {
                            tvStatus.setText("单身");
                        } else {
                            tvStatus.setText("已婚");
                        }
                    }
                }).show();
                break;
            case R.id.rl_signname:
                final int magin4 = (int) DisplayUtil.dip2px(20);
                final TextInputEditText editText4 = new TextInputEditText(this);
                editText4.setText(mTvSignname.getText().toString());
                builder.setTitle("个性签名").setView(editText4, magin4, magin4, magin4, magin4).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtil.isEmpty(editText4.getText().toString())) {
                            ToastUtil.showToast("您输入的内容为空！");
                        } else {
                            mTvSignname.setText(editText4.getText().toString());
                        }
                    }
                }).show();
                break;
            default:
                break;
        }
    }

    private void senMsg() {
        dialog = new MyDialog().showLodingDialog(this);
        User user = UserUtil.user;
        user.setNickname(tvName.getText().toString());
        if (tvSex.getText().toString().equals("男")) {
            user.setSex("2");
        } else {
            user.setSex("1");
        }
        user.setAge(Integer.valueOf(tvAge.getText().toString()));
        user.setBirthday(DateUtil.getDateByFormat(tvBirthday.getText().toString(), DateUtil.dateFormatYMD));
        user.setDescription(mTvSignname.getText().toString());
        if (tvStatus.getText().toString().equals("单身")) {
            user.setStatus(0);
        } else {
            user.setStatus(1);
        }
        user.setHobby(tvHoby.getText().toString());

        PostFormBuilder builder = OkHttpUtils.post().url(Config.IP + "/user_editUser")
                .addParams("userDto", JsonUtils.getJsonStringformat(user));

        if (!StringUtil.isEmpty(imgUrl)) {
            builder.addFile("files", "img.jpg", new File(imgUrl.replace("file://", "")));
        }

        builder.build().connTimeOut(300000000).readTimeOut(300000000).writeTimeOut(300000000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.showNetError();
                        dialog.cancel();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.e("main", "onResponse: " + response);
                        dialog.cancel();
                        String result;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result = jsonObject.getString("messageInfo");
                            if (result.equals("成功")) {
                                ToastUtil.showToast("修改成功！");
                                EventBus.getDefault().post(new LoginEvent(true));
                                UserInfo myinfo = JMessageClient.getMyInfo();
                                myinfo.setNickname(tvName.getText().toString());
                                JMessageClient.updateMyInfo(UserInfo.Field.nickname, myinfo, new BasicCallback() {

                                    @Override
                                    public void gotResult(int i, String s) {

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


    //时间选择器
    private void showDatePick() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("date", "");
        bundle.putBoolean("clear", false);
        newFragment.setArguments(bundle);
        newFragment.setCancelable(true);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        getSupportFragmentManager().executePendingTransactions();
    }

    //eventbus接收选择图片界面发来的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventPhotoSelect(AddPhotoEvent event) {
        imgUrl = event.getImgurl().get(0);
        //更新头像
        Glide.with(this)
                .load(imgUrl)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(imgHead);
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
            senMsg();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dateSetResult(String date, boolean clear) {
        tvBirthday.setText(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }
}
