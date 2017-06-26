package com.upc.help_system.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.upc.help_system.R;
import com.upc.help_system.model.User;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;

import java.util.Set;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.upc.help_system.utils.network.MyRetrofit.requestService;

public class PhonePassword extends AppCompatActivity {

    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.identify_btn)
    Button identifyBtn;
    String phone;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    InputMethodManager imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_password);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhonePassword.this.finish();
            }
        });
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @OnClick(R.id.identify_btn)
    public void onClick() {
        imm.hideSoftInputFromWindow(PhonePassword.this.getWindow().getDecorView().getWindowToken(), 0);
        String password_string = password.getText().toString();
        if (password_string != null && !password_string.equals("")) {
            rigisterToCloud(phone, password_string, "未设置", phone);
        }
    }

    void rigisterToCloud(String username, String password, String sex, String phonenubmer) {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setPhonenumber(phonenubmer);
        user.setNickname(username);
        Call<String> call = requestService.register(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
                int a = Integer.parseInt(s);
                switch (a) {
                    case 1:
                        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("name", username);
                        editor.putString("password", password);
                        editor.putString("sex", sex);
                        editor.putString("phone", phonenubmer);
                        editor.putString("nickname", username);
                        editor.commit();
                        JPushInterface.setAlias(getApplicationContext(), username, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                            }
                        });

                        EMClient.getInstance().login(username, password, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                Snackbar.make(getWindow().getDecorView(), "注册成功", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(PhonePassword.this,MainActivity.class);
                                        startActivity(intent);
                                        PhonePassword.this.finish();
                                    }
                                }).show();

                            }

                            @Override
                            public void onError(int i, String s) {
                                Log.d("main", "登录聊天服务器失败！");
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                        break;
                    case 2:
                        Toast.makeText(PhonePassword.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(PhonePassword.this, "请检查网络", Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity", "t:" + t);
            }
        });
    }


}
