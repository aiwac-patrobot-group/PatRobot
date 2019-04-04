package com.aiwac.robotapp.patrobot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

public class timeReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        LogUtil.d(bundle.get("type")+"");
        if(bundle.get("type").equals("feed") && bundle.get("condition").equals("start")){
            //处理开始投食
            LogUtil.d("开始投食");

        }else if(bundle.get("type").equals("feed") && bundle.get("condition").equals("end")){
            //处理结束投食
            LogUtil.d("结束投食");

        }else if(bundle.get("type").equals("navigate") && bundle.get("condition").equals("start")){
            //处理开始巡航
            LogUtil.d("开始巡航");

        }else if(bundle.get("type").equals("navigate") && bundle.get("condition").equals("end")){
            //处理结束巡航
            LogUtil.d("结束巡航");

        }
    }

}
