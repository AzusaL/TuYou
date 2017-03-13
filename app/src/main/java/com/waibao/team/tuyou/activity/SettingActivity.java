package com.waibao.team.tuyou.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.database.SharedPreferenceData;
import com.waibao.team.tuyou.event.LoginEvent;
import com.waibao.team.tuyou.model.User;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Delete_exe on 2016/5/12.
 */
public class SettingActivity extends BaseActivity {
    @Bind(R.id.setting_toolbar)
    Toolbar settingToolbar;
    @Bind(R.id.cb_setting_push)
    CheckBox cbSettingPush;
    @Bind(R.id.clearCache_tx)
    TextView clearCacheTx;
    @Bind(R.id.logout)
    Button logout_bt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        if (!UserUtil.userisLogin) {
            logout_bt.setVisibility(View.GONE);
        } else {
            logout_bt.setVisibility(View.VISIBLE);
        }
        ToolBarBuilder builder = new ToolBarBuilder(this, (Toolbar) findViewById(R.id.setting_toolbar));
        builder.setTitle("设置").build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.clearCache_rv, R.id.checkUpdate_rv, R.id.logout, R.id.getgroup_rv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearCache_rv:
                break;
            case R.id.checkUpdate_rv:
                break;
            case R.id.getgroup_rv:
                JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
                    @Override
                    public void gotResult(int responseCode, String s, List<Long> list) {
                        if (responseCode == 0) {
                            Log.e("LLLLL", "gotResult: " + list);
                        }
                    }
                });
                break;
            case R.id.logout:
                logout();
                break;
        }
    }

    private void logout() {
        OkHttpUtils.get().url(Config.IP + "/user_logOut")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络连接出错");
            }


            @Override
            public void onResponse(String response) {
                String messageInfo = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    messageInfo = jsonObject.get("messageInfo").toString();
                    Log.e("LLLLL", messageInfo);
                    if (messageInfo.equals("退出成功")) {
                        UserUtil.user = new User();
                        ToastUtil.showToast(messageInfo);
                        SharedPreferenceData.getInstance().saveIsLogined(SettingActivity.this, false);
                        EventBus.getDefault().post(new LoginEvent(false));
//                        JMessageClient.logout();
                        UserUtil.userisLogin = false;
                        finish();
                    } else {
                        ToastUtil.showToast("退出失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
