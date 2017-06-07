package com.upc.help_system.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.upc.help_system.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/8.
 */

public class RegisterActivity extends Activity {
    @BindView(R.id.usr_name)
    TextInputEditText username;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.cofirm_password)
    TextInputEditText cofirmPassword;
    @BindView(R.id.identify_btn)
    Button identifyBtn;
    @BindView(R.id.qq_reg)
    ImageButton qqReg;
    @BindView(R.id.phone_reg)
    ImageButton phoneReg;
    InputMethodManager imm;
    String username_string;
    String password_string;
    String cofirm_password_string;
    @BindView(R.id.back_btn)
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @OnClick({R.id.identify_btn, R.id.qq_reg, R.id.phone_reg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identify_btn:
                imm.hideSoftInputFromWindow(RegisterActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                register();
                break;
            case R.id.qq_reg:
                break;
            case R.id.phone_reg:
                break;
        }
    }

    private void register() {
        username_string = username.getText().toString();
        password_string = password.getText().toString();
        cofirm_password_string = cofirmPassword.getText().toString();
        switch (validate(username_string, password_string, cofirm_password_string)) {
            case 1:
                Intent intent = new Intent(RegisterActivity.this, PerfectInformation.class);
                intent.putExtra("name", username_string);
                intent.putExtra("password", password_string);
                startActivity(intent);
                this.finish();
                break;
            case 2:
                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "用户名只能由字母,数字和_组成", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 1 成功
     * 2 密码输入不一致
     * 3.用户名不符合规范
     */
    private int validate(String u, String p, String c) {
        int len1 = u.length();
        int len2 = p.length();
        if (u.equals(p)) {
            System.out.println("用户名和密码重复");
            return 3;
        }
        if (!p.equals(c)) {
            System.out.println("两次密码不一致");
            return 2;
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


    @OnClick(R.id.back_btn)
    public void onClick() {
        this.finish();
    }
}
