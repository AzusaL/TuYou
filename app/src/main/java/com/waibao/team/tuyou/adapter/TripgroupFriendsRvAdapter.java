package com.waibao.team.tuyou.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.LoginActivity;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.searchView.utils.AnimationUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Azusa on 2016/5/6.
 */
public class TripgroupFriendsRvAdapter extends RecyclerView.Adapter<TripgroupFriendsRvAdapter.ViewHolder> {

    private List<UserDto> datas;
    private Activity mActivity;
    private OnItemClickListener onItemClickListener;

    public TripgroupFriendsRvAdapter(List<UserDto> datas, Activity activity) {
        this.datas = datas;
        this.mActivity = activity;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = View.inflate(parent.getContext(), R.layout.item_tripgroup_friendrv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_name.setText(datas.get(position).getNickname());
        holder.tv_signname.setText(datas.get(position).getDescription());

        Glide.with(ConstanceUtils.CONTEXT)
                .load(Config.Pic + datas.get(position).getImgUrl())
                .into(holder.img);

        if (datas.get(position).getId().equals(UserUtil.user.getId())) {
            holder.btn_like.setVisibility(View.GONE);
        } else {
            holder.btn_like.setVisibility(View.VISIBLE);
        }

        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserUtil.userisLogin) {
                    ToastUtil.showToast_center("请先登陆！");
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    return;
                }
                AnimationUtil.likeAnimation(holder.btn_like, R.drawable.ic_like_red);
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("添加好友").setMessage("添加该用户为好友？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addFriend(holder.btn_like, datas.get(position).getId());
                                dialog.cancel();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        AnimationUtil.likeAnimation(holder.btn_like, R.drawable.ic_favorite1);
                    }
                }).show();
            }
        });

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }

    private void addFriend(final Button btn, String uId) {
        OkHttpUtils.get().url(Config.IP + "/friend_addFriend")
                .addParams("request_uid", UserUtil.user.getId())
                .addParams("requested_uid", uId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络连接出错");
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String messageInfo = jsonObject.get("messageInfo").toString();
                    if ("成功".equals(messageInfo)) {
                        ToastUtil.showToast_center("请求已发送");
                        btn.setClickable(false);
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
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView tv_name;
        public TextView tv_signname;
        public Button btn_like;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_friendhead);
            tv_name = (TextView) itemView.findViewById(R.id.tv_friendname);
            tv_signname = (TextView) itemView.findViewById(R.id.tv_signname);
            btn_like = (Button) itemView.findViewById(R.id.btn_favorite);
        }
    }
}
