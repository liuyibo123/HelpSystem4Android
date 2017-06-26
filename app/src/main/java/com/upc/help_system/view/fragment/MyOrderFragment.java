package com.upc.help_system.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.view.activity.DetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.upc.help_system.utils.network.MyRetrofit.requestService;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderFragment extends Fragment {

    View myView;
    MyOrdersViewHolder myordersholder;
    String username;
    String sex;
    public MyOrderFragment() {
        username = SharedPreferenceUtil.getString("user","name");
        sex = SharedPreferenceUtil.getString("user","sex");
    }

    @Override
    public void onResume() {
        super.onResume();
        username = SharedPreferenceUtil.getString("user","name");
        sex = SharedPreferenceUtil.getString("user","sex");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView =getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_myorders, null);
        if(myordersholder==null){
            myordersholder = new MyOrdersViewHolder();
        }
        init();
        return myView;
    }
    public void init(){
        if (sex.equals("女")) {
            Resources resources = MyApplication.getContext().getResources();
            Drawable drawable = resources.getDrawable(R.drawable.ic_girl_48);
            myordersholder.img.setImageDrawable(drawable);
        } else {
            Resources resources = MyApplication.getContext().getResources();
            Drawable drawable = resources.getDrawable(R.drawable.ic_boy_48);
            myordersholder.img.setImageDrawable(drawable);
        }
        myorderrefresh();
        myordersholder.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myorderrefresh();
            }
        });

    }
    private void myorderrefresh() {
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username = sp.getString("name","");
        Log.d("MainPresenterImpl", username);
        Call<List<MainTable>> call = requestService.getOrdersByPerson(username);
        call.enqueue(new Callback<List<MainTable>>() {
            @Override
            public void onResponse(Call<List<MainTable>> call, Response<List<MainTable>> response) {
                Log.d("MainPresenterImpl", "response.body():" + MyGson.toJson(response.body()));
                MyOrderAdapter.ListItemClickListener listener = new MyOrderAdapter.ListItemClickListener() {
                    @Override
                    public void onListItemClick(int itemIndex) {
                        MainTable table = response.body().get(itemIndex);
                        ItemClick(table);
                    }
                };
                if (response.body()!=null&&response.body().size() != 0) {
                    myordersholder.recyclerView.setAdapter(new MyOrderAdapter(response.body().size(), listener, response.body()));
                }
                myordersholder.swiperefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<MainTable>> call, Throwable t) {
                Toast.makeText(getActivity(), "错误：" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ItemClick(MainTable table) {
        int id = table.getId();
        int cata = table.getCatagory();
        switch (cata) {
            case 1:
                Call<Express> call_express = requestService.getExpressById(id);
                call_express.enqueue(new Callback<Express>() {
                    @Override
                    public void onResponse(Call<Express> call, Response<Express> response) {
                        Express express = response.body();
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
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
                        intent.putExtra("location",pub_loc);
                        intent.putExtra("tip", tip);
                        intent.putExtra("content", content);
                        intent.putExtra("acceptperson", accept_person);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Express> call, Throwable t) {

                    }
                });
                break;
            case 2:
                Call<Food> call_food = requestService.getFoodById(id);
                call_food.enqueue(new Callback<Food>() {
                    @Override
                    public void onResponse(Call<Food> call, Response<Food> response) {
                        Food express = response.body();
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        String pub_pserson = table.getPub_person();
                        float tip = table.getTip();
                        String pub_loc = table.getPub_loc();
                        String help_loc = table.getHelp_loc();
                        String content = table.getContent();
                        String accept_person = table.getAccept_person();
                        String phone = express.getPhone();
                        intent.putExtra("id", table.getId());
                        intent.putExtra("pubperson", pub_pserson);
                        intent.putExtra("tip", tip);
                        intent.putExtra("content", content);
                        intent.putExtra("acceptperson", accept_person);
                        intent.putExtra("phonenumber", phone);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Food> call, Throwable t) {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 3:
                Call<Homework> call_work = requestService.getHomeWorkById(id);
                call_work.enqueue(new Callback<Homework>() {
                    @Override
                    public void onResponse(Call<Homework> call, Response<Homework> response) {
                        Homework express = response.body();
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        String pub_pserson = table.getPub_person();
                        float tip = table.getTip();
                        String pub_loc = table.getPub_loc();
                        String help_loc = table.getHelp_loc();
                        String content = table.getContent();
                        String accept_person = table.getAccept_person();
                        String phone = express.getPhone();
                        intent.putExtra("id", table.getId());
                        intent.putExtra("pubperson", pub_pserson);
                        intent.putExtra("tip", tip);
                        intent.putExtra("content", content);
                        intent.putExtra("acceptperson", accept_person);
                        intent.putExtra("phonenumber", phone);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Homework> call, Throwable t) {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 4:
                Call<Buy> call_buy = requestService.getBuyById(id);
                call_buy.enqueue(new Callback<Buy>() {
                    @Override
                    public void onResponse(Call<Buy> call, Response<Buy> response) {
                        Buy express = response.body();
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        String pub_pserson = table.getPub_person();
                        float tip = table.getTip();
                        String pub_loc = table.getPub_loc();
                        String help_loc = table.getHelp_loc();
                        String content = table.getContent();
                        String accept_person = table.getAccept_person();
                        String phone = express.getPhone();
                        intent.putExtra("id", table.getId());
                        intent.putExtra("pubperson", pub_pserson);
                        intent.putExtra("tip", tip);
                        intent.putExtra("content", content);
                        intent.putExtra("acceptperson", accept_person);
                        intent.putExtra("phonenumber", phone);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Buy> call, Throwable t) {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 5:
                Call<Others> call_other = requestService.getOthersById(id);
                call_other.enqueue(new Callback<Others>() {
                    @Override
                    public void onResponse(Call<Others> call, Response<Others> response) {
                        Others express = response.body();
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        String pub_pserson = table.getPub_person();
                        float tip = table.getTip();
                        String pub_loc = table.getPub_loc();
                        String help_loc = table.getHelp_loc();
                        String content = table.getContent();
                        String accept_person = table.getAccept_person();
                        String phone = express.getPhone();
                        intent.putExtra("id", table.getId());
                        intent.putExtra("pubperson", pub_pserson);
                        intent.putExtra("tip", tip);
                        intent.putExtra("content", content);
                        intent.putExtra("acceptperson", accept_person);
                        intent.putExtra("phonenumber", phone);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Others> call, Throwable t) {
                        Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

    }
    class MyOrdersViewHolder {

        RecyclerView recyclerView;
        SwipeRefreshLayout swiperefresh;
        ImageView img;

        public MyOrdersViewHolder() {

            recyclerView = (RecyclerView) myView.findViewById(R.id.recycler);
            swiperefresh = (SwipeRefreshLayout) myView.findViewById(R.id.swiperefresh);
            img = (ImageView) myView.findViewById(R.id.head);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
        }
    }

}
