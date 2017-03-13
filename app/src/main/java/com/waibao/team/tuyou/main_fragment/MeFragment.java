package com.waibao.team.tuyou.main_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.IdentityActivity;
import com.waibao.team.tuyou.activity.LoginActivity;
import com.waibao.team.tuyou.activity.MyCollectionsActivity;
import com.waibao.team.tuyou.activity.MyFriendsActivity;
import com.waibao.team.tuyou.activity.MyMessageActivity;
import com.waibao.team.tuyou.activity.PersonPageActivity;
import com.waibao.team.tuyou.activity.SettingActivity;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.event.LoginEvent;
import com.waibao.team.tuyou.event.MessageEvent;
import com.waibao.team.tuyou.utils.UserUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Delete_exe on 2016/5/12.
 */

public class MeFragment extends Fragment {

    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.weidenglu)
    TextView weidenglu;
    @Bind(R.id.me_message_point)
    View message_point;
    @Bind(R.id.headImage)
    ImageView headImg;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    weidenglu.setText(UserUtil.user.getNickname());
                    Glide.with(MeFragment.this).load(Config.Pic + UserUtil.user.getImgUrl())
                            .bitmapTransform(new CropCircleTransformation(getActivity())).into(headImg);
                    break;
                case 2:
                    weidenglu.setText("请登录");
                    headImg.setImageResource(R.drawable.default_head);
                    break;
                case 3:
                    message_point.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    message_point.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        if (UserUtil.userisLogin) {
            weidenglu.setText(UserUtil.user.getNickname());
            Glide.with(MeFragment.this).load(Config.Pic + UserUtil.user.getImgUrl())
                    .bitmapTransform(new CropCircleTransformation(getActivity())).into(headImg);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Subscribe
    public void onEvent_Login(LoginEvent loginEvent) {
        if (loginEvent.isLogin() && null != weidenglu) {
            handler.sendEmptyMessage(1);
        } else if (null != weidenglu) {
            handler.sendEmptyMessage(2);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MessageEvent messageEvent) {
        if (messageEvent.isMsgReceive() && null != message_point) {
            handler.sendEmptyMessage(3);
        }
    }


    @OnClick({R.id.myYouJi, R.id.collection_rv, R.id.myFriends_rv, R.id.advice_rv,
            R.id.setting_rv, R.id.me_notifiy, R.id.myMessage_rl})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.me_notifiy:
                break;
            case R.id.myYouJi:
                if (UserUtil.userisLogin) {
                    intent = new Intent(getActivity(), PersonPageActivity.class);
                    intent.putExtra("uId", UserUtil.user.getId());
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.collection_rv:
                Toast.makeText(getActivity(), "我的收藏", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), MyCollectionsActivity.class);
                startActivity(intent);
                break;
            case R.id.myFriends_rv:
                intent = null == UserUtil.user ? new Intent(getActivity(), LoginActivity.class)
                        : new Intent(getActivity(), MyFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.myMessage_rl:
                intent = new Intent(getActivity(), MyMessageActivity.class);
                startActivity(intent);
                if (null != message_point) {
                    handler.sendEmptyMessage(4);
                }
                break;
            case R.id.advice_rv:
//                Toast.makeText(getActivity(), "意见反馈", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), IdentityActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_rv:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

        }
    }
}
