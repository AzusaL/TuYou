package com.waibao.team.tuyou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.ConversationGroupRvAdapter;
import com.waibao.team.tuyou.adapter.ConversationRvAdapter;
import com.waibao.team.tuyou.adapter.TripgroupFriendsRvAdapter;
import com.waibao.team.tuyou.callback.UsersCallback;
import com.waibao.team.tuyou.config.Config;
import com.waibao.team.tuyou.dto.GroupDto;
import com.waibao.team.tuyou.dto.UserDto;
import com.waibao.team.tuyou.utils.JsonUtils;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.MyDialog;
import com.waibao.team.tuyou.widget.ToolBarBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import okhttp3.Call;

/**
 * Created by Delete_exe on 2016/6/2.
 */
public class ConversationGroupActivity extends BaseActivity {

    @Bind(R.id.conversation_toolbar)
    Toolbar toolbar;
    @Bind(R.id.input_edit)
    EditText inputEdit;
    @Bind(R.id.conversation_list)
    RecyclerView MessageList;

    private List<UserDto> users = new ArrayList<>();
    private MyDialog myDialog = new MyDialog();
    private Dialog sendmore_dialog;
    private List<Message> messages;
    private ConversationGroupRvAdapter adapter;
    private long JgroupID;
    private String groupDtoJson;
    private GroupDto groupDto;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    adapter.notifyItemInserted(messages.size() - 1);
                    MessageList.smoothScrollToPosition(messages.size() - 1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        JMessageClient.registerEventReceiver(this);

        JgroupID = getIntent().getLongExtra("JgroupID", 0);
        groupDtoJson = getIntent().getStringExtra("groupJson");
        groupDto = JsonUtils.getObjectfromString(groupDtoJson, GroupDto.class);
        String[] ways = groupDto.getWay().split(";");
        String str_way = ways[0] + "-" + ways[ways.length - 1];
        ToolBarBuilder builder = new ToolBarBuilder(this, toolbar);
        builder.setTitle(str_way + "(" + JgroupID + ")").build();

        Conversation conversation = JMessageClient.getGroupConversation(JgroupID);
        if (conversation == null) {
            messages = new ArrayList<>();
        } else {
            messages = conversation.getAllMessage();
        }

        initView();
        adjustRVBySoftInput();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_conversation;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        MessageList.setLayoutManager(layoutManager);
        MessageList.setItemAnimator(new DefaultItemAnimator());
        getData();
    }


    private void getData() {
        final Dialog dialog = new MyDialog().showLodingDialog(this);
        OkHttpUtils.get().url(Config.IP + "/group_getAllGroupFriend")
                .addParams("gid", groupDto.getId())
                .build().execute(new UsersCallback() {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.showToast("网络链接出错");
                dialog.cancel();
            }

            @Override
            public void onResponse(final List<UserDto> response) {
                dialog.cancel();
                users.addAll(response);
                adapter = new ConversationGroupRvAdapter(messages, users);
                MessageList.setAdapter(adapter);
                if (messages.size() > 0) {
                    MessageList.smoothScrollToPosition(messages.size() - 1);
                }
            }
        });
    }

    /**
     * 监听输入法弹出，调整RecyclerView
     */
    private void adjustRVBySoftInput() {
        final View activityRootView = findViewById(R.id.activityRoot);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int heightDiff = activityRootView.getRootView()
                                .getHeight() - activityRootView.getHeight();
                        if (heightDiff > 0 && messages.size() > 0) {
                            MessageList.smoothScrollToPosition(messages.size() - 1);
                        }
                    }
                });
    }

    @OnClick({R.id.pic, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic:
                sendmore_dialog = myDialog.showConversationMore(this);
                LinearLayout send_img = (LinearLayout) sendmore_dialog.findViewById(R.id.send_img);
                LinearLayout send_vote = (LinearLayout) sendmore_dialog.findViewById(R.id.send_vote);
                send_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast("发送图片");
                        sendmore_dialog.dismiss();
                    }
                });
                send_vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showToast("发起投票");
                        sendmore_dialog.dismiss();
                        Intent intent = new Intent(ConversationGroupActivity.this, AllGroupVoteActivity.class);
                        intent.putExtra("gid",groupDto.getId());
                        startActivity(intent);
                    }
                });

                break;
            case R.id.send:
                String content = inputEdit.getText().toString();
                if (content.length() == 0 || null == content) {
                    return;
                }
                cn.jpush.im.android.api.model.Message message = JMessageClient.createGroupTextMessage(JgroupID, content);
                messages.add(message);
                inputEdit.setText("");
                adapter.notifyItemInserted(messages.size() - 1);
                MessageList.smoothScrollToPosition(messages.size() - 1);
                JMessageClient.sendMessage(message);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();

        switch (msg.getContentType()) {
            case text:
                //处理文字消息
//                TextContent textContent = (TextContent) msg.getContent();
                messages.add(msg);
                handler.sendEmptyMessage(3);
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件（只有被踢的用户能收到此事件）
                        break;
                    case group_member_exit:
                        //群成员退群事件（已弃用）
                        break;
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JMessageClient.exitConversation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 进入会话状态，JgroupID为会话对象
         */
        JMessageClient.enterGroupConversation(JgroupID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }
}
