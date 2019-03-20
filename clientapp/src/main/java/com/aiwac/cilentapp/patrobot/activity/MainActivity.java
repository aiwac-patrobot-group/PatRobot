package com.aiwac.cilentapp.patrobot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.activity.loginandregister.LoginActivity;
import com.aiwac.cilentapp.patrobot.utils.ActivityUtil;

public class MainActivity extends AppCompatActivity {
    private Button btn_setting;
    private Button btn_register;
    private Button btn_video_chat;
    private Button btn_player;
    private Button btn_feed;
    private Button btn_cruise;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        btn_setting=findViewById(R.id.btn_setting);
        btn_register=findViewById(R.id.btn_register);
        btn_video_chat=findViewById(R.id.btn_video_chat);
        btn_player=findViewById(R.id.btn_player);
        btn_feed=findViewById(R.id.btn_feed);
        btn_cruise=findViewById(R.id.btn_cruise);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                //ActivityUtil.skipActivity(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
