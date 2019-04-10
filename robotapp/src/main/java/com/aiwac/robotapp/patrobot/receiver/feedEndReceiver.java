package com.aiwac.robotapp.patrobot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

public class feedEndReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //LogUtil.d(intent.getAction());
        //处理结束投食
        LogUtil.d("结束投食");
    }
}
