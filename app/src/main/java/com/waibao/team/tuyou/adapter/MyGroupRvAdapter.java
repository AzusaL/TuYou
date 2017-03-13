package com.waibao.team.tuyou.adapter;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.DisplayUtil;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Azusa on 2016/5/6.
 */
public class MyGroupRvAdapter extends RecyclerView.Adapter<MyGroupRvAdapter.ViewHolder> {

    private List<GroupDto> datas;
    private Fragment context;
    private OnItemClickListener onItemClickListener;
    private int what;

    public MyGroupRvAdapter(List<GroupDto> datas, Fragment context) {
        this.datas = datas;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_mygroup_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        List<String> imgs = StringUtil.getList(datas.get(position).getImgUrl());
        Glide.with(context)
                .load(Config.Pic + StringUtil.parseEmpty(imgs.get(0)))
                .into(holder.imgGrounps);
        List<String> ways = StringUtil.getList(datas.get(position).getWay());
        holder.tvAddress.setText(ways.get(0) + "-" + ways.get(ways.size() - 1));

        final List<String> times = StringUtil.getList(datas.get(position).getWayTime());
        holder.tvTime.setText(times.get(0).substring(5).replace("-", "/") + "-"
                + times.get(times.size() - 1).substring(5).replace("-", "/")
                + " " + datas.get(position).getKm() + "km");

        holder.tvCollectcound.setText(datas.get(position).getCollectionCount() + "");

        holder.tvPeopelcount.setText(datas.get(position).getCurrent_people() + "");

        holder.rl_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hight;
                if(UserUtil.user.getId().equals(datas.get(position).getUid()) && datas.get(position).getStatus() == 0){
                    hight = (int) DisplayUtil.dip2px(105);
                }else {
                    hight = (int) DisplayUtil.dip2px(60);
                }
                View view = View.inflate(ConstanceUtils.CONTEXT, R.layout.item_simpleselectitem, null);
                final PopupWindow mypopupWindow = new PopupWindow(view, (int) DisplayUtil.dip2px(120), hight);
                mypopupWindow.setBackgroundDrawable(ConstanceUtils.resources.getDrawable(R.drawable.bg_container_shadow));
                mypopupWindow.setOutsideTouchable(true);
                mypopupWindow.setFocusable(true);
                mypopupWindow.showAsDropDown(holder.rl_setting);
                mypopupWindow.update();
                TextView tv2 = (TextView) view.findViewById(R.id.tv_item1);
                TextView tv = (TextView) view.findViewById(R.id.tv_item2);
                final String msg;
                if (datas.get(position).getUid().equals(UserUtil.user.getId())) {
                    msg = "取消该行程";
                    what = 1;
                } else {
                    msg = "退出该行程";
                    what = 2;
                }
                tv2.setText(msg);
                if (UserUtil.user.getId().equals(datas.get(position).getUid()) && datas.get(position).getStatus() == 0) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("开始行程");
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mypopupWindow.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
                            builder.setMessage("确认开始行程 ？").setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mypopupWindow.dismiss();
                                }
                            }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startGroup(datas.get(position).getId());
                                }
                            }).show();
                        }
                    });
                } else {
                    tv.setVisibility(View.GONE);
                }

                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mypopupWindow.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context.getActivity());
                        builder.setMessage("确认" + msg + "？").setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mypopupWindow.dismiss();
                            }
                        }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                breakGroup(datas.get(position).getId(), datas.get(position).getTalkId(), what);
                            }
                        }).show();

                    }
                });
            }
        });
        String statusText;
        int status = datas.get(position).getStatus();
        if (status == 0) {
            statusText = "未开始";
        } else if (status == 1) {
            statusText = "进行中";
        } else {
            statusText = "已结束";
        }
        holder.tvStatus.setText(statusText);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }

    /**
     * 解散团,结束行程
     *
     * @param id   团id
     * @param what =1为结束行程，=2为退出行程
     */
    private void breakGroup(final String id, final long talkId, final int what) {
        String ip;
        if (what == 1) {
            ip = "/group_breakGroup";
        } else {
            ip = "/group_exitGroup";
        }
        OkHttpUtils.get().url(Config.IP + ip)
                .addParams("gid", id)
                .addParams("uid", UserUtil.user.getId())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showNetError();
            }

            @Override
            public void onResponse(String response) {
                String result;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    result = jsonObject.getString("messageInfo");
                    if (result.equals("成功")) {
                        if (what == 1) {
                            ToastUtil.showToast("结束成功！");
                            breakJpush(talkId, 1);
                        } else {
                            ToastUtil.showToast("退出成功！");
                            breakJpush(talkId, 2);
                        }
                    } else {
                        ToastUtil.showToast("请求失败，请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 开始行程
     *
     * @param gid 团id
     */
    private void startGroup(String gid) {
        Log.e("main", "startGroup: --------------");
        OkHttpUtils.get().url(Config.IP + "/group_setGroupStatus")
                .addParams("gid", gid)
                .addParams("status", "1")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showNetError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                Log.e("main", "onBefore: " + request.url().toString());
            }

            @Override
            public void onResponse(String response) {
                String result;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    result = jsonObject.getString("messageInfo");
                    if (result.equals("成功")) {
                        ToastUtil.showToast("开始成功！");
                    } else {
                        ToastUtil.showToast("请求失败，请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 退出结束群聊
     *
     * @param talkId 群聊id
     * @param what   1是群解散，2是个人退出
     */
    private void breakJpush(final long talkId, final int what) {
        if (what == 1) {
            JMessageClient.getGroupMembers(talkId, new GetGroupMembersCallback() {
                @Override
                public void gotResult(int code, String s, final List<UserInfo> list) {
                    List<String> removelist = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        removelist.add(list.get(i).getUserName());
                    }
                    JMessageClient.removeGroupMembers(talkId, removelist, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                ToastUtil.showToast("J结束成功！");
                            } else {
                                Log.e("LLLLL", "gotResult: " + s);
                            }
                        }
                    });
                }
            });
        } else if (what == 2) {
            List<String> removelist = new ArrayList<>();
            removelist.add(UserUtil.user.getLoginName());
            JMessageClient.removeGroupMembers(talkId, removelist, new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    if (i == 0) {
                        ToastUtil.showToast("J退出成功！");
                    } else {
                        Log.e("LLLLL", "gotResult: " + s);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_grounps)
        ImageView imgGrounps;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.rl_setting)
        RelativeLayout rl_setting;
        @Bind(R.id.tv_collectcound)
        TextView tvCollectcound;
        @Bind(R.id.tv_peopelcount)
        TextView tvPeopelcount;
        @Bind(R.id.tv_status)
        TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
