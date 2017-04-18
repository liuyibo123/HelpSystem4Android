package com.upc.help_system.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.stetho.Stetho;
import com.upc.help_system.R;
import com.upc.help_system.presenter.MainPresenter;
import com.upc.help_system.presenter.impl.MainPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    private MainPresenter presenter = new MainPresenterImpl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter.setNavigationViewItemSelectedListener(navigationView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

    }

    @OnClick({R.id.myorder_btn, R.id.help_btn, R.id.community_btn, R.id.love_btn,R.id.fab})
    public void onClick(View view) {
        presenter.click(view.getId());
        }
}

