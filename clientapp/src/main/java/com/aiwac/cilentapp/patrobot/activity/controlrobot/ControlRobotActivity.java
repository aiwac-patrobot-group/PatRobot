package com.aiwac.cilentapp.patrobot.activity.controlrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aiwac.cilentapp.patrobot.R;
import com.aiwac.cilentapp.patrobot.activity.videochat.VideoChatViewActivity;
import com.aiwac.cilentapp.patrobot.sport.MoveControlService;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;

import me.caibou.rockerview.DirectionView;

public class ControlRobotActivity extends AppCompatActivity {
    private Button btn_feed;
    private Button btn_navigate;
    private boolean feedIsOpen=false;
    private boolean navigateIsOpen= false;
    private boolean isLongClick=false;
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

        //设置运动监听
        setControlDirection();

        //设置投食
        btn_feed=findViewById(R.id.btn_feed);
        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(feedIsOpen==false){
                    MoveControlService.getInstance().sendFeedStart();
                    btn_feed.setText("停止投食");
                    feedIsOpen=true;
                }else{
                    MoveControlService.getInstance().sendFeedClose();
                    btn_feed.setText("开启投食");
                    feedIsOpen=false;
                }
            }
        });

        //设置巡航
        btn_navigate=findViewById(R.id.btn_navigate);
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
                //MoveControlService.getInstance().getMessage(direction.toString());
                sendMessageToRobot(direction.toString());
            }
        });
    }
    private void sendMessageToRobot(final String messageCode){
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                if(messageCode.equals("NONE")){
                    isLongClick=false;
                    try{
                        MoveControlService.getInstance().getMessage(messageCode);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    isLongClick=true;
                }
                while (isLongClick){
                    try{
                        MoveControlService.getInstance().getMessage(messageCode);
                        //间隔0.8
                        Thread.sleep(800);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }




}
