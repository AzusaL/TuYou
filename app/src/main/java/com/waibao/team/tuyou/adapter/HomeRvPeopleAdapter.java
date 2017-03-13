package com.waibao.team.tuyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;

/**
 * Created by Delete_exe on 2016/5/22.
 */
public class HomeRvPeopleAdapter extends RecyclerView.Adapter<HomeRvPeopleAdapter.ViewHolder> {
    private Context context;
    private List<UserDto> datas;
    private OnItemClickListener onItemClickListener;

    public HomeRvPeopleAdapter(List<UserDto> datas) {
        this.datas = datas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.item_homefragment_rv_people, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserDto userDto = datas.get(position);
        holder.username.setText(userDto.getNickname());
        holder.usergender.setText(userDto.getAge() + "");
        int res = "2".equals(userDto.getSex()) ? R.drawable.male_shape : R.drawable.female_shape;
        String type = position > 2 ? "里程达人" : "出行达人";
        String sum = position > 2 ? userDto.getKm() + "km" : userDto.getTimes() + "次";
        Log.e("LLLLL", Config.Pic + userDto.getImgUrl());
        Glide.with(context).load(Config.Pic + userDto.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(context)).into(holder.tuijian_user_head);
        holder.usergender.setBackgroundResource(res);
        holder.tuijiantype.setText(type);
        holder.tuijiansum.setText(sum);
        holder.add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != UserUtil.user.getLoginName() && UserUtil.user.getLoginName().length() > 0) {
                    v.setEnabled(false);
                    addFriend(holder, userDto.getId());
                } else {
                    ToastUtil.showToast("当前用户未登录");
                }
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

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView tuijian_user_head;
        private TextView username;
        private TextView usergender;
        private TextView tuijiantype;
        private TextView tuijiansum;
        private Button add_friend;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.tuijian_user_name);
            usergender = (TextView) itemView.findViewById(R.id.tuijian_user_gender);
            tuijiantype = (TextView) itemView.findViewById(R.id.tuijian_type);
            tuijiansum = (TextView) itemView.findViewById(R.id.tuijiansum);
            add_friend = (Button) itemView.findViewById(R.id.home_addFriend);
            tuijian_user_head = (ImageView) itemView.findViewById(R.id.tuijian_user_head);
        }
    }

    private void addFriend(final ViewHolder holder, String other_id) {
        OkHttpUtils.get().url(Config.IP + "/friend_addFriend")
                .addParams("request_uid", UserUtil.user.getId())
                .addParams("requested_uid", other_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络连接出错");
                holder.add_friend.setEnabled(true);
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String messageInfo = jsonObject.get("messageInfo").toString();
                    if ("成功".equals(messageInfo)) {
                        ToastUtil.showToast("添加成功");
                    } else if ("服务器异常".equals(messageInfo)) {
                        ToastUtil.showToast(messageInfo);
                        holder.add_friend.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
