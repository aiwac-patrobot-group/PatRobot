package com.aiwac.robotapp.patrobot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.utils.AiwacSportApi;

public class timeReceiver extends BroadcastReceiver {
    private static final AiwacSportApi aiwacSportApi =  new AiwacSportApi();
    int upDis=0,downDis=0,leftDis=0,rightDis=0;
    private Handler aiwacAndroidHandler  = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // TODO Auto-generated method stub
            if(msg.what == 100)
            {
                String avoidanceContent = msg.obj.toString();
                Log.i("A33Socket", "analysisContent:"+avoidanceContent+"++");
                if(avoidanceContent.substring(0,1).equals("1") == true){
                    upDis = Integer.valueOf(avoidanceContent.substring(2,5));
                }else if(avoidanceContent.substring(0,1).equals("2") == true){
                    downDis = Integer.valueOf(avoidanceContent.substring(2,5));
                }else if(avoidanceContent.substring(0,1).equals("3") == true){
                    leftDis = Integer.valueOf(avoidanceContent.substring(2,5));
                }else if(avoidanceContent.substring(0,1).equals("4") == true){
                    rightDis = Integer.valueOf(avoidanceContent.substring(2,5));
                }
            }
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        aiwacSportApi.setAiwacHaLMsgHandler(aiwacAndroidHandler);
        if(intent.getAction().equals("feedStart")){
            //处理开始投食
            LogUtil.d("开始投食");
            aiwacSportApi.aiwacFeedPetType(0);
        }else if(intent.getAction().equals("feedEnd")){
            //处理结束投食
            LogUtil.d("结束投食");
            aiwacSportApi.aiwacFeedPetType(1);
        }else if(intent.getAction().equals("navigateStart")) {
            //处理开始巡航
            LogUtil.d("开始巡航");
            int duration = intent.getIntExtra("Duration",10);
            long t1 = System.currentTimeMillis();
            aiwacSportApi.aiwacUltrasoundDetectionType(1);
            while(true){
                long t2 = System.currentTimeMillis();
                //设定循环执行时长
                if(t2 - t1 > duration * 60 * 1000){
                    aiwacSportApi.aiwacUltrasoundDetectionType(0);
                    aiwacSportApi.aiwacSportType(0);
                    break;
                }
                //自动巡航
                if(upDis > 50){
                    aiwacSportApi.aiwacSportType(1);//前方无障碍朝前走
                }else if(leftDis > 50){
                    aiwacSportApi.aiwacSportType(4);//前方有障碍，左转，朝前走
                    aiwacSportApi.aiwacSportType(1);
                }else if(rightDis > 50){
                    aiwacSportApi.aiwacSportType(8);//前方有障碍，左方有障碍，右转，朝前走
                    aiwacSportApi.aiwacSportType(1);
                }else if(downDis > 50 ){
                    aiwacSportApi.aiwacSportType(2);//前方、左方、右方有障碍，后退，后右
                    aiwacSportApi.aiwacSportType(2);
                    aiwacSportApi.aiwacSportType(2);
                    aiwacSportApi.aiwacSportType(10);
                }else{//走不通，停止
                    aiwacSportApi.aiwacUltrasoundDetectionType(0);
                    aiwacSportApi.aiwacSportType(0);
                }

            }
        }else if(intent.getAction().equals("navigateEnd")) {
            //处理结束巡航
            LogUtil.d("结束巡航");
            aiwacSportApi.aiwacUltrasoundDetectionType(0);
            aiwacSportApi.aiwacSportType(0);
        }


    }
}
