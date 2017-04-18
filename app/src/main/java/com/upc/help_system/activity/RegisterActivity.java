package com.upc.help_system.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.upc.help_system.R;
import com.upc.help_system.model.HelpInfo;
import com.upc.help_system.utils.network.MyRetrofit;
import com.upc.help_system.utils.network.OnGetResponseListener;
import com.upc.help_system.utils.network.RequestServices;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/4/8.
 */

public class RegisterActivity extends Activity {
    @BindView(R.id.back_imgbtn)
    ImageButton backImgbtn;
    @BindView(R.id.phone_number)
    TextInputEditText phoneNumber;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.cofirm_password)
    TextInputEditText cofirmPassword;
    @BindView(R.id.check_number)
    TextInputEditText checkNumber;
    @BindView(R.id.get_check_number_btn)
    Button getCheckNumberBtn;
    @BindView(R.id.identify_btn)
    Button identifyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back_imgbtn, R.id.get_check_number_btn, R.id.identify_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_imgbtn:
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                break;
            case R.id.get_check_number_btn:
                identifyBtn.setEnabled(true);
                break;
            case R.id.identify_btn:
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://112.74.209.39:8080/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                RequestServices requestServices = retrofit.create(RequestServices.class);
                Call<String> call = requestServices.getStr();
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("tag",response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        Log.d("tag","error:"+throwable.getMessage());
                    }
                });

                Snackbar.make(getWindow().getDecorView(),"注册完成",Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}
