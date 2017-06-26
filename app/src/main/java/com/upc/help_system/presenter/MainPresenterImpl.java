package com.upc.help_system.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.upc.help_system.MyApplication;
import com.upc.help_system.R;
import com.upc.help_system.model.Buy;
import com.upc.help_system.model.Express;
import com.upc.help_system.model.Food;
import com.upc.help_system.model.Homework;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.model.Others;
import com.upc.help_system.presenter.adapter.MyOrderAdapter;
import com.upc.help_system.presenter.adapter.OrderAdapter;
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.MyRetrofit;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.view.activity.CirclePostFragment;
import com.upc.help_system.view.activity.DetailActivity;
import com.upc.help_system.view.activity.FragmentCircle;
import com.upc.help_system.view.activity.MainActivity;
import com.upc.help_system.view.fragment.MyOrderFragment;
import com.upc.help_system.view.fragment.OrderFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by Administrator on 2017/5/15.
 */

public class MainPresenterImpl implements MainPresenter {

    MainActivity view;
    public MainPresenterImpl(MainActivity view) {
        if (view != null) {
            this.view = view;
        }
    }
    public void showMyOrders() {
        view.getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new MyOrderFragment()).commit();
    }

    @Override
    public void showSocial() {
        view.getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new FragmentCircle()).commit();

    }

    public void showOrders() {
        view.getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new OrderFragment()).commit();
    }


}
