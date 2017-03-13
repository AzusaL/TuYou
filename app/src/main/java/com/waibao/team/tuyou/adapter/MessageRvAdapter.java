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
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.MessageDto;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Delete_exe on 2016/6/3.
 */
public class MessageRvAdapter extends RecyclerView.Adapter<MessageRvAdapter.ViewHolder> {
    private Context context;
    private List<MessageDto> messageList;

    public MessageRvAdapter(List<MessageDto> messageList) {
        this.messageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.item_message, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MessageDto messagedto = messageList.get(position);
        final boolean isGroupMessage = 1 == messagedto.getType();
        if (isGroupMessage && UserUtil.user.getId().equals(messagedto.getOwn_id())) {
            holder.content.setText("请求与您的团拼团");
        }
        if (isGroupMessage && !UserUtil.user.getId().equals(messagedto.getOwn_id())) {
            holder.content.setText("拼团请求已处理");
        }
        if (!isGroupMessage && UserUtil.user.getId().equals(messagedto.getOwn_id())) {
            holder.content.setText("请求添加您为好友");
        }
        if (!isGroupMessage && !UserUtil.user.getId().equals(messagedto.getOwn_id())) {
            holder.content.setText("好友请求已处理");
        }
        holder.name.setText(messagedto.getNickname());
        Glide.with(context).load(messagedto.getImgUrl())
                .bitmapTransform(new CropCircleTransformation(context)).into(holder.head_img);
        if (0 == messagedto.getFlag()) {
            holder.sure.setVisibility(View.VISIBLE);
            holder.refuse.setVisibility(View.VISIBLE);
            holder.result.setVisibility(View.GONE);

            holder.sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isGroupMessage) {
                        sendFriendRespone(messagedto.getOther_id(), "2");
                    } else {
                        sendGroupRespone(messagedto.getOwn_gid(), messagedto.getOther_gid(), messagedto.getOther_id(), "2");
                    }
                    holder.sure.setVisibility(View.GONE);
                    holder.refuse.setVisibility(View.GONE);
                    holder.result.setVisibility(View.VISIBLE);
                    holder.result.setText("已同意");
                }
            });
            holder.refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isGroupMessage) {
                        sendFriendRespone(messagedto.getOther_id(), "1");
                    } else {
                        sendGroupRespone(messagedto.getOwn_gid(), messagedto.getOther_gid(), messagedto.getOther_id(), "1");
                    }
                    holder.sure.setVisibility(View.GONE);
                    holder.refuse.setVisibility(View.GONE);
                    holder.result.setVisibility(View.VISIBLE);
                    holder.result.setText("已拒绝");
                }
            });
        } else if (1 == messagedto.getFlag()) {
            holder.sure.setVisibility(View.GONE);
            holder.refuse.setVisibility(View.GONE);
            holder.result.setVisibility(View.VISIBLE);
            holder.result.setText("已拒绝");
        } else if (2 == messagedto.getFlag()) {
            holder.sure.setVisibility(View.GONE);
            holder.refuse.setVisibility(View.GONE);
            holder.result.setVisibility(View.VISIBLE);
            holder.result.setText("已同意");
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView result;
        private TextView content;
        private Button sure;
        private Button refuse;
        private ImageView head_img;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            content = (TextView) itemView.findViewById(R.id.content);
            sure = (Button) itemView.findViewById(R.id.sure);
            refuse = (Button) itemView.findViewById(R.id.refuse);
            result = (TextView) itemView.findViewById(R.id.tx_result);
            head_img = (ImageView) itemView.findViewById(R.id.head_img);
        }
    }

    private void sendFriendRespone(String other_id, String responseCode) {
        OkHttpUtils.get().url(Config.IP + "/friend_manageFriend")
                .addParams("own_id", UserUtil.user.getId())
                .addParams("other_id", other_id)
                .addParams("type", responseCode)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络请求出错");
            }

            @Override
            public void onBefore(Request request) {
                Log.e("LLLLLL", request.url().toString());
                super.onBefore(request);
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    jsonObject.get("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 发送拼团请求的处理结果
     *
     * @param own_gid      请求接收方的gid
     * @param other_gid    请求发起方的gid
     * @param other_id     请求发起方的uid
     * @param responseCode 1拒绝，２同意
     */
    private void sendGroupRespone(String own_gid, String other_gid, String other_id, String responseCode) {
        OkHttpUtils.get().url(Config.IP + "/group_approvalOrDisapproveUnion")
                .addParams("flag", responseCode)
                .addParams("other_gid", other_gid)
                .addParams("own_gid", own_gid)
                .addParams("other_id", other_id)
                .addParams("own_id", UserUtil.user.getId())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络请求出错");
            }

            @Override
            public void onBefore(Request request) {
                Log.e("LLLLL", "onBefore: " + request.url().toString());
                super.onBefore(request);
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    jsonObject.get("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
