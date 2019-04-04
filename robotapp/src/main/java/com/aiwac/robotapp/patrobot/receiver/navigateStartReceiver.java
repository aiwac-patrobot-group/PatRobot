package com.aiwac.robotapp.patrobot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

public class navigateStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //处理开始巡航
        LogUtil.d("开始巡航");
    }
}
