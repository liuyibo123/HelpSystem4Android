package com.upc.help_system;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.Stetho;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.mob.MobApplication;
import com.upc.help_system.utils.BDLocationUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/5/27.
 */

public class MyApplication extends MobApplication {
    private static Context context;
    private boolean isInit = false;
    @Override
    public void onCreate() {
        super.onCreate();
        //获取Context
        context = getApplicationContext();
        EaseUI.getInstance().init(this,null);
        SDKInitializer.initialize(getApplicationContext());
        BDLocationUtil.getCurrentLoc();
        //initEasemob();
    }

    //返回
    public static Context getContext() {
        return context;
    }

}
