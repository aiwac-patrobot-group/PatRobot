package com.aiwac.robotapp.patrobot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aiwac.robotapp.patrobot.R;
import com.aiwac.robotapp.patrobot.activity.update.RobotUpdateActivity;

import zuo.biao.library.base.BaseActivity;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.btn_wifi_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wifiIntent = new Intent(SettingActivity.this, ConnectWifiActivity.class);
                wifiIntent.putExtra("from", "setting");
                startActivity(wifiIntent);
            }
        });

        findViewById(R.id.btn_robot_app_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this, RobotUpdateActivity.class));
            }
        });

        /*//返回键
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
