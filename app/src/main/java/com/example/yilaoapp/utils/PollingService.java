package com.example.yilaoapp.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.yilaoapp.R;
import com.example.yilaoapp.chat.activity.ChatActivity;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.chat_service;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollingService extends
        Service {

    public static final String ACTION = "com.example.yilaoapp.utils.PollingService";

    private Notification mNotification;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("start1");
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("start2");
        initNotifiManager();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("start");
        new PollingThread().start();
    }

    //初始化通知栏配置
    private void initNotifiManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int icon = R.drawable.default_icon;
        mNotification = new Notification();
        mNotification.icon = icon;
        mNotification.tickerText = "New Message";
        mNotification.defaults |= Notification.DEFAULT_SOUND;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    //弹出Notification
    private void showNotification() {
        mNotification.when = System.currentTimeMillis();
        //Navigator to the new activity when click the notification title
        Intent i = new Intent(this, ChatActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("mobile","13412101248");
        bundle.putString("uuid","uuid");
        bundle.putString("id_name","nickName");
        i.putExtra("bundle",bundle);
        @SuppressLint("WrongConstant")
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        /*Notification.Builder builder = new Notification.Builder(this);//新建Notification.Builder对象
       //PendingIntent点击通知后所跳转的页面
        //builder.setContentTitle("Bmob Test");
        builder.setContentText("message");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);//执行intent
        mNotification = builder.getNotification();//将builder对象转换为普通的notification*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String id = "channelId";
            String name = "channelName";
            NotificationChannel channel = new NotificationChannel(id,name,NotificationManager.IMPORTANCE_LOW);
            mManager.createNotificationChannel(channel);
            mNotification = new Notification.Builder(getApplicationContext())
                    .setChannelId(id)
                    .setContentTitle("This is content title O")
                    .setContentText("This is content text O")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                    .build();
        }else{
            mNotification  = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle("This is content title")
                    .setContentText("This is content text")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                    .build();
        }
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mManager.notify(0, mNotification);
    }

    int count = 0;
    class PollingThread extends Thread {
        @Override
        public void run() {
            System.out.println("Polling...");
            count ++;
            //当计数能被5整除时弹出通知
            if (count % 2 == 0) {
                chat_service ch=new RetrofitUser().get(getApplicationContext()).create(chat_service.class);
                SharedPreferences pre=getSharedPreferences("login", Context.MODE_PRIVATE);
                String token=pre.getString("token","");
                Call<ResponseBody> ch_back=ch.get_message("13060887368","13412101248",token,"df3b72a07a0a4fa1854a48b543690eab");
                ch_back.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            System.out.println(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                showNotification();
                System.out.println("New message!");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service:onDestroy");
    }

}