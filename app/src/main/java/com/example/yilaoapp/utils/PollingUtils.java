package com.example.yilaoapp.utils;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

public class PollingUtils {
    public static void startPollingService(Context context,int seconds,Class<?>cls,String action){
        //获取Alarm系统服务
        AlarmManager manager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //包装需要执行service包的Intent
        Intent intent =new Intent(context,cls);
        intent.setAction(action);

    }
}
