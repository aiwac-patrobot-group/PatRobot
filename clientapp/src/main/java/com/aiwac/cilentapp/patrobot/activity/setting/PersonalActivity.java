package com.aiwac.cilentapp.patrobot.activity.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.robotapp.commonlibrary.common.Constant;

public class PersonalActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        initView();

    }

    private void initView(){
        SharedPreferences s = getSharedPreferences(Constant.DB_USER_TABLENAME,MODE_PRIVATE);
        String phoneNumber = s.getString(Constant.USER_REGISTER_NUMBER,"");
        String macaddress=s.getString(Constant.ROBOT_MAC_ADDRESS,"");
        //设置账户手机号和mac地址
        TextView tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvPhoneNumber.setText(phoneNumber);
        tvPhoneNumber.setEnabled(false);
        TextView tvMacAddress  = findViewById(R.id.tv_mac_address);
        tvMacAddress.setText(macaddress);
        tvMacAddress.setEnabled(false);


        //设置修改密码的跳转
        findViewById(R.id.btn_personal_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalActivity.this,ChangePasswordActivity.class));
            }
        });
    }
}
