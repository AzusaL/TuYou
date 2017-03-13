package com.waibao.team.tuyou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.activity.CommentActivity;
import com.waibao.team.tuyou.activity.LoginActivity;
import com.waibao.team.tuyou.activity.PersonPageActivity;
import com.waibao.team.tuyou.activity.PhotoViewActivity;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.JournalDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.ConstanceUtils;
import com.waibao.team.tuyou.utils.StringUtil;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.utils.UserUtil;
import com.waibao.team.tuyou.widget.searchView.utils.AnimationUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;

/**
 * Created by Azusa on 2016/5/6.
 */
public class TravelsRvAdapter extends RecyclerView.Adapter<TravelsRvAdapter.ViewHolder> {

    private List<JournalDto> datas;
    private Activity mActivity;

    public TravelsRvAdapter(List<JournalDto> datas, Activity activity) {
        this.datas = datas;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_travelsfragment_rv, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //图片集合
        final List<String> imgs = StringUtil.getList(datas.get(position).getImgUrl());

        //头像
        Glide.with(mActivity)
                .load(Config.Pic + StringUtil.parseEmpty(datas.get(position).getUimgUrl()))
                .bitmapTransform(new CropCircleTransformation(mActivity))
                .into(holder.imgHead);

        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, PersonPageActivity.class);
                intent.putExtra("uId", datas.get(position).getUid());
                mActivity.startActivity(intent);
            }
        });

        //名字
        holder.tvName.setText(datas.get(position).getNickname());

        List<String> ways = StringUtil.getList(datas.get(position).getGroup_way());
        //路径、
        holder.tvIntroduction.setText(ways.get(0) + "-" + ways.get(ways.size() - 1));

        //游记大图（第一张）
        Glide.with(mActivity)
                .load(Config.Pic + imgs.get(0))
                .into(holder.mainImg);

        holder.mainImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(mActivity, PhotoViewActivity.class);
                photo.putExtra("photo_count", imgs.size());
                photo.putExtra("photo_position", 0);
                ArrayList<String> tems = new ArrayList<>();
                tems.addAll(imgs);
                photo.putStringArrayListExtra("photourl", tems);
                mActivity.startActivity(photo);
            }
        });

        //游记图片
        if (imgs.size() != 1) {
            holder.imgRecyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(ConstanceUtils.CONTEXT);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.imgRecyclerView.setLayoutManager(layoutManager);
            TravelsImgRvAdapter adapter = new TravelsImgRvAdapter(imgs, 1);
            holder.imgRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View v, int Position) {
                    Intent photo = new Intent(mActivity, PhotoViewActivity.class);
                    photo.putExtra("photo_count", imgs.size());
                    photo.putExtra("photo_position", Position);
                    ArrayList<String> tems = new ArrayList<>();
                    tems.addAll(imgs);
                    photo.putStringArrayListExtra("photourl", tems);
                    mActivity.startActivity(photo);
                }
            });

        } else {
            holder.imgRecyclerView.setVisibility(View.GONE);
        }

        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvContent.setMaxLines(1000);
            }
        });

        //游记标题
        holder.tvTitle.setText(datas.get(position).getTitle());
        //游记内容
        holder.tvContent.setText(datas.get(position).getContent());
        //赞人数
        holder.tvLike.setText(datas.get(position).getZan() + "");
        //评论数
        holder.tvComment.setText(datas.get(position).getCommentCount() + "");
        //收藏数
        holder.tvCollection.setText(datas.get(position).getCollectionCount() + "");
        //点赞监听
        holder.rlLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserUtil.userisLogin) {
                    ToastUtil.showToast("请先登陆");
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    return;
                }
                AnimationUtil.zanAnimation(holder.viewLike, R.drawable.icon_like_highlight);
                OkHttpUtils.get().url(Config.IP + "/user_increaseJournalLikes")
                        .addParams("jid", datas.get(position).getId())
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        ToastUtil.showToast("服务器出错");
                    }

                    @Override
                    public void onResponse(String response) {
                        String result;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result = jsonObject.getString("messageInfo");
                            if (result.equals("成功")) {
                                datas.get(position).setZan(datas.get(position).getZan() + 1);
                                holder.tvLike.setText(datas.get(position).getZan() + "");
                                ToastUtil.showToast("点赞成功！");
                                holder.rlLike.setClickable(false);
                            } else {
                                ToastUtil.showToast("请求失败，请重试");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        //评论按钮监听
        holder.rlComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CommentActivity.class);
                intent.putExtra("id", datas.get(position).getId());
                mActivity.startActivity(intent);
            }
        });

        //收藏按钮监听
        holder.rlCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserUtil.userisLogin) {
                    ToastUtil.showToast("请先登陆");
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                    return;
                }
                AnimationUtil.likeAnimation(holder.viewCollection, R.drawable.ic_collection_light);
                OkHttpUtils.get().url(Config.IP + "/user_collection")
                        .addParams("uid", UserUtil.user.getId())
                        .addParams("type", "0")
                        .addParams("fid", datas.get(position).getId())
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
                                datas.get(position).setCollectionCount(datas.get(position).getCollectionCount() + 1);
                                holder.tvCollection.setText(datas.get(position).getCollectionCount() + "");
                                ToastUtil.showToast("收藏成功！");
                                holder.rlCollection.setClickable(false);
                            } else {
                                ToastUtil.showToast("请求失败，请重试");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgHead;
        private TextView tvName;
        private TextView tvIntroduction;

        private TextView tvTitle;
        private TextView tvMore;
        private TextView tvContent;

        private RelativeLayout rlLike;
        private RelativeLayout rlComment;
        private RelativeLayout rlCollection;

        private TextView tvLike;
        private TextView tvComment;
        private TextView tvCollection;

        private RecyclerView imgRecyclerView;
        private ImageView mainImg;

        private View viewLike;
        private View viewCollection;

        public ViewHolder(View itemView) {
            super(itemView);
            imgHead = (ImageView) itemView.findViewById(R.id.img_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvIntroduction = (TextView) itemView.findViewById(R.id.tv_jianjie);

            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvMore = (TextView) itemView.findViewById(R.id.tv_more);

            rlLike = (RelativeLayout) itemView.findViewById(R.id.rl_like);
            rlComment = (RelativeLayout) itemView.findViewById(R.id.rl_commentcount);
            rlCollection = (RelativeLayout) itemView.findViewById(R.id.rl_collectcount);

            tvLike = (TextView) itemView.findViewById(R.id.tv_like);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);
            tvCollection = (TextView) itemView.findViewById(R.id.tv_collection);

            imgRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview_img);
            mainImg = (ImageView) itemView.findViewById(R.id.main_img);

            viewLike = itemView.findViewById(R.id.view_like);
            viewCollection = itemView.findViewById(R.id.view_collection);
        }
    }
}
