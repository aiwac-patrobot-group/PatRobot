package com.aiwac.robotapp.patrobot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.service.SportService;

public class TimeReceiver extends BroadcastReceiver {
    private SportService sportService=SportService.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("feedStart")){
            //处理开始投食
            LogUtil.d("开始投食");
            sportService.feedOpen();
        }else if(intent.getAction().equals("feedEnd")){
            //处理结束投食
            LogUtil.d("结束投食");
            sportService.feedClose();
        }else if(intent.getAction().equals("navigateStartHandle")) {
            //处理开始巡航
            LogUtil.d("开始巡航");
            int duration = intent.getIntExtra("Duration",10);//duration是巡航时间
            sportService.navigateStart(duration);
        }else if(intent.getAction().equals("navigateEnd")) {
            //处理结束巡航
            LogUtil.d("结束巡航");
            sportService.navigateStop();
        }
    }


}
