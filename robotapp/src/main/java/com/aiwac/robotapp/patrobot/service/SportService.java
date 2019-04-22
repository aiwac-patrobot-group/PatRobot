package com.aiwac.robotapp.patrobot.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.receiver.timeReceiver;
import com.aiwac.robotapp.patrobot.utils.AiwacSportApi;

public class SportService extends Service {

    private static SportService myInstance;
    private AiwacSportApi aiwacSportApi;
    private int type=0b0000;
    public SportService() {
        myInstance =this;
        aiwacSportApi=new AiwacSportApi();
    }

    public static SportService getInstance(){
        return myInstance;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        LogUtil.d("控制机器人移动服务启动");
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void getMessage(String messageCode){
        LogUtil.d("getMessage: "+messageCode);
        if(messageCode.equals("UP")){
            //LogUtil.d( "上");
            up();
        }else if(messageCode.equals("DOWN")){
            //LogUtil.d( "下");
            down();
        }else if(messageCode.equals("LEFT")){
            //LogUtil.d( "左");
            left();
        }else if(messageCode.equals("RIGHT")){
            //LogUtil.d( "右");
            right();
        }else if(messageCode.equals("NONE")){
            //LogUtil.d( "停止");
            stop();
        }else if(messageCode.equals(Constant.WEBSOCKET_COMMAND_FEED_START)) {
            feedOpen();
        }else if(messageCode.equals(Constant.WEBSOCKET_COMMAND_FEED_STOP)) {
            feedClose();
        }else if(messageCode.equals(Constant.WEBSOCKET_COMMAND_ULTRASOUND_NAVIGATE_START)){
            navigateStart();
        }else if(messageCode.equals(Constant.WEBSOCKET_COMMAND_ULTRASOUND_NAVIGATE_STOP)){
            navigateStop();
        }else if(messageCode.equals("UP_AND_LEFT")){
            //LogUtil.d( "左上");
            upAndLeft();
        }else if(messageCode.equals("UP_AND_RIGHT")){
            //LogUtil.d( "右上");
            upAndRight();
        }else if(messageCode.equals("DOWN_AND_LEFT")){
            //LogUtil.d( "左下");
            downAndLeft();
        }else if(messageCode.equals("DOWN_AND_RIGHT")){
            //LogUtil.d( "右下");
            downAndRight();
        }
        //LogUtil.d( "getMessage: type:"+type);
    }

    /**
     * 投食开关
     */
    public void feedOpen(){
        aiwacSportApi.aiwacFeedPetType(0b1);
    }
    public void feedClose(){
        aiwacSportApi.aiwacFeedPetType(0b0);
    }

    /**
     * 超声波开关
     */
    public void ultrasoundOpen(){
        aiwacSportApi.aiwacUltrasoundDetectionType(0b1);
    }
    public void ultrasoundClose(){
        aiwacSportApi.aiwacUltrasoundDetectionType(0b0);
    }

    /**
     * 巡航开关
     */
    public void navigateStart(){
        Intent intent = new Intent(getApplicationContext(), timeReceiver.class);
        intent.putExtra("Duration",30);
        intent.setAction("navigateStart");

        sendBroadcast(intent);
    }
    public void navigateStop(){
        Intent intent = new Intent(getApplicationContext(), timeReceiver.class);
        intent.setAction("navigateEnd");
        sendBroadcast(intent);
    }


    /**
     * 运动控制
     */
    private void  up(){
        type=type|0b0001;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  down(){
        type=type|0b0010;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  left(){
        type=type|0b0100;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  right(){
        type=type|0b1000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void upAndLeft(){
        type=type|0b0101;
        aiwacSportApi.aiwacSportType(type);
    }
    private void upAndRight(){
        type=type|0b1001;
        aiwacSportApi.aiwacSportType(type);
    }
    private void downAndLeft(){
        type=type|0b0110;
        aiwacSportApi.aiwacSportType(type);
    }
    private void downAndRight(){
        type=type|0b1010;
        aiwacSportApi.aiwacSportType(type);
    }
    private void stop(){
        type=type&0b0000;
        aiwacSportApi.aiwacSportType(type);
    }
}
