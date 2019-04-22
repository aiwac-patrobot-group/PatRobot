package com.aiwac.cilentapp.patrobot.sport;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aiwac.cilentapp.patrobot.server.WebSocketApplication;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

public class MoveControlService extends Service {
    private static MoveControlService minstance;
    public MoveControlService() {
        minstance=this;
    }

    public static MoveControlService getInstance(){
        return minstance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        LogUtil.d("控制机器人移动服务启动");
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private String command="";
    public void getMessage(String messageCode){
        LogUtil.d(messageCode);
        this.command=messageCode;
        sendMoveCommandToRobot();
    }
    private void sendMoveCommandToRobot(){
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    String jsonStr= JsonUtil.commendMoveDirection(command);
                    WebSocketApplication.getWebSocketApplication().getWebSocketHelper().send(jsonStr);
                    LogUtil.d("发送移动指令成功");
                } catch (Exception e){
                    e.printStackTrace();
                    LogUtil.d( "发送移动指令失败");
                }
            }
        });
    }

    //打开投食
    public void sendFeedStart(){
        getMessage(Constant.WEBSOCKET_COMMAND_FEED_START);
    }
    //关闭投食
    public void sendFeedClose(){
        getMessage(Constant.WEBSOCKET_COMMAND_FEED_STOP);
    }
    //开始巡航
    public void sendNavigateStart(){
        getMessage(Constant.WEBSOCKET_COMMAND_ULTRASOUND_NAVIGATE_START);
    }
    //结束巡航
    public void sendNavigateStop(){
        getMessage(Constant.WEBSOCKET_COMMAND_ULTRASOUND_NAVIGATE_STOP);
    }


/*    public void getMessage(String messageCode){
        Log.d(TAG, "getMessage: "+messageCode);
        if(messageCode.equals("UP")){
            Log.d(TAG, "上");
            up();
        }
        if(messageCode.equals("DOWN")){
            Log.d(TAG, "下");
            down();
        }
        if(messageCode.equals("LEFT")){
            Log.d(TAG, "左");
            left();
        }
        if(messageCode.equals("RIGHT")){
            Log.d(TAG, "右");
            right();
        }
        if(messageCode.equals("NONE")){
            Log.d(TAG, "停止");
            stop();
        }
        if(messageCode.equals("UP_AND_LEFT")){
            Log.d(TAG, "左上");
            upAndLeft();
        }
        if(messageCode.equals("UP_AND_RIGHT")){
            Log.d(TAG, "右上");
            upAndRight();
        }
        if(messageCode.equals("DOWN_AND_LEFT")){
            Log.d(TAG, "左下");
            downAndLeft();
        }
        if(messageCode.equals("DOWN_AND_RIGHT")){
            Log.d(TAG, "右下");
            downAndRight();
        }


        Log.d(TAG, "getMessage: type:"+type);
    }

    private void  up(){
        type=type|0b0000001;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  down(){
        type=type|0b0000010;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  left(){
        type=type|0b0000100;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  right(){
        type=type|0b0001000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void upAndLeft(){
        type=type|0b0000101;
        aiwacSportApi.aiwacSportType(type);
    }
    private void upAndRight(){
        type=type|0b0001001;
        aiwacSportApi.aiwacSportType(type);
    }
    private void downAndLeft(){
        type=type|0b0000110;
        aiwacSportApi.aiwacSportType(type);
    }
    private void downAndRight(){
        type=type|0b0001010;
        aiwacSportApi.aiwacSportType(type);
    }
    private void stop(){
        type=type&0b1110000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void openLightOne(){
        type=type|0b0010000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void openLightTwo(){
        type=type|0b0100000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void closeLightOne(){
        type=type&0b1101111;
        aiwacSportApi.aiwacSportType(type);
    }
    private void closeLightTwo(){
        type=type|0b1011111;
        aiwacSportApi.aiwacSportType(type);
    }
    */
}
