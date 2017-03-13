package com.waibao.team.tuyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;

/**
 * Created by Delete_exe on 2016/5/27.
 */
public class RecommendFriendsAdapter extends RecyclerView.Adapter<RecommendFriendsAdapter.ViewHolder> {

    private Context context;
    private List<UserDto> datas;
    private OnItemClickListener onItemClickListener;
    private int type;

    public RecommendFriendsAdapter(List<UserDto> datas, int type) {
        this.datas = datas;
        this.type = type;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.item_recommend_friends, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserDto userDto = datas.get(position);
        holder.darentuijianUserName.setText(userDto.getNickname());
        holder.darentuijianUserGender.setText(userDto.getAge() + "");
        int res = "2".equals(userDto.getSex()) ? R.drawable.male_shape : R.drawable.female_shape;
        holder.darentuijianUserGender.setBackgroundResource(res);
        String sum = type == 1 ? userDto.getKm() + "km" : userDto.getTimes() + "次";
        Glide.with(context).load(Config.Pic + userDto.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(context)).into(holder.user_head);
        holder.darentuijiansum.setText(sum);
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


    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.darentuijian_user_name)
        TextView darentuijianUserName;
        @Bind(R.id.darentuijian_user_gender)
        TextView darentuijianUserGender;
        @Bind(R.id.darentuijiansum)
        TextView darentuijiansum;
        @Bind(R.id.add_friend)
        Button add_friend;
        @Bind(R.id.darentuijian_user_head)
        ImageView user_head;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
