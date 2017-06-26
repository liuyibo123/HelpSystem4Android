package com.upc.help_system.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.upc.help_system.R;
import com.upc.help_system.model.User;
import com.upc.help_system.utils.Util;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.usr_name)
    TextInputEditText usrName;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.identify_btn)
    Button identifyBtn;
    @BindView(R.id.qq_reg)
    ImageButton qqlogin;
    @BindView(R.id.register)
    TextView reg;
    InputMethodManager imm;
    String username_string;
    String password_string;
    String nickname_string;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    private Tencent mTencent;
    public static QQAuth mQQAuth;
    public static String mAppid;
    public static String openidString;
    public static String nicknameString;
    public static String TAG="MainActivity";
   private static String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        identifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_string = usrName.getText().toString();
                password_string = password.getText().toString();
                imm.hideSoftInputFromWindow(LoginActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                switch (validate(username_string, password_string)) {
                    case 1:
                        Log.d("LoginActivity", username_string + password_string);
                        loginToCloud(username_string, password_string);
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(LoginActivity.this, "用户名只能由字母,数字和_组成", Toast.LENGTH_SHORT).show();

                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

    }

    private void loginToCloud(String name, String password_string) {
        User user = new User();
        user.setName(name);
        user.setPassword(password_string);
        Call<String> call = requestService.login(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
                int a = Integer.parseInt(s);
                switch (a) {
                    case 1:
                        Call<User> userCall = requestService.getUser(username_string);
                        userCall.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                User user1 = response.body();
                                SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                String nickname = user1.getNickname();
                                nickname_string = nickname;
                                editor.putString("name", user1.getName());
                                editor.putString("nickname", user1.getNickname());
                                editor.putString("password", user1.getPassword());
                                editor.putString("sex", user1.getSex());
                                editor.putString("phone", user1.getPhonenumber());
                                editor.commit();
                                JPushInterface.setAlias(getApplicationContext(), username_string, new TagAliasCallback() {
                                    @Override
                                    public void gotResult(int i, String s, Set<String> set) {

                                    }
                                });
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                        EMClient.getInstance().login(username_string, password_string, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                Snackbar.make(getWindow().getDecorView(), "登录成功", Snackbar.LENGTH_SHORT).setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LoginActivity.this.finish();
                                    }
                                }).show();
                            }

                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                        break;
                    case 2:
                        Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(LoginActivity.this, "用户名和密码不匹配", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity", "t:" + t);
            }
        });
    }

    private int validate(String u, String p) {
        int len1 = u.length();
        int len2 = p.length();
        if (u.equals(p)) {
            System.out.println("用户名和密码重复");
            return 3;
        }

        if (len1 < 6 || len1 > 16) {
            System.out.println("用户名限制在6-16");
            return 3;
        } else {
            boolean flag = false;
            Pattern pattern = Pattern.compile("[a-zA-Z0-9_.-s]*");
            if (!u.equals(p)) {
                if (u != null) {
                    Matcher matcher = pattern.matcher(u);
                    flag = matcher.matches();
                    if (flag == false)
                        return 3;
                }
            }
            return 1;
        }
    }

    @OnClick({R.id.identify_btn, R.id.qq_reg, R.id.phone_reg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identify_btn:
                break;
            case R.id.qq_reg:
                Toast.makeText(this, "测试中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.phone_reg:
                break;
        }
    }


    @OnClick(R.id.back_btn)
    public void onClick() {
        this.finish();
    }

}

