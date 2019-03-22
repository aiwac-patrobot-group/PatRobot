package com.aiwac.cilentapp.patrobot.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.activity.loginandregister.LoginByPasswordActivity;
import com.aiwac.cilentapp.patrobot.activity.setting.ScanCodeActivity;
import com.aiwac.cilentapp.patrobot.activity.setting.SettingActivity;
import com.aiwac.cilentapp.patrobot.database.UserData;
import com.aiwac.cilentapp.patrobot.service.WebSocketService;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btn_setting;
    private Button btn_login;
    private Button btn_video_chat;
    private Button btn_player;
    private Button btn_feed;
    private Button btn_cruise;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        hasLogged();
        hasMac();
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
            UserData userData = UserData.getUserData();
            userData.setClientID(s.getString(Constant.WEBSOCKET_MESSAGE_CLIENTID,""));
            userData.setPassword(s.getString(Constant.USER_DATA_FIELD_PASSWORD,""));
            userData.setNumber(phoneNumber);
            LogUtil.d("用户已经登录："+phoneNumber);
        }
    }
    //判断是否已经绑定了机器人，如果没有，则结束本activity，跳转到绑定页面
    private void hasMac(){
        //在这里不能关闭本activity，他需要websocket
        SharedPreferences s = getSharedPreferences(Constant.DB_USER_TABLENAME,MODE_PRIVATE);
        String macAddress = s.getString(Constant.ROBOT_MAC_ADDRESS,"");
        if(macAddress.equals("")){
            startActivity(new Intent(MainActivity.this, ScanCodeActivity.class));
        }
    }

    private void initView(){
        btn_setting=findViewById(R.id.btn_setting);
        btn_login =findViewById(R.id.btn_login);
        btn_video_chat=findViewById(R.id.btn_video_chat);
        btn_player=findViewById(R.id.btn_player);
        btn_feed=findViewById(R.id.btn_feed);
        btn_cruise=findViewById(R.id.btn_cruise);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginByPasswordActivity.class);
                //ActivityUtil.skipActivity(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
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

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.VIBRATE
                /*,
                Manifest.permission.READ_EXTERNAL_STORAGE*/
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。

    }
}
