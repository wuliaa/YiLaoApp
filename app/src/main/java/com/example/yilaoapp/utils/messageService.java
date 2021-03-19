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
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.yilaoapp.MainActivity;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.bean.Mess;
import com.example.yilaoapp.chat.activity.ChatActivity;
import com.example.yilaoapp.database.chat.ChatDataBase;
import com.example.yilaoapp.database.dao.ChatDao;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.chat_retrofit;
import com.example.yilaoapp.service.chat_service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.greenrobot.eventbus.EventBus.TAG;

public class messageService extends Service {
    //获取消息线程
    private MessageThread messageThread = null;

    //点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;
    ChatDataBase chatDataBase;
    ChatDao chatDao;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //初始化

        flags = START_STICKY;
        messageNotification = new Notification();
        messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //开启线程
        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器端获取消息
     */
    class MessageThread extends Thread {
        //运行状态，下一步骤有大用
        public boolean isRunning = true;

        public void run() {
            while (isRunning) {
                try {
                    //休息3秒
                    Thread.sleep(3000);
                    //获取服务器消息
                       /* Notification.Builder builder = new Notification.Builder(getApplicationContext());//新建Notification.Builder对象
                        //PendingIntent点击通知后所跳转的页面
                        builder.setContentTitle("Bmob Test");*/
                    chat_service ch = new chat_retrofit().get().create(chat_service.class);
                    SharedPreferences pre = getSharedPreferences("login", Context.MODE_PRIVATE);
                    String phone = pre.getString("mobile", "");
                    String token = pre.getString("token", "");
                    SharedPreferences pre1 = getSharedPreferences(phone, Context.MODE_PRIVATE);
                    String min_id = pre1.getString("min_id", "");
                    if (min_id.equals(""))
                        min_id = "0";
                    Call<ResponseBody> ch_back = ch.get_message(phone, "0", token, "df3b72a07a0a4fa1854a48b543690eab", min_id);
                    ch_back.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() / 100 == 4) {
                                //Toast.makeText(getApplicationContext(), "401", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    if (response.body() != null) {
                                        List<Mess> ms = new LinkedList<>();
                                        String str = "";
                                        str = response.body().string();
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<List<Mess>>() {
                                        }.getType();
                                        response.body().close();
                                        ms = gson.fromJson(str, type);
                                        if (ms.size() > 0) {//有新的聊天记录
                                            chatDataBase = ChatDataBase.getDatabase(getApplicationContext());
                                            chatDao = chatDataBase.getChatDao();
                                            List<Mess> finalMs = ms;
                                            new Thread() {
                                                public void run() {
                                                    for (int i = 0; i < finalMs.size(); i++) {
                                                        if(!chatDao.hasItem(finalMs.get(i).getId()))
                                                            chatDao.insert(finalMs.get(i));
                                                    }
                                                }
                                            }.start();///855
                                            SharedPreferences.Editor e = pre1.edit();
                                            e.putString("min_id", String.valueOf(ms.get(ms.size() - 1).getId() + 1));
                                            e.commit();
                                            if (judge_work.isBackground(getApplicationContext())) {//在后台运行
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    messageIntent = new Intent(getApplicationContext(), MainActivity.class);//跳转
                                                    messagePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, messageIntent, 0);
                                                    String id = "channelId";
                                                    String name = "channelName";
                                                    NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
                                                    messageNotificatioManager.createNotificationChannel(channel);
                                                    messageNotification = new Notification.Builder(getApplicationContext())
                                                            .setChannelId(id)
                                                            .setContentTitle("YilaoApp")
                                                            .setContentText("您有新消息，请及时查看！")
                                                            .setWhen(System.currentTimeMillis())
                                                            .setSmallIcon(R.mipmap.ic_launcher)//图标设置
                                                            .setContentIntent(messagePendingIntent)
                                                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                                            .build();
                                                } else {
                                                    messageIntent = new Intent(getApplicationContext(), ChatActivity.class);//跳转
                                                    messagePendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, messageIntent, 0);
                                                    messageNotification = new NotificationCompat.Builder(getApplicationContext())
                                                            .setContentTitle("YilaoApp")
                                                            .setContentText("您有新消息，请及时查看！")
                                                            .setWhen(System.currentTimeMillis())
                                                            .setContentIntent(messagePendingIntent)
                                                            .setSmallIcon(R.mipmap.ic_launcher)
                                                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                                            .build();
                                                }
                                                messageNotification.flags = Notification.FLAG_AUTO_CANCEL;
                                                messageNotificatioManager.notify(0, messageNotification);
                                            }
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        //System.exit(0);
        super.onDestroy();
    }
}
