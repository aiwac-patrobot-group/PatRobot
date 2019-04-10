package com.aiwac.robotapp.patrobot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

public class feedStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("feedStart")){
            //处理开始投食
            LogUtil.d("开始投食");
        }else if(intent.getAction().equals("feedEnd")){
            //处理结束投食
            LogUtil.d("结束投食");
        }else if(intent.getAction().equals("navigateStart")) {
            //处理开始巡航
            LogUtil.d("开始巡航");
        }else if(intent.getAction().equals("navigateEnd")) {
            //处理结束巡航
            LogUtil.d("结束巡航");
        }


    }
}
