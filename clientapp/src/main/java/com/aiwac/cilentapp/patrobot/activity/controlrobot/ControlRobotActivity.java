package com.aiwac.cilentapp.patrobot.activity.controlrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.activity.videochat.VideoChatViewActivity;
import com.aiwac.cilentapp.patrobot.sport.MoveControlService;

import me.caibou.rockerview.DirectionView;

public class ControlRobotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_robot);
        initView();
        initEvent();
    }

    public void initView(){

    }
    public void initEvent(){
        //返回键
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setControlDirection();

    }

    //设置运动监听
    private void setControlDirection(){
        //启动服务
        startService(new Intent(ControlRobotActivity.this, MoveControlService.class));

        //设置监听
        DirectionView dv = findViewById(R.id.direct_control);
        dv.setDirectionChangeListener(new DirectionView.DirectionChangeListener() {
            @Override
            public void onDirectChange(DirectionView.Direction direction) {
                MoveControlService.getInstance().getMessage(direction.toString());
            }
        });
    }
}
