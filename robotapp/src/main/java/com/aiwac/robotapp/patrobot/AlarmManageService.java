package com.aiwac.robotapp.patrobot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.aiwac.robotapp.patrobot.activity.MainActivity;
import com.aiwac.robotapp.patrobot.activity.timeReceiver;

import java.util.Calendar;

import static android.icu.text.DateTimePatternGenerator.DAY;

public class AlarmManageService {
    public static AlarmManager alarmManager;
    private static String TAG = "AlarmManageService";
    private static int hour,minute;

    public static void addAlarm(Context context, int requestCode, Bundle bundle, String[] time,int flag){
        Intent intent = new Intent(context, timeReceiver.class);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode,intent,0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        for(int i = 0; i <time.length;i ++){
            if(flag == 0){
                if(bundle.getString("type").equals("feed")){
                    hour = Integer.valueOf(time[i].split(":")[0]).intValue();
                    minute = Integer.valueOf(time[i].split(":")[1]).intValue();
                }else if(bundle.getString("type").equals("navigate")){
                    String startTime = time[i].split("-")[0];
                    hour = Integer.valueOf(startTime.split(":")[0]).intValue();
                    minute = Integer.valueOf(startTime.split(":")[1]).intValue();
                }
            }else if(flag == 1){
                if(bundle.getString("type").equals("feed")){
                    hour = Integer.valueOf(time[i].split(":")[0]).intValue();
                    minute = Integer.valueOf(time[i].split(":")[1]).intValue()+30;
                }else if(bundle.getString("type").equals("navigate")){
                    String endTime = time[i].split("-")[1];
                    hour = Integer.valueOf(endTime.split(":")[0]).intValue();
                    minute = Integer.valueOf(endTime.split(":")[1]).intValue();
                }
            }
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            //注册新提醒
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),DAY,pendingIntent);
        }
    }

}
