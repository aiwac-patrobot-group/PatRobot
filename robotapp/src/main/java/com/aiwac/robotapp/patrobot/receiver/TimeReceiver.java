package com.aiwac.robotapp.patrobot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.utils.AiwacSportApi;

public class TimeReceiver extends BroadcastReceiver {
    private AiwacSportApi aiwacSportApi = AiwacSportApi.getInstance();
    private int sleepSeconds=1000;
    private static boolean navigateFlag=false;
    int upDis=0,downDis=0,leftDis=0,rightDis=0;
    private int direction=50;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("feedStart")){
            //处理开始投食
            LogUtil.d("开始投食");
            aiwacSportApi.aiwacFeedPetType(1);
        }else if(intent.getAction().equals("feedEnd")){
            //处理结束投食
            LogUtil.d("结束投食");
            aiwacSportApi.aiwacFeedPetType(0);
        }else if(intent.getAction().equals("navigateStart")) {
            //处理开始巡航
            LogUtil.d("开始巡航");

            setCallBackFromApi();
            navigateFlag=true;
            int duration = intent.getIntExtra("Duration",10);
            long t1 = System.currentTimeMillis();
            aiwacSportApi.aiwacUltrasoundDetectionType(1);

            try {
                Thread.sleep(sleepSeconds);
                while(navigateFlag){
                    long t2 = System.currentTimeMillis();
                    //设定循环执行时长
                    if(t2 - t1 > duration * 60 * 1000){
                        aiwacSportApi.aiwacUltrasoundDetectionType(0);
                        aiwacSportApi.aiwacSportType(0);
                        break;
                    }
                    //自动巡航
                    LogUtil.d("uP:"+upDis+"  down:"+downDis+"    leF:"+leftDis+"   right:"+rightDis);
                    if(upDis > direction){
                        //LogUtil.d("前");
                        aiwacSportApi.aiwacSportType(1);//前方无障碍朝前走
                    }else if(leftDis > direction){
                        aiwacSportApi.aiwacSportType(4);//前方有障碍，左转，朝前走
                        //LogUtil.d("左");

                        Thread.sleep(sleepSeconds);

                        aiwacSportApi.aiwacSportType(1);

                    }else if(rightDis > direction){
                        aiwacSportApi.aiwacSportType(8);//前方有障碍，左方有障碍，右转，朝前走

                        //LogUtil.d("右");

                        Thread.sleep(sleepSeconds);

                        aiwacSportApi.aiwacSportType(1);
                    }else if(downDis > direction ){
                        aiwacSportApi.aiwacSportType(2);//前方、左方、右方有障碍，后退，后右

                        //LogUtil.d("后");
                        Thread.sleep(sleepSeconds);
                        aiwacSportApi.aiwacSportType(4);

                        Thread.sleep(sleepSeconds);


                        /*aiwacSportApi.aiwacSportType(2);

                        Thread.sleep(sleepSeconds);
                        aiwacSportApi.aiwacSportType(10);*/
                    }else{//走不通，停止

                        //LogUtil.d("停止");
                        //aiwacSportApi.aiwacUltrasoundDetectionType(0);
                        aiwacSportApi.aiwacSportType(0);
                    }
                    Thread.sleep(sleepSeconds);
                }
                if(navigateFlag==false){
                    aiwacSportApi.aiwacUltrasoundDetectionType(0);
                    aiwacSportApi.aiwacSportType(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(intent.getAction().equals("navigateEnd")) {
            //处理结束巡航
            LogUtil.d("结束巡航");
            navigateFlag=false;
            aiwacSportApi.aiwacUltrasoundDetectionType(0);
            aiwacSportApi.aiwacSportType(0);
        }


    }

    //停止巡航
    public static void setNavigateFlag(boolean flag){
        LogUtil.d("结束巡航");
        navigateFlag=flag;
    }

}
