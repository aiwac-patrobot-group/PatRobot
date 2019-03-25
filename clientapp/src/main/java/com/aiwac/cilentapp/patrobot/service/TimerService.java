package com.aiwac.cilentapp.patrobot.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.aiwac.cilentapp.patrobot.server.WebSocketApplication;
import com.aiwac.cilentapp.patrobot.utils.JsonUtil;
import com.aiwac.robotapp.commonlibrary.task.ThreadPoolManager;

public class TimerService extends Service {
    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
            }
        });
        long videoTime = (long)intent.getSerializableExtra("videoDuration");
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //int videoTime = 100000;  //视频时长
        long triggerAtTime = SystemClock.elapsedRealtime()+videoTime;
        Intent i = new Intent(this,TimerService.class);
       // PendingIntent pi = PendingIntent().getService(this,0,i,0);
       // manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime.pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
