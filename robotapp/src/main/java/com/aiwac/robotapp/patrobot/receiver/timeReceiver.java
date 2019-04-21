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
            aiwacSportApi.aiwacUltrasoundDetectionType(1);
            while(true){
                //自动巡航
                if(upDis > 50){
                    aiwacSportApi.aiwacSportType(1);
                }else if(leftDis > 50){
                    aiwacSportApi.aiwacSportType(4);
                    aiwacSportApi.aiwacSportType(9);
                }else if(rightDis > 50){
                    aiwacSportApi.aiwacSportType(8);
                    aiwacSportApi.aiwacSportType(9);
                }else if(downDis > 50 ){
                    aiwacSportApi.aiwacSportType(2);
                    aiwacSportApi.aiwacSportType(10);
                }
                if(intent.getAction().equals("navigateEnd")){//收到停止的广播结束循环
                    break;
                }
            }
        }else if(intent.getAction().equals("navigateEnd")) {
            //处理结束巡航
            LogUtil.d("结束巡航");
            aiwacSportApi.aiwacUltrasoundDetectionType(0);
        }


    }
}
