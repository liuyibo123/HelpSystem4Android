package com.upc.help_system.presenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.upc.help_system.R;
import com.upc.help_system.model.Express;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.presenter.adapter.OrderAdapter;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.view.activity.DetailActivity;
import com.upc.help_system.view.activity.MainActivity;

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
    private MainActivity view;
    OrdersViewHolder ordersholder;
    Retrofit retrofit;
    RequestService requestService;

    public MainPresenterImpl(MainActivity view) {
        if (view != null) {
            this.view = view;
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(ConConfig.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        requestService = retrofit.create(RequestService.class);

    }

    public void showOrders() {
        Log.d("MainPresenterImpl", "showorders");
        ordersholder = new OrdersViewHolder();
        View myview = ordersholder.myView;
        view.frameLayout.addView(myview);
        refresh();
        ordersholder.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        ordersholder.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Call<List<MainTable>> call = requestService.getOrdersByContent(query);
                call.enqueue(new Callback<List<MainTable>>() {
                    @Override
                    public void onResponse(Call<List<MainTable>> call, Response<List<MainTable>> response) {

                        ordersholder.recyclerView.setAdapter(new OrderAdapter(response.body().size(), new OrderAdapter.ListItemClickListener() {
                            @Override
                            public void onListItemClick(int itemIndex) {
                                MainTable table = response.body().get(itemIndex);
                                ItemClick(table);
                            }
                        }, response.body()));
                    }

                    @Override
                    public void onFailure(Call<List<MainTable>> call, Throwable t) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void ItemClick(MainTable table) {
        int id = table.getId();
        Call<Express> call_express = requestService.getExpressById(id);
        call_express.enqueue(new Callback<Express>() {
            @Override
            public void onResponse(Call<Express> call, Response<Express> response) {
                Express express = response.body();
                Intent intent = new Intent(view, DetailActivity.class);
                String pub_pserson = table.getPub_person();
                String phone_number = express.getPhone_number();
                float tip = table.getTip();
                String pub_loc = table.getPub_loc();
                String help_loc = table.getHelp_loc();
                String content = table.getContent();
                String accept_person = table.getAccept_person();
                intent.putExtra("id", table.getId());
                intent.putExtra("pubperson", pub_pserson);
                intent.putExtra("phonenumber", phone_number);
                intent.putExtra("tip", tip);
                intent.putExtra("content", content);
                intent.putExtra("acceptperson", accept_person);
                view.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Express> call, Throwable t) {

            }
        });
    }

    @Override
    public void onNewOrder() {

    }

    void refresh() {
        Call<List<MainTable>> call = requestService.getOrders();
        call.enqueue(new Callback<List<MainTable>>() {
            @Override
            public void onResponse(Call<List<MainTable>> call, Response<List<MainTable>> response) {
                Log.d("MainPresenterImpl", "response.body():" + response.body());

                OrderAdapter.ListItemClickListener listener = new OrderAdapter.ListItemClickListener() {
                    @Override
                    public void onListItemClick(int itemIndex) {
                        MainTable table = response.body().get(itemIndex);
                        ItemClick(table);
                    }
                };
                if (response.body() != null) {
                    ordersholder.recyclerView.setAdapter(new OrderAdapter(response.body().size(), listener, response.body()));
                }
                ordersholder.swiperefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<MainTable>> call, Throwable t) {
                Toast.makeText(view, "错误：" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class OrdersViewHolder {
        View myView;
        RecyclerView recyclerView;
        Spinner timeSpinner;
        Spinner help_locSpinner;
        Spinner cataSpinner;
        Spinner tipSpinner;
        SearchView searchView;
        SwipeRefreshLayout swiperefresh;

        public OrdersViewHolder() {
            myView = view.getLayoutInflater().inflate(R.layout.fragment_orders, null);
            recyclerView = (RecyclerView) myView.findViewById(R.id.recycler);
//            timeSpinner = (Spinner) myView.findViewById(R.id.spinner_time);
//            help_locSpinner = (Spinner) myView.findViewById(R.id.spinner_helploc);
//            cataSpinner = (Spinner) myView.findViewById(R.id.spinner_cata);
//             tipSpinner = (Spinner) myView.findViewById(R.id.spinner_tip);
            searchView = (SearchView) myView.findViewById(R.id.searchView);
            swiperefresh = (SwipeRefreshLayout) myView.findViewById(R.id.swiperefresh);
            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) searchView.findViewById(id);
            textView.setTextColor(Color.BLACK);
            textView.setHintTextColor(Color.parseColor("#CCCCCC"));
            LinearLayoutManager layoutManager = new LinearLayoutManager(view);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
