package com.waibao.team.tuyou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.waibao.team.tuyou.R;
import com.waibao.team.tuyou.adapter.ConversationRvAdapter;
import com.waibao.team.tuyou.utils.ToastUtil;
import com.waibao.team.tuyou.widget.ToolBarBuilder;

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

public class ConversationActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @Bind(R.id.conversation_list)
    RecyclerView converstaion_list;
    @Bind(R.id.conversation_toolbar)
    Toolbar toolbar;
    @Bind(R.id.input_edit)
    EditText inputEdit;

    private String uImgUrl;
    private String userLoginname;
    private String username;
    private ConversationRvAdapter adapter;
    private Conversation conversation;
    private List<cn.jpush.im.android.api.model.Message> messages;
    private ToolBarBuilder builder;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    break;
                case 2:
//                    conversationSwipe.setRefreshing(false);
                    break;
                case 3:
                    adapter.notifyItemInserted(messages.size() - 1);
                    converstaion_list.smoothScrollToPosition(messages.size() - 1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        JMessageClient.registerEventReceiver(this);
        uImgUrl = getIntent().getStringExtra("uImgUrl");
        userLoginname = getIntent().getStringExtra("userLoginName");
        username = getIntent().getStringExtra("userName");
        builder = new ToolBarBuilder(ConversationActivity.this, toolbar);
        builder.setCanBack(true).setTitle(username + "(" + userLoginname + ")").build();
        conversation = JMessageClient.getSingleConversation(userLoginname);
        if (conversation == null) {
            messages = new ArrayList<>();
        } else {
            messages = conversation.getAllMessage();
        }
        initView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_conversation;
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        converstaion_list.setLayoutManager(layoutManager);
        converstaion_list.setItemAnimator(new DefaultItemAnimator());

        adapter = new ConversationRvAdapter(messages,uImgUrl);
        converstaion_list.setAdapter(adapter);

        if (messages.size() > 0) {
            converstaion_list.smoothScrollToPosition(messages.size() - 1);
        }
        adjustRVBySoftInput();
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
                            converstaion_list.smoothScrollToPosition(messages.size() - 1);
                        }
                    }
                });
    }

    @OnClick({R.id.pic, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pic:
                ToastUtil.showToast("pic");
                break;
            case R.id.send:
                String content = inputEdit.getText().toString();
                if (content.length() == 0 || null == content) {
                    return;
                }
                cn.jpush.im.android.api.model.Message message = JMessageClient.createSingleTextMessage(userLoginname, content);
                messages.add(message);
                inputEdit.setText("");
                adapter.notifyItemInserted(messages.size() - 1);
                converstaion_list.smoothScrollToPosition(messages.size() - 1);
                JMessageClient.sendMessage(message);
                break;

        }
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
         * 进入会话状态，username为会话对象
         */
        JMessageClient.enterSingleConversation(userLoginname);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_people_center, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 点击toolbar上的发送按钮
        if (item.getItemId() == R.id.action_people_center) {
            ToastUtil.showToast("个人详情页面");
        }
        return super.onOptionsItemSelected(item);
    }
}
