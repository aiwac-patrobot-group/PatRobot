package com.aiwac.cilentapp.patrobot.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.activity.loginandregister.LoginByPasswordActivity;
import com.aiwac.cilentapp.patrobot.activity.loginandregister.RegisterCodeActivity;
import com.aiwac.cilentapp.patrobot.database.UserData;
import com.aiwac.cilentapp.patrobot.service.WebSocketService;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

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
        hasLogged();

        initView();
        initEven();
    }
    //判断 是否已经登录，如果没有登录，结束本activity，跳转到登录界面
    private void hasLogged(){
        SharedPreferences s = getSharedPreferences(Constant.DB_USER_TABLENAME,MODE_PRIVATE);
        String phoneNumber = s.getString(Constant.USER_REGISTER_NUMBER,"");
        if(phoneNumber.equals("")){
            startActivity(new Intent(MainActivity.this, LoginByPasswordActivity.class));
            finish();
        }else{
            UserData.getUserData().setNumber(phoneNumber);
            LogUtil.d("用户已经登录："+phoneNumber);
        }
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
                Intent intent = new Intent(MainActivity.this, RegisterCodeActivity.class);
                //ActivityUtil.skipActivity(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    private Intent intentWebsocketService;
    private void initEven(){
        intentWebsocketService = new Intent(MainActivity.this, WebSocketService.class);
        intentWebsocketService.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
        startService(intentWebsocketService);
    }

    @Override
    public void finish() {
        super.finish();
        stopService(intentWebsocketService);
    }
}
