package com.upc.help_system.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.upc.help_system.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
                //TODO 书写注册的确定
                //TODO 确定之后跳转到主界面
                break;
        }
    }
}
