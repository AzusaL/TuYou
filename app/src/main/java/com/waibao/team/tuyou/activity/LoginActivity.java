package com.waibao.team.tuyou.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.database.SharedPreferenceData;
import com.waibao.team.tuyou.event.LoginEvent;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Request;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.edit_name)
    TextInputEditText editName;
    @Bind(R.id.edit_pwd)
    TextInputEditText editPwd;
    @Bind(R.id.login_ll)
    LinearLayout loginLl;
    @Bind(R.id.edit_name_reg)
    TextInputEditText editNameReg;
    @Bind(R.id.edit_pwd_reg)
    TextInputEditText editPwdReg;
    @Bind(R.id.edit_yanzheng_reg)
    TextInputEditText editYanzhengReg;
    @Bind(R.id.register_ll)
    LinearLayout registerLl;
    @Bind(R.id.bt_getyanzheng)
    Button bt_getyanzheng;

    private String name;
    private String pwd;
    private String name_reg;//phone
    private String pwd_reg;
    private String yanzheng;

    private MyDialog mydialog = new MyDialog();
    private Dialog loaddialog;

    private int can_back = 0;
    //    PropertyValuesHolder inpvhX = PropertyValuesHolder.ofFloat(ALPHA, 0f, 1f);
    PropertyValuesHolder inpvhY = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
    PropertyValuesHolder inpvhZ = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);

    //    PropertyValuesHolder outpvhX = PropertyValuesHolder.ofFloat(ALPHA, 1f, 0f);
    PropertyValuesHolder outpvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f);
    PropertyValuesHolder outpvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.btn_login, R.id.tv_register, R.id.btn_back, R.id.btn_register, R.id.bt_getyanzheng})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                name = editName.getText().toString();
                pwd = editPwd.getText().toString();
                userLogin();

                break;
            case R.id.tv_register:
                registerLl.setVisibility(View.VISIBLE);
                can_back += 1;
                ObjectAnimator.ofPropertyValuesHolder(registerLl, inpvhY, inpvhZ).setDuration(250).start();
                ObjectAnimator.ofPropertyValuesHolder(loginLl, outpvhY, outpvhZ).setDuration(250)
                        .start();
                loginLl.setVisibility(View.GONE);
                break;
            case R.id.bt_getyanzheng:
                name_reg = editNameReg.getText().toString();
                if (!StringUtil.isMobileNo(name_reg)) {
                    ToastUtil.showToast("请输入正确的手机号");
                    return;
                }
                getYanZheng();
                break;
            case R.id.btn_register:
                name_reg = editNameReg.getText().toString();
                yanzheng = editYanzhengReg.getText().toString();
                pwd_reg = editPwdReg.getText().toString();
                register();
                break;
            case R.id.btn_back:
                if (can_back == 0) {
                    finish();
                } else {
                    can_back -= 1;
                    loginLl.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofPropertyValuesHolder(loginLl, inpvhY, inpvhZ).setDuration(250).start();
                    ObjectAnimator.ofPropertyValuesHolder(registerLl, outpvhY, outpvhZ).setDuration(250)
                            .start();
                    registerLl.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void userLogin() {
        loaddialog = mydialog.showLodingDialog(this);
        OkHttpUtils.get().url(Config.IP + "/user_login")
                .addParams("loginName", name)
                .addParams("passwd", pwd)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络连接出错");
                if (null != loaddialog) {
                    loaddialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                String messageInfo;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    messageInfo = jsonObject.get("messageInfo").toString();

                    if (messageInfo.equals("成功")) {
                        final String userJson = jsonObject.get("userDto").toString();
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            UserUtil.user = mapper.readValue(userJson, User.class);
                            JPushInterface.setAlias(ConstanceUtils.CONTEXT, UserUtil.user.getId(), null);
                            UserUtil.userisLogin = true;
                            ToastUtil.showToast("登录成功");
                            new JloginThread().start();
                            SharedPreferenceData.getInstance().saveIsLogined(LoginActivity.this, true);
                            SharedPreferenceData.getInstance().saveUsernamePwd(LoginActivity.this, name, pwd);
                            EventBus.getDefault().post(new LoginEvent(true));
                            if (null != loaddialog) {
                                loaddialog.dismiss();
                            }
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (messageInfo.equals("用户名不存在") || messageInfo.equals("密码不正确")
                            || messageInfo.equals("服务器出错，请稍后重试")) {
                        loaddialog.dismiss();
                        ToastUtil.showToast(messageInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class JloginThread extends Thread {
        @Override
        public void run() {
            JMessageClient.login(name, pwd, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    Log.e("J登录", " responseCode" + i);
                }
            });
        }
    }

    private void getYanZheng() {
        OkHttpUtils.get().url(Config.IP + "/user_sendMsg")
                .addParams("loginName", name_reg)
                .addParams("type", "0")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络连接出错");
            }

            @Override
            public void onBefore(Request request) {
                Log.e("LLLLL", "onBefore: " + request.url());
                super.onBefore(request);
            }

            @Override
            public void onResponse(String response) {
                String messageInfo = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    messageInfo = jsonObject.get("messageInfo").toString();

                    if (messageInfo.equals("success")) {
                        ToastUtil.showToast("验证码发送成功");
                        bt_getyanzheng.setText("60秒内不可重复发送");
                    } else if (messageInfo.equals("fail") || messageInfo.equals("exist")
                            || messageInfo.equals("unexist")) {
                        ToastUtil.showToast(messageInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void register() {
        loaddialog = mydialog.showLodingDialog(this);
        OkHttpUtils.get().url(Config.IP + "/user_setPassword")
                .addParams("loginName", name_reg)
                .addParams("password", pwd_reg)
                .addParams("msgCode", yanzheng)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                loaddialog.dismiss();
                ToastUtil.showToast("网络连接出错");
            }

            @Override
            public void onResponse(final String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String messageInfo = jsonObject.get("messageInfo").toString();

                    if ("success".equals(messageInfo)) {
                        JMessageClient.register(name_reg, pwd_reg, new BasicCallback() {
                            @Override
                            public void gotResult(int responseCode, String responseMessage) {
                                if (responseCode == 0) {
                                    loaddialog.dismiss();
                                    can_back -= 1;
                                    loginLl.setVisibility(View.VISIBLE);
                                    ObjectAnimator.ofPropertyValuesHolder(loginLl, inpvhY, inpvhZ).setDuration(250).start();
                                    ObjectAnimator.ofPropertyValuesHolder(registerLl, outpvhY, outpvhZ).setDuration(250)
                                            .start();
                                    registerLl.setVisibility(View.GONE);
                                    ToastUtil.showToast("注册成功");
                                }
                            }
                        });


                    } else if ("fail".equals(messageInfo)) {
                        ToastUtil.showToast("注册失败");
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (can_back == 0) {
                finish();
            } else {
                can_back -= 1;
                loginLl.setVisibility(View.VISIBLE);
                ObjectAnimator.ofPropertyValuesHolder(loginLl, inpvhY, inpvhZ).setDuration(250).start();
                ObjectAnimator.ofPropertyValuesHolder(registerLl, outpvhY, outpvhZ).setDuration(250)
                        .start();
                registerLl.setVisibility(View.GONE);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
