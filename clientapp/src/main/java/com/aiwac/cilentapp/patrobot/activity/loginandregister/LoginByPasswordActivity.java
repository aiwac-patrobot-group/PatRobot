package com.aiwac.cilentapp.patrobot.activity.loginandregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aiwac.cilentapp.patrobot.R;

public class LoginByPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_password);

        TextView textToRegister = findViewById(R.id.btn_to_register);
        textToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegisterIntent=new Intent(LoginByPasswordActivity.this,RegisterCodeActivity.class);
                //ActivityUtil.skipActivity(LoginActivity.this,RegisterCodeActivity.class,true);
                startActivity(toRegisterIntent);
                finish();
            }
        });

        TextView tvToLoginByCode = findViewById(R.id.tv_code_login);
        tvToLoginByCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginByPasswordActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
}
