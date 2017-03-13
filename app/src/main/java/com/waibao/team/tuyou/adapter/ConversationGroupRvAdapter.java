package com.waibao.team.tuyou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.listener.OnItemClickListener;
import com.waibao.team.tuyou.utils.DateUtil;
import com.waibao.team.tuyou.utils.UserUtil;

import java.util.List;

import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Message;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Delete_exe on 2016/6/13.
 */
public class ConversationGroupRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int other = -1;
    private static final int text_from = 0;
    private static final int text_to = 1;
    private static final int vote_from = 2;
    private static final int vote_to = 3;

    private Context context;
    private List<Message> messages;
    private List<UserDto> users;
    private OnItemClickListener onItemClickListener;

    public ConversationGroupRvAdapter(List<Message> messages, List<UserDto> users) {
        this.messages = messages;
        this.users = users;
//        Log.e("LLLLLLL", "ConversationGroupRvAdapter: "+messages);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view;
        switch (viewType) {
            case text_from:
                view = View.inflate(parent.getContext(), R.layout.item_conversation_come, null);
                ViewHolderText holderTesxt_come = new ViewHolderText(view);
                return holderTesxt_come;
            case text_to:
                view = View.inflate(parent.getContext(), R.layout.item_conversation_to, null);
                ViewHolderText holderTesxt_to = new ViewHolderText(view);
                return holderTesxt_to;
            case vote_from:
                view = View.inflate(parent.getContext(), R.layout.item_vote_come, null);
                ViewHolderVote holderVote_come = new ViewHolderVote(view);
                return holderVote_come;
            case vote_to:
                view = View.inflate(parent.getContext(), R.layout.item_vote_to, null);
                ViewHolderVote holderVote_to = new ViewHolderVote(view);
                return holderVote_to;
            case other:
                Log.e("LLLLL", "onCreateViewHolder: null");
                return new ViewHolderrrr(new View(context));
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.e("LLLL", "getItemViewType: " + messages.get(position).getFromUser().getUserName());
        Log.e("LLLL", "getContentType: " + messages.get(position).getContentType());
        if (messages.get(position).getFromUser().getUserName().equals(UserUtil.user.getLoginName())) {//是自己发消息
            switch (messages.get(position).getContentType()) {
                case text://是文字消息
                    return text_to;
                case custom://是投票消息
                    return vote_to;
                default:
                    return other;
            }
        } else {//是收到消息
            switch (messages.get(position).getContentType()) {
                case text://是文字消息
                    return text_from;
                case custom://是投票消息
                    return vote_from;
                default:
                    return other;
            }
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Message message = messages.get(position);
        if (holder instanceof ViewHolderText) {
            TextContent textContent = (TextContent) message.getContent();
            ((ViewHolderText) holder).content.setText(textContent.getText());
            Glide.with(context).load(Config.Pic + getImgUrl(message.getFromUser().getUserName()))
                    .bitmapTransform(new CropCircleTransformation(context)).into(((ViewHolderText) holder).headImg);
            ((ViewHolderText) holder).time.setText(DateUtil.getStringByFormat(message.getCreateTime(), DateUtil.dateFormatYMDHMS));
            if (getItemViewType(position) == text_from) {
                ((ViewHolderText) holder).nick_name.setText(message.getFromUser().getNickname() + ":");
            } else if (getItemViewType(position) == text_to) {
                ((ViewHolderText) holder).nick_name.setText("");
            }
        }
        if (holder instanceof ViewHolderVote) {
            CustomContent voteContent = (CustomContent) message.getContent();
            ((ViewHolderVote) holder).voteTitle.setText(voteContent.getStringValue("title"));
            Glide.with(context).load(Config.Pic + getImgUrl(message.getFromUser().getUserName()))
                    .bitmapTransform(new CropCircleTransformation(context)).into(((ViewHolderVote) holder).headImg);
            ((ViewHolderVote) holder).time.setText(DateUtil.getStringByFormat(message.getCreateTime(), DateUtil.dateFormatYMDHMS));
            if (getItemViewType(position) == vote_from) {
                ((ViewHolderVote) holder).nickName.setText(message.getFromUser().getNickname() + ":");
            } else if (getItemViewType(position) == vote_to) {
                ((ViewHolderVote) holder).nickName.setText("");
            }
        }
    }


    private String getImgUrl(String loginname) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getLoginName().equals(loginname)) {
                return users.get(i).getImgUrl();
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private static class ViewHolderText extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView content;
        private ImageView headImg;
        private TextView nick_name;

        public ViewHolderText(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.conversation_time);
            content = (TextView) itemView.findViewById(R.id.conver_content);
            headImg = (ImageView) itemView.findViewById(R.id.head_img);
            nick_name = (TextView) itemView.findViewById(R.id.nick_name);
        }
    }


    private static class ViewHolderVote extends RecyclerView.ViewHolder {
        private TextView time;
        private ImageView headImg;
        private TextView nickName;
        private TextView voteTitle;

        public ViewHolderVote(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.conversation_time);
            headImg = (ImageView) view.findViewById(R.id.head_img);
            nickName = (TextView) view.findViewById(R.id.nick_name);
            voteTitle = (TextView) view.findViewById(R.id.vote_title);
        }
    }

    private static class ViewHolderrrr extends RecyclerView.ViewHolder {
        public ViewHolderrrr(View v) {
            super(v);
        }
    }
}
