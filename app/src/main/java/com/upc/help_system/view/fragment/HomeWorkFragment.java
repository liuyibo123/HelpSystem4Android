package com.upc.help_system.view.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.upc.help_system.BuildConfig;
import com.upc.help_system.R;
import com.upc.help_system.model.Homework;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.MyGson;
import com.upc.help_system.utils.MyLoction;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.TimeUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.MyResponse;
import com.upc.help_system.utils.network.RequestService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeWorkFragment extends Fragment {

    View v;
    HomeWorkViewHolder homeWorkViewHolder;
    private String phone;
    private String username;
    private MyLoction loc;
    public HomeWorkFragment() {
        phone = SharedPreferenceUtil.getString("user","phone");
        username = SharedPreferenceUtil.getString("user","name");
        String loc_string = SharedPreferenceUtil.getString("location","location");
        loc = MyGson.fromJson(loc_string,MyLoction.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =  getLayoutInflater(savedInstanceState).inflate(R.layout.fragment_work, null);
        if(homeWorkViewHolder==null){
            homeWorkViewHolder = new HomeWorkViewHolder();
        }
        init();
        return v;
    }

    private void init() {
        homeWorkViewHolder.phone_number.setText(phone);
        homeWorkViewHolder.makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                MainTable mainTable = new MainTable();
                mainTable.setContent("作业求助：" + homeWorkViewHolder.subject.getText().toString() + "\n" + homeWorkViewHolder.question.getText().toString());
                mainTable.setTip(Float.parseFloat(homeWorkViewHolder.charge.getText().toString()));
                mainTable.setState(1);
                mainTable.setCatagory(Container.HOMEWORK);
                mainTable.setPub_person(username);
                mainTable.setPub_time(TimeUtil.getCurrentTime());
                mainTable.setPub_loc(MyGson.toJson(loc));
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestService requestService = retrofit.create(RequestService.class);
                Call<MyResponse> call = requestService.addMainTable(mainTable);

                Log.d("PubPresenterImpl", "table:" + gson.toJson(mainTable));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response<MyResponse> response = null;
                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Homework homework_temp = new Homework();
                        homework_temp.setId(response.body().getValue());
                        homework_temp.setSubject(homeWorkViewHolder.subject.getText().toString());
                        homework_temp.setTip(Float.parseFloat(homeWorkViewHolder.charge.getText().toString()));
                        homework_temp.setQuestion(homeWorkViewHolder.question.getText().toString());
                        homework_temp.setPhone(homeWorkViewHolder.phone_number.getText().toString());
                        Call<Void> call2 = requestService.addHomework(homework_temp);
                        Log.d("PubPresenterImpl", MyGson.toJson(homework_temp));
                        call2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
                                Snackbar.make(getActivity().getWindow().getDecorView(), "发布成功", Snackbar.LENGTH_SHORT).show();
                                homeWorkViewHolder.makesure.setEnabled(false);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
                                Snackbar.make(getActivity().getWindow().getDecorView(), "连接不上服务器", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });
        homeWorkViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    class HomeWorkViewHolder{
        EditText subject;
        EditText question;
        EditText charge;
        EditText phone_number;
        Button makesure;
        Button cancel;
        public HomeWorkViewHolder(){
            subject = (EditText) v.findViewById(R.id.subject_content);
            question = (EditText) v.findViewById(R.id.question_content);
            charge = (EditText) v.findViewById(R.id.work_charge_content);
            phone_number = (EditText) v.findViewById(R.id.phone_number_content);
            makesure = (Button) v.findViewById(R.id.btn_makesure);
            cancel = (Button) v.findViewById(R.id.btn_cancel);
        }
    }
}
