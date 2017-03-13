package com.waibao.team.tuyou.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.waibao.team.tuyou.event.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Delete_exe on 2016/6/3.
 */
public class JPReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 推送消息
         */
        if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            EventBus.getDefault().post(new MessageEvent(true));
            Log.e("LLLLLLLLLL", "onMessage: " + "title:" + title + "\nmessage:" + message);
        }
        /**
         * 推送通知
         */
        if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String notification = bundle.getString(JPushInterface.EXTRA_ALERT);
            Log.e("LLLLLLLLL", "onNotification: " + "title:" + title + "\nnotification:" + notification);
        }
        /**
         * 点击打开通知
         */
        if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            Log.e("LLLLLLLLLL", "onOpened: ");
//            Intent i = new Intent(context, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
        }
    }
}
