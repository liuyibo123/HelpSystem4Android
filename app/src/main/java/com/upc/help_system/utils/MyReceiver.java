package com.upc.help_system.utils;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.upc.help_system.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/5/31.
 */

public class MyReceiver extends BroadcastReceiver {
    private static String TAG = "pushreceiver";
    private String content;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            System.out.println("收到了自定义消息@@消息内容是:" + content);
            System.out.println("收到了自定义消息@@消息extra是:" + extra);

            //**************解析推送过来的json数据并存放到集合中 begin******************
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(extra);
                String type = jsonObject.getString("type");
                map.put("type", type);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            map.put("content", content);
            Calendar rightNow = Calendar.getInstance();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String date = fmt.format(rightNow.getTime());
            map.put("date", date);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            ClipboardManager clipboardManager = (ClipboardManager)MyApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(ClipData.newPlainText("content",content));
            Toast.makeText(context, "内容已经复制到剪贴板", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}