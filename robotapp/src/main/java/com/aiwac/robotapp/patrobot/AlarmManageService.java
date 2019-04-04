package com.aiwac.robotapp.patrobot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.receiver.feedEndReceiver;
import com.aiwac.robotapp.patrobot.receiver.feedStartReceiver;
import com.aiwac.robotapp.patrobot.receiver.navigateEndReceiver;
import com.aiwac.robotapp.patrobot.receiver.navigateStartReceiver;

import java.util.Calendar;
import java.util.TimeZone;

import static android.icu.text.DateTimePatternGenerator.DAY;

public class AlarmManageService {
    public   static AlarmManager alarmManager1,alarmManager2;
    private static String TAG = "AlarmManageService";
    private static int hour,minute;


    public static void  addAlarm1(Context context, Bundle bundle, String[] time,int flag){
        Intent intent = new Intent(context,feedStartReceiver.class);

        for(int i = 0; i <time.length;i ++) {
            if (flag == 0) {
                hour = Integer.valueOf(time[i].split(":")[0]).intValue();
                minute = Integer.valueOf(time[i].split(":")[1]).intValue();
                intent = new Intent(context, feedStartReceiver.class);

            } else if (flag == 1) {
                    hour = Integer.valueOf(time[i].split(":")[0]).intValue();
                    minute = Integer.valueOf(time[i].split(":")[1]).intValue() + 30;
                    //LogUtil.d("喂食结束时间");
                    intent = new Intent(context, feedEndReceiver.class);
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            // 这里时区需要设置一下，不然会有8个小时的时间差
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            // 选择的定时时间
            long selectTime = calendar.getTimeInMillis();
            long systemTime = System.currentTimeMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
            if(systemTime > selectTime) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                selectTime = calendar.getTimeInMillis();
            }
// 计算现在时间到设定时间的时间差
            long time1 = selectTime - systemTime;
            long firstTime = SystemClock.elapsedRealtime();    // 开机之后到现在的运行时间(包括睡眠时间)
            firstTime += time1;
            //注册新提醒
            alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, DAY, pendingIntent);
            LogUtil.d("闹钟设置完毕");
        }
    }
    public  static void addAlarm2(Context context, Bundle bundle, String[] time,int flag){
        Intent intent = new Intent(context,navigateStartReceiver.class);

        for(int i = 0; i <time.length;i ++){
            if(flag == 0){
                    String startTime = time[i].split("-")[0];
                    hour = Integer.valueOf(startTime.split(":")[0]).intValue();
                    minute = Integer.valueOf(startTime.split(":")[1]).intValue();
                    LogUtil.d("巡航开始时间");
                intent = new Intent(context, navigateStartReceiver.class);
            }else if(flag == 1){
                    String endTime = time[i].split("-")[1];
                    hour = Integer.valueOf(endTime.split(":")[0]).intValue();
                    minute = Integer.valueOf(endTime.split(":")[1]).intValue();
                    LogUtil.d("巡航结束时间");
                intent = new Intent(context, navigateEndReceiver.class);
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,i,intent,0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            // 选择的定时时间
            long selectTime = calendar.getTimeInMillis();
            long systemTime = System.currentTimeMillis();
            // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
            if(systemTime > selectTime) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                selectTime = calendar.getTimeInMillis();
            }
            // 计算现在时间到设定时间的时间差
            long time1 = selectTime - systemTime;
            long firstTime = SystemClock.elapsedRealtime();    // 开机之后到现在的运行时间(包括睡眠时间)
            firstTime += time1;
            //注册新提醒
            alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP,firstTime,DAY,pendingIntent);
            LogUtil.d("闹钟设置完毕");
        }
    }

}
