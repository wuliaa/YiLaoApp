package com.example.yilaoapp;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    public static Application mApplication;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication=this;
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
