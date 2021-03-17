package com.example.yilaoapp.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class judge_work {
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    //Log.i(context.getPackageName(), "后台"
                           // + appProcess.processName);
                    return true;
                } else {
                    //Log.i(context.getPackageName(), "前台"
                            //+ appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
