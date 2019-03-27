package com.aiwac.cilentapp.patrobot;

import android.content.Context;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.lzy.okgo.OkGo;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;

import zuo.biao.library.base.BaseApplication;

public class PatClientApplication extends BaseApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // LogcatHelper.getInstance(this).start();

        context = getApplicationContext();
        ZXingLibrary.initDisplayOpinion(this);
        LogUtil.d( context.toString());


        //更新用的
        OkHttpUtils.getInstance()
                .init(this)
                .debug(true, "okHttp")
                .timeout(20 * 1000);
        OkGo.getInstance().init(this);

    }
    public static Context getContext(){
        return context;
    }
}
