package com.aiwac.robotapp.patrobot;

import android.content.Context;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;

import zuo.biao.library.base.BaseApplication;

public class PatRobotApplication  extends BaseApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // LogcatHelper.getInstance(this).start();

        context = getApplicationContext();
        LogUtil.d( context.toString());
    }

    public static Context getContext(){
        return context;
    }
}
