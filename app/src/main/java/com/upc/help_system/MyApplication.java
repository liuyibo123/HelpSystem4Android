package com.upc.help_system;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.Stetho;

import org.greenrobot.eventbus.EventBus;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/5/27.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //获取Context
        context = getApplicationContext();
        SDKInitializer.initialize(context);
    }

    //返回
    public static Context getContext() {
        return context;
    }

}
