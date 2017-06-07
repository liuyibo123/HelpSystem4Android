package com.upc.help_system.presenter;

import android.content.Context;
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
import com.upc.help_system.model.Express;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.MyLoction;
import com.upc.help_system.utils.TimeUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.MyResponse;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.view.activity.PubActivity;

import java.io.IOException;

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
    String username;
    String phone;
    public PubPresenterImpl(PubActivity view) {
        this.view = view;
        SharedPreferences sharedPreferences = view.getSharedPreferences("user", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        phone = sharedPreferences.getString("phone", "");
        showExpress();
    }
    @Override
    public void OnTabClicked(String s) {
        switch (s){
            case "取快递":
                showExpress();
            case "带饭":
                showTakeFood();

        }
    }
    public void showTakeFood() {
        View v = view.getLayoutInflater().inflate(R.layout.fragment_food, null);
        view.framePub.removeAllViews();
        view.framePub.addView(v);
        EditText restaurant = (EditText) v.findViewById(R.id.restaurant);
        EditText food = (EditText) v.findViewById(R.id.food);
        EditText time = (EditText) v.findViewById(R.id.time);
        EditText acceptloc = (EditText) v.findViewById(R.id.acceptloc);
        EditText tip = (EditText) v.findViewById(R.id.tip);
        EditText phone_et = (EditText) v.findViewById(R.id.phone);
        Button makesure = (Button) v.findViewById(R.id.makesure);
        Button cancel = (Button) v.findViewById(R.id.cancel);
        phone_et.setText(phone);
    }

    public void showHomeWork() {
        //TODO 写作业帮的内容
    }

    public void showBuy() {
        //TODO 写帮购物的内容
    }

    public void showOther() {
        //TODO 写其他的东西
    }


    public void showExpress() {
        View v = view.getLayoutInflater().inflate(R.layout.fragment_express, null);
        view.framePub.removeAllViews();
        view.framePub.addView(v);
        Button button = (Button) view.findViewById(R.id.btn_makesure);
        AutoCompleteTextView express_company = (AutoCompleteTextView) view.findViewById(R.id.express_company);
        Resources resources = view.getResources();
        String express[] = resources.getStringArray(R.array.express_company);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view, android.R.layout.simple_dropdown_item_1line, express);
        express_company.setAdapter(adapter);
        EditText take_number = (EditText) view.findViewById(R.id.take_number);
        EditText name = (EditText) view.findViewById(R.id.name);
        name.setText(username);
        EditText phone_number = (EditText) view.findViewById(R.id.usr_name);
        phone_number.setText(phone);
        EditText destination_to = (EditText) view.findViewById(R.id.destination_to);
        EditText tip = (EditText) view.findViewById(R.id.tip);
        Spinner volume = (Spinner) view.findViewById(R.id.volume);
        Spinner weight = (Spinner) view.findViewById(R.id.weight);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                MainTable table = new MainTable();
                table.setContent("帮取快递:" + express_company.getText().toString() + "大小：" + Container.volume.values()[volume.getSelectedItemPosition()] +
                        "重量：" + Container.weight.values()[weight.getSelectedItemPosition()]);
                table.setTip(Float.parseFloat(tip.getText().toString()));
                table.setState(1);
                table.setCatagory(Container.EXPRESS);
                table.setHelp_loc(destination_to.getText().toString());
                //TODO （1）百度地图自动获取位置
                String loction = view.getSharedPreferences("location", Context.MODE_PRIVATE).getString("location", "获取位置失败");
                MyLoction myLoction = MyGson.fromJson(loction, MyLoction.class);
                table.setPub_loc(myLoction.getAddr());
                //TODO　(2) 用户类
                table.setPub_person(username);
                table.setPub_time(TimeUtil.getCurrentTime());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestService service = retrofit.create(RequestService.class);
                Call<MyResponse> call = service.addMainTable(table);
                Call<Void> call2 = service.addExpress(express_temp);
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
                        express_temp.setId(response.body().getValue());
                        express_temp.setCompany(express_company.getText().toString());
                        express_temp.setName(name.getText().toString());
                        express_temp.setPhone_number(phone_number.getText().toString());
                        express_temp.setTake_number(take_number.getText().toString());
                        express_temp.setVolume(volume.getCount());
                        express_temp.setWeight(weight.getCount());


                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
                                Snackbar.make(view.getWindow().getDecorView(), "发布成功", Snackbar.LENGTH_SHORT).show();
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
