package com.upc.help_system.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.Stetho;
import com.upc.help_system.R;
import com.upc.help_system.events.NewOrderEvent;
import com.upc.help_system.presenter.MainPresenter;
import com.upc.help_system.presenter.MainPresenterImpl;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;




public class MainActivity extends FragmentActivity {

    @BindView(R.id.myorder_btn)
    ImageButton myorderBtn;
    @BindView(R.id.help_btn)
    ImageButton helpBtn;
    @BindView(R.id.community_btn)
    ImageButton communityBtn;
    @BindView(R.id.love_btn)
    ImageButton loveBtn;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    MainPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PubActivity.class));
            }
        });

    }

    void init() {
        SDKInitializer.initialize(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Stetho.initializeWithDefaults(this);
        presenter = new MainPresenterImpl();
    }

    @Subscribe
    public void onReceiveNewOrder(NewOrderEvent event) {
        presenter.onNewOrder();
    }
}



