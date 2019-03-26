package com.aiwac.robotapp.patrobot.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class timeReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle.get("type").equals("feed") && bundle.get("condition").equals("start")){
            //处理开始投食

        }else if(bundle.get("type").equals("feed") && bundle.get("condition").equals("end")){
            //处理结束投食

        }else if(bundle.get("type").equals("navigate") && bundle.get("condition").equals("start")){
            //处理开始巡航

        }else if(bundle.get("type").equals("navigate") && bundle.get("condition").equals("end")){
            //处理结束巡航

        }


    }

}
