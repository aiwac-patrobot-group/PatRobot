package com.aiwac.robotapp.patrobot.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;

import com.aiwac.robotapp.commonlibrary.common.Constant;
import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.utils.AiwacSportApi;

import java.util.Random;

import io.vov.vitamio.utils.Log;

public class SportService extends Service {

    private static SportService myInstance;
    private AiwacSportApi aiwacSportApi;

    //发送给api的指令
    private int type=0b0000;

    //保存从api获取的前后左右的超声波距离信息
    int upDis=0,downDis=0,leftDis=0,rightDis=0;

    private int sleepSeconds=1000;
    private static boolean navigateFlag=false;
    private int direction=100;

    private HandlerThread navigateHandlerThread;

    private Thread runNavigateThread;
    private Handler workHandler;
    public SportService() {
        myInstance =this;
        aiwacSportApi=AiwacSportApi.getInstance();


        navigateHandlerThread=new HandlerThread("navigate");
        navigateHandlerThread.start();

        workHandler=new Handler(navigateHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                LogUtil.d("stop in handle1111");
                super.handleMessage(msg);
                if(msg.what==1){
                    final int d= (int) msg.obj;
                    LogUtil.d("what 1");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            navigateStartHandle(d);
                        }
                    }).start();

                }else if(msg.what==0){
                    LogUtil.d("what 0");
                    navigateStopHandle();
                }
            }

            /**
             * 巡航开关
             */
            public void navigateStartHandle(final int duration){
                setCallBackFromApi();
                navigateFlag=true;
                ultrasoundOpen();
                try {
                    Thread.sleep(sleepSeconds);
                    while (navigateFlag) {
                        long t2 = System.currentTimeMillis();
                        //设定循环执行时长
                        long t1 = System.currentTimeMillis();
                        if (t2 - t1 > duration * 60 * 1000) {
                            aiwacSportApi.aiwacUltrasoundDetectionType(0);
                            aiwacSportApi.aiwacSportType(0);
                            break;
                        }
                        //自动巡航
                        LogUtil.d("uP:" + upDis + "  down:" + downDis + "    leF:" + leftDis + "   right:" + rightDis);
                        if (upDis > direction) {//前方无障碍朝前走
                            //up();

                            if (new Random().nextInt(50) > 5) {//前方无障碍朝前走
                                up();
                                LogUtil.d("qian");
                            } else {//前方无障碍朝前走,随机变化方向
                                LogUtil.d("suiji");
                                turnRandom();
                            }
                        } else if (leftDis > direction) { //前方有障碍，左或者右转，朝前走
                            turnRandom();
                            LogUtil.d("zhuan");
                        } else if (rightDis > direction) {//前方有障碍，左方有障碍，右转，朝前走
                            turnRight();
                            LogUtil.d("youzhuan");
                        } else if (downDis > direction) {
                            LogUtil.d("houtui");
                            down();//前方、左方、右方有障碍，后退，后右
                            Thread.sleep(sleepSeconds);
                            down();
                            Thread.sleep(sleepSeconds);
                            turnRandom();
                        } else {//走不通，停止
                            LogUtil.d("停止");
                            stop();
                            navigateFlag = false;
                        }
                        Thread.sleep(sleepSeconds);
                    }
                    if (navigateFlag == false) {
                        ultrasoundClose();
                        stop();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
            public void navigateStopHandle(){
                //LogUtil.d("stop");
                navigateFlag=false;
                ultrasoundClose();
                stop();
            }
        };
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
            navigateStart(10);//此处只有手机端控制机器人运动的界面的按钮会触发
        }else if(messageCode.equals(Constant.WEBSOCKET_COMMAND_ULTRASOUND_NAVIGATE_STOP)){
            navigateStop();//此处只有手机端控制机器人运动的界面的按钮会触发
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


    //当多次startService去启动Service，若Service对象存在，就只调用onStartCommand，若Service对象不存在，创建Service对象。
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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





    public void navigateStart(final int duration){
        Message msg=Message.obtain();
        msg.what=1;
        msg.obj=duration;
        workHandler.sendMessage(msg);
    }
    public void navigateStop(){
        //LogUtil.d("stopin waimian");
        Message msg=Message.obtain();
        msg.what=0;
        workHandler.sendMessage(msg);
    }




    //设置回调函数
    private void setCallBackFromApi(){
        aiwacSportApi.setCallback(new AiwacSportApi.Callback() {
            @Override
            public void newMsgFromA33(int Type, String data) {
                if(Type==100){
                    LogUtil.d("getmessaage: "+Type+"   "+data);
                    String avoidanceContent=data.substring(0,1);
                    if(avoidanceContent.equals("1") == true){
                        upDis = Integer.valueOf(data.substring(2,5));
                    }else if(avoidanceContent.equals("2") == true){
                        downDis = Integer.valueOf(data.substring(2,5));
                    }else if(avoidanceContent.equals("3") == true){
                        leftDis = Integer.valueOf(data.substring(2,5));
                    }else if(avoidanceContent.equals("4") == true){
                        rightDis = Integer.valueOf(data.substring(2,5));
                    }
                }
            }
        });
    }



    /**
     * 运动控制
     */
    private void  up(){
        LogUtil.d("Up");
        //type=type|0b0001;
        type=0b0001;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  down(){
        LogUtil.d("down");
        type=0b0010;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  left(){
        LogUtil.d("lef");
        type=0b0100;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  right(){
        LogUtil.d("right");
        type=0b1000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void upAndLeft(){
        type=0b0101;
        aiwacSportApi.aiwacSportType(type);
    }
    private void upAndRight(){
        type=0b1001;
        aiwacSportApi.aiwacSportType(type);
    }
    private void downAndLeft(){
        type=0b0110;
        aiwacSportApi.aiwacSportType(type);
    }
    private void downAndRight(){
        type=0b1010;
        aiwacSportApi.aiwacSportType(type);
    }
    private void stop(){
        LogUtil.d("stop");
        type=type&0b0000;
        aiwacSportApi.aiwacSportType(type);
    }
    //转向
    private void turnRight() throws InterruptedException {
        LogUtil.d("turnright");
        right();
        Thread.sleep(sleepSeconds);
        right();
    }
    private void turnLeft() throws InterruptedException {
        LogUtil.d("turnleft");
        left();
        Thread.sleep(sleepSeconds);
        left();
    }
    //随机转向，概率一致
    private void turnRandom() throws InterruptedException {
        if (new Random().nextInt(2) == 0) {
            turnLeft();
        } else {
            turnRight();
        }
    }
}
