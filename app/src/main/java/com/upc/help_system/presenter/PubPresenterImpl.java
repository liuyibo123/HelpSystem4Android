package com.upc.help_system.presenter;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.upc.help_system.BuildConfig;
import com.upc.help_system.R;
import com.upc.help_system.events.FirstCallFinsh;
import com.upc.help_system.model.Express;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.utils.Container;
import com.upc.help_system.utils.TimeUtil;
import com.upc.help_system.utils.network.MyResponse;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.view.activity.MainActivity;
import com.upc.help_system.view.activity.PubActivity;

import org.greenrobot.eventbus.EventBus;

import java.sql.Timestamp;

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
    public PubPresenterImpl(PubActivity view) {
        this.view = view;
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
        //TODO 写填写带饭的内容
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
    @Override
    public void onExpressFirstFinsh() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://180.201.158.155:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestService service = retrofit.create(RequestService.class);


        Call<Void> call2 = service.addExpress(express_temp);

        call2.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "success");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (BuildConfig.DEBUG) Log.d("PubPresenterImpl", "t:" + t);
                Toast.makeText(view, "连接不到服务器", Toast.LENGTH_SHORT).show();
            }
        });
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
        EditText phone_number = (EditText) view.findViewById(R.id.phone_number);
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
                table.setPub_loc("暂无法自动获取");
                //TODO　(2) 用户类
                table.setPub_person(0);
                table.setPub_time(TimeUtil.getCurrentTime());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://180.201.158.155:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RequestService service = retrofit.create(RequestService.class);
                Call<MyResponse> call = service.addMainTable(table);
                Log.d("PubPresenterImpl", "table:" + gson.toJson(table));
                call.enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        express_temp.setId(response.body().getValue());
                        express_temp.setCompany(express_company.getText().toString());
                        express_temp.setName(name.getText().toString());
                        express_temp.setPhone_number(phone_number.getText().toString());
                        express_temp.setTake_number(take_number.getText().toString());
                        express_temp.setVolume(volume.getCount());
                        express_temp.setWeight(weight.getCount());
                        EventBus.getDefault().post(new FirstCallFinsh());
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.d("PubPresenterImpl", "t:" + t);
                        Toast.makeText(view, "连接不到服务器", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO  销毁视图
            }
        });
    }

}
