package com.upc.help_system.presenter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.upc.help_system.BuildConfig;
import com.upc.help_system.R;
import com.upc.help_system.model.Buy;
import com.upc.help_system.model.Express;
import com.upc.help_system.model.Food;
import com.upc.help_system.model.Homework;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.model.Others;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.MyLoction;
import com.upc.help_system.utils.TimeUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.MyResponse;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;
import com.upc.help_system.view.activity.PubActivity;
import com.upc.help_system.view.fragment.ExpressFragment;
import com.upc.help_system.view.fragment.HomeWorkFragment;
import com.upc.help_system.view.fragment.TakeFoodFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/26.
 */

public class PubPresenterImpl implements PubPresenter {
    PubActivity view;
    Express express_temp = new Express();
    Food food_temp = new Food();
    Homework homework_temp = new Homework();
    Buy buy_temp = new Buy();
    Others others_temp = new Others();
    String username;
    String phone;
    MyLoction loc = null;

    public PubPresenterImpl(PubActivity view) {
        this.view = view;
        SharedPreferences sharedPreferences = view.getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        phone = sharedPreferences.getString("phone", "");
        SharedPreferences sp = view.getSharedPreferences("location", Context.MODE_PRIVATE);
        String myloc = sp.getString("location", "");
        if (myloc != null && !myloc.equals("")) {
            loc = MyGson.fromJson(myloc, MyLoction.class);
        }
        showExpress();
    }

    @Override
    public void OnTabClicked(String s) {
        switch (s) {
            case "取快递":
                showExpress();
                break;
            case "带饭":
                showTakeFood();
                break;
            case "作业帮":
                showHomeWork();
                break;
            case "代买":
                showBuy();
                break;
            case "其他":
                showOther();
                break;
        }
    }

    public void showExpress() {
        view.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ExpressFragment()).commit();
    }
    public void showTakeFood() {
        view.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new TakeFoodFragment()).commit();
    }
    public void showHomeWork() {
        view.getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new HomeWorkFragment()).commit();
    }
    public void showBuy() {
        View v = view.getLayoutInflater().inflate(R.layout.fragment_buy, null);//fragment_buy
        view.framePub.removeAllViews();
        view.framePub.addView(v);
        Button button = (Button) v.findViewById(R.id.btn_makesure);
        EditText buy_pot = (EditText) v.findViewById(R.id.buy_pot);//代购点——buy_pot
        EditText buy_thing = (EditText) v.findViewById(R.id.buy_thing);//商品
        final EditText phone_number = (EditText) v.findViewById(R.id.phone_number_content);
        phone_number.setText(phone);
        EditText Price = (EditText) v.findViewById(R.id.price);//商品价格
        EditText Pay_for = (EditText) v.findViewById(R.id.pay_for);//支付方式
        EditText DeadLine = (EditText) v.findViewById(R.id.deadline_content);//截至时间
        EditText Fee = (EditText) v.findViewById(R.id.fee);//酬金
        Button btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                MainTable table = new MainTable();
                table.setContent("帮购物：" + buy_pot.getText().toString());//显示帮买
                //TODO （1）百度地图自动获取位置
//                String loction = view.getSharedPreferences("location", Context.MODE_PRIVATE).getString("location", "获取位置失败");
//                MyLoction myLoction = MyGson.fromJson(loction, MyLoction.class);
//                table.setPub_loc(myLoction.getAddr());
                //TODO　(2) 用户类
                table.setPub_person(username);
                table.setPub_time(TimeUtil.getCurrentTime());
                table.setCatagory(Container.BUY);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestService service = retrofit.create(RequestService.class);
                Call<MyResponse> call = service.addMainTable(table);
                final Call<Void> call2 = service.addBuy(buy_temp);//添加项目——buy_temp
                Log.d("PubPresenterImpl", "table:" + gson.toJson(table));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response<MyResponse> response = null;
                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        buy_temp.setId(response.body().getValue());
                        buy_temp.setPrice(1);
                        buy_temp.setGoods(buy_thing.getText().toString());
                        buy_temp.setShop(buy_pot.getText().toString());
                        buy_temp.setMoney_method(1);
                        buy_temp.setPhone(phone_number.getText().toString());
                        buy_temp.setTime(DeadLine.getText().toString());
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
                                Snackbar.make(view.getWindow().getDecorView(), "发布成功", Snackbar.LENGTH_SHORT).show();
                                button.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
                                Toast.makeText(view, "连接不到服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.finish();
            }
        });
    }
    public void showOther() {
        //TODO 写其他的东西
        View v = view.getLayoutInflater().inflate(R.layout.fragment_others, null);//fragment_other
        view.framePub.removeAllViews();
        view.framePub.addView(v);
        Button button = (Button) view.findViewById(R.id.btn_makesure);
        EditText other_content = (EditText) v.findViewById(R.id.other_content);//求助信息内容
        EditText phone_number = (EditText) v.findViewById(R.id.phone_number_content);
        phone_number.setText(phone);
        EditText Fee = (EditText) v.findViewById(R.id.fee);//酬金
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                MainTable table = new MainTable();
                table.setContent("帮做其他事：" + other_content.getText().toString());//显示信息

                //TODO （1）百度地图自动获取位置
//                String loction = view.getSharedPreferences("location", Context.MODE_PRIVATE).getString("location", "获取位置失败");
//                MyLoction myLoction = MyGson.fromJson(loction, MyLoction.class);
//                table.setPub_loc(myLoction.getAddr());
                //TODO　(2) 用户类
                table.setPub_person(username);
                table.setPub_loc(MyGson.toJson(loc));
                table.setCatagory(Container.OTHER);
                table.setPub_time(TimeUtil.getCurrentTime());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestService service = retrofit.create(RequestService.class);
                Call<MyResponse> call = service.addMainTable(table);
                //other_temp
                Log.d("PubPresenterImpl", "table:" + gson.toJson(table));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response<MyResponse> response = null;
                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        others_temp.setId(response.body().getValue());
                        others_temp.setContent(other_content.getText().toString());
                        others_temp.setPhone(phone_number.getText().toString());
                        Call<Void> call2 = service.addOthers(others_temp);
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
                                Snackbar.make(view.getWindow().getDecorView(), "发布成功", Snackbar.LENGTH_SHORT).show();
                                button.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
                                Toast.makeText(view, "连接不到服务器", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.finish();
            }
        });
    }
}