package com.aiwac.robotapp.patrobot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aiwac.robotapp.commonlibrary.utils.LogUtil;
import com.aiwac.robotapp.patrobot.receiver.timeReceiver;

import java.util.Calendar;
import java.util.TimeZone;


public class AlarmManageService {
    public   static AlarmManager alarmManager1,alarmManager2;
    private static String TAG = "AlarmManageService";
    private static int hour,minute;


    public static void  addAlarm1(Context context, String[] time,int flag){
        Intent intent = new Intent(context, timeReceiver.class);
        PendingIntent pendingIntent;
        for(int i = 0; i <time.length;i ++) {
            if (flag == 0) {
                hour = Integer.valueOf(time[i].split(":")[0]).intValue();
                minute = Integer.valueOf(time[i].split(":")[1]).intValue();
                intent.setAction("feedStart");

            } else if (flag == 1) {
                    hour = Integer.valueOf(time[i].split(":")[0]).intValue();
                    if(Integer.valueOf(time[i].split(":")[1]).intValue()<30)
                        minute = Integer.valueOf(time[i].split(":")[1]).intValue() + 30;
                    else{
                        minute=Integer.valueOf(time[i].split(":")[1]).intValue()-30;
                        if(hour<23)
                            hour = hour+1;
                        else
                            hour = 0;
                    }
                    //intent = new Intent(context, feedEndReceiver.class);
                    LogUtil.d("结束hour:"+hour);
                    LogUtil.d("结束minute:"+minute);
                    intent.setAction("feedEnd");
            }
            pendingIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            //注册新提醒
            alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            // 这里时区需要设置一下，不然会有8个小时的时间差
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            // 选择的定时时间
            long selectTime = calendar.getTimeInMillis();
            long systemTime = System.currentTimeMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
            if(systemTime > selectTime) {
                LogUtil.d("当前时间大于设置的时间");
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pendingIntent);
            LogUtil.d("闹钟设置完毕");
        }
    }
    public  static void addAlarm2(Context context,String[] time,int flag){
        Intent intent = new Intent(context, timeReceiver.class);
        PendingIntent pendingIntent;

        for(int i = 0; i <time.length;i ++){
            if(flag == 0){
                    String startTime = time[i].split("-")[0];
                    hour = Integer.valueOf(startTime.split(":")[0]).intValue();
                    minute = Integer.valueOf(startTime.split(":")[1]).intValue();
                intent.setAction("navigateStart");
            }else if(flag == 1){
                    String endTime = time[i].split("-")[1];
                    hour = Integer.valueOf(endTime.split(":")[0]).intValue();
                    minute = Integer.valueOf(endTime.split(":")[1]).intValue();
                intent.setAction("navigateEnd");
            }
            pendingIntent = PendingIntent.getBroadcast(context,i,intent,PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            // 这里时区需要设置一下，不然会有8个小时的时间差
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            // 选择的定时时间
            long selectTime = calendar.getTimeInMillis();
            long systemTime = System.currentTimeMillis();
            // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
            if(systemTime > selectTime) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            //注册新提醒
            alarmManager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),(1000 * 60 * 60 * 24),pendingIntent);
            LogUtil.d("闹钟设置完毕");
        }
    }

}
