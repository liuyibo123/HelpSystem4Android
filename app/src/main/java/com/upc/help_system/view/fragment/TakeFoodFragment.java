package com.upc.help_system.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.upc.help_system.R;
import com.upc.help_system.model.Food;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.MyLoction;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.TimeUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.MyResponse;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TakeFoodFragment extends Fragment {

    View v;
    TakeFoodViewHolder takeFoodViewHolder;
    private String phone;
    private String username;
    private MyLoction loc;

    public TakeFoodFragment() {
        phone = SharedPreferenceUtil.getString("user","phone");
        username = SharedPreferenceUtil.getString("user","name");
        String loc_string = SharedPreferenceUtil.getString("location","location");
        loc = MyGson.fromJson(loc_string,MyLoction.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_food, null);
        if(takeFoodViewHolder==null){
            takeFoodViewHolder = new TakeFoodViewHolder();
        }
        init();
        return v;
    }

    private void init() {
        takeFoodViewHolder.phone_number.setText(phone);
        Food food_temp = new Food();
        takeFoodViewHolder.makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                MainTable table = new MainTable();
                table.setContent("帮带饭，餐厅:" + takeFoodViewHolder.canteen.getText().toString());
                table.setTip(Float.parseFloat(takeFoodViewHolder.charge.getText().toString()));
                table.setState(1);
                table.setCatagory(Container.TAKEFOOD);
                table.setHelp_loc(takeFoodViewHolder.deatination.getText().toString());
                table.setPub_person(username);
                table.setPub_time(TimeUtil.getCurrentTime());
                table.setPub_loc(MyGson.toJson(loc));
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestService service = retrofit.create(RequestService.class);
                Call<MyResponse> call = service.addMainTable(table);
                Call<Void> call2 = service.addFood(food_temp);
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
                        food_temp.setId(response.body().getValue());
                        food_temp.setCanteen(takeFoodViewHolder.canteen.getText().toString());
                        food_temp.setContent(takeFoodViewHolder.food.getText().toString());
                        food_temp.setTime(takeFoodViewHolder.dealine.getText().toString());
                        food_temp.setPhone(takeFoodViewHolder.phone_number.getText().toString());
                        Log.d("PubPresenterImpl", MyGson.toJson(food_temp));
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                SnackbarUtil.LongSnackbar(getActivity().getWindow().getDecorView(), "发布成功", SnackbarUtil.Confirm).setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getActivity().finish();
                                    }
                                }).show();
                                takeFoodViewHolder.makesure.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                SnackbarUtil.LongSnackbar(getActivity().getWindow().getDecorView(), "网络原因发布失败", SnackbarUtil.Alert).setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getActivity().finish();
                                    }
                                }).show();
                            }
                        });
                    }
                }).start();
            }
        });
        takeFoodViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    class TakeFoodViewHolder{
        EditText canteen;
        EditText food;
        EditText dealine;
        EditText deatination;
        EditText charge;
        EditText phone_number;
        Button makesure;
        Button cancel;
        public TakeFoodViewHolder(){

            canteen = (EditText) v.findViewById(R.id.canteen_content);
            food = (EditText) v.findViewById(R.id.food_content);
            dealine = (EditText) v.findViewById(R.id.deadline_content);
            deatination = (EditText) v.findViewById(R.id.food_destination_content);
            charge = (EditText) v.findViewById(R.id.food_charge_content);
            phone_number = (EditText) v.findViewById(R.id.phone_number_content);
            makesure = (Button) v.findViewById(R.id.btn_makesure);
            cancel = (Button) v.findViewById(R.id.btn_cancel);
        }

    }

}
