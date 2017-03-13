package com.waibao.team.tuyou.tripgrounp_fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.PersonPageActivity;
import com.waibao.team.tuyou.adapter.TripgroupFriendsRvAdapter;
import com.waibao.team.tuyou.callback.UsersCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.MyRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;

/**
 * Created by Azusa on 2016/5/6.
 */
public class PartnerFragment extends Fragment {
    @Bind(R.id.recyclerview)
    MyRecyclerView recyclerview;
    private View view;
    private View headView;
    private List<UserDto> datas = new ArrayList<>();
    private AlertDialog dialog;
    private GroupDto mGroupDto;
    private boolean isInited = false;  //是否初始化过
    private String gId; //团Id

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tripgroup_friend, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInited) {
            initView();
            isInited = true;
        }
    }

    private void initView() {
        gId = getArguments().getString("gId");
        Log.e("main", "initView: "+getArguments().getString("dto"));
        mGroupDto = JsonUtils.getObjectfromString(getArguments().getString("dto"), GroupDto.class);
        dialog = new MyDialog().showLodingDialog(getActivity());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2
                , StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);

        headView = View.inflate(ConstanceUtils.CONTEXT, R.layout.head_groupfriends_rv, null);
        getData();
    }

    private void getData() {
        OkHttpUtils.get().url(Config.IP + "/group_getAllGroupFriend")
                .addParams("gid", gId)
                .build().execute(new UsersCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络链接出错");
                dialog.cancel();
            }

            @Override
            public void onResponse(final List<UserDto> response) {
                datas.addAll(response);
                TripgroupFriendsRvAdapter adapter = new TripgroupFriendsRvAdapter(datas, getActivity());
                recyclerview.setAdapter(adapter);
                recyclerview.addHeaderView(headView);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View v, int Position) {
                        Intent intent = new Intent(getActivity(), PersonPageActivity.class);
                        intent.putExtra("uId", response.get(Position).getId());
                        startActivity(intent);
                    }
                });
                initHead();
            }
        });
    }

    private void initHead() {
        TextView tv_friendcount = (TextView) headView.findViewById(R.id.tv_friendscount);
        Button btn_joinin = (Button) headView.findViewById(R.id.btn_joinin);

        tv_friendcount.setText("结伴好友（" + datas.size() + "人）");
        btn_joinin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(UserUtil.user.getIdentity())) {
                    ToastUtil.showToast("您还没有通过身份验证");
                } else if (UserUtil.user.getCredit() < mGroupDto.getCredit()) {
                    ToastUtil.showToast("您的信誉度不够");
                } else if (!mGroupDto.getSexType().equals("0") && UserUtil.user.getSex().equals(mGroupDto.getSexType())) {
                    ToastUtil.showToast("您的性别不符合团内要求");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            }
        });
        dialog.cancel();
    }

    //加入团
    private void joinInTripGroup() {
        OkHttpUtils.get().url(Config.IP + "/group_addGroup")
                .addParams("gid", mGroupDto.getId())
                .addParams("uid", UserUtil.user.getId())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络链接出错！");
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
