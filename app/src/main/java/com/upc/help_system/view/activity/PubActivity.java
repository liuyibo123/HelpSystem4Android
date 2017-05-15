package com.upc.help_system.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;

import com.upc.help_system.R;
import com.upc.help_system.events.FirstCallFinsh;
import com.upc.help_system.presenter.PubPresenter;
import com.upc.help_system.presenter.PubPresenterImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PubActivity extends FragmentActivity {
    @BindView(R.id.tabs)
    public TabLayout tabs;
    @BindView(R.id.appbar)
    public AppBarLayout appbar;
    @BindView(R.id.frame_pub)
    public NestedScrollView framePub;
    PubPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        presenter = new PubPresenterImpl(this);
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String s = tab.getText().toString();
                presenter.OnTabClicked(s);
                }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                presenter.OnTabClicked("取快递");
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Subscribe
    public void onExpressFirstFinsh(FirstCallFinsh firstCallFinsh) {
        presenter.onExpressFirstFinsh();
    }
}
