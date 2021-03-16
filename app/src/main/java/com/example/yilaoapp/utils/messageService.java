package com.example.yilaoapp.utils;

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

import static org.greenrobot.eventbus.EventBus.TAG;

public class messageService extends Service {
    //获取消息线程
    private MessageThread messageThread = null;

    //点击查看
    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    //通知栏消息
    private int messageNotificationID = 1000;
    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //初始化
        messageNotification = new Notification();
        messageNotification.icon = R.drawable.ic_launcher_background;
        messageNotification.tickerText = "新消息";
        messageNotification.defaults = Notification.DEFAULT_SOUND;
        messageNotificatioManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        messageIntent = new Intent(this,ChatActivity.class);//跳转
        messagePendingIntent = PendingIntent.getActivity(this,0,messageIntent,0);

        //开启线程
        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();
        System.out.println("start");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器端获取消息
     *
     */
    class MessageThread extends Thread{
        //运行状态，下一步骤有大用
        public boolean isRunning = true;
        public void run() {
            while(isRunning){
                try {
                    //休息10分钟
                    Thread.sleep(3000);
                    //获取服务器消息
                    String serverMessage = getServerMessage();
                    if(serverMessage!=null&&!"".equals(serverMessage)){
                       /* Notification.Builder builder = new Notification.Builder(getApplicationContext());//新建Notification.Builder对象
                        //PendingIntent点击通知后所跳转的页面
                        builder.setContentTitle("Bmob Test");*/
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
                       /* builder.setContentText("奥巴马宣布,本拉登兄弟挂了!"+serverMessage);
                        builder.setSmallIcon(R.mipmap.ic_launcher);
                        builder.setContentIntent(messagePendingIntent);//执行intent
                        //更新通知栏
                        System.out.println(serverMessage);
                        messageNotification=builder.getNotification();*/
                        //创建Notification，传入Context和channelId
                        /*Notification notification = new NotificationCompat.Builder(getApplicationContext())
                                .setAutoCancel(true)
                                .setContentTitle("收到聊天消息")
                                .setContentText("今天晚上吃什么")
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                .setContentIntent(messagePendingIntent)
                                .setPriority(Notification.PRIORITY_DEFAULT)
                                .setChannelId("com.example.yilaoapp.utils.messageService")
                                //在build()方法之前还可以添加其他方法
                                .build();*/
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            String id = "channelId";
                            String name = "channelName";
                            NotificationChannel channel = new NotificationChannel(id,name,NotificationManager.IMPORTANCE_LOW);
                            messageNotificatioManager.createNotificationChannel(channel);
                            messageNotification = new Notification.Builder(getApplicationContext())
                                    .setChannelId(id)
                                    .setContentTitle("This is content title O")
                                    .setContentText("This is content text O")
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                                    .build();
                        }else{
                            messageNotification = new NotificationCompat.Builder(getApplicationContext())
                                    .setContentTitle("This is content title")
                                    .setContentText("This is content text")
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                                    .build();
                        }
                        messageNotification.flags = Notification.FLAG_AUTO_CANCEL;
                        messageNotificatioManager.notify(messageNotificationID,messageNotification);
                        //每次通知完，通知ID递增一下，避免消息覆盖掉
                        messageNotificationID++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 这里以此方法为服务器Demo，仅作示例
     * @return 返回服务器要推送的消息，否则如果为空的话，不推送
     */
    public String getServerMessage(){
        return "YES!";
    }
    @Override
    public void onDestroy() {
        System.exit(0);
        //或者，二选一，推荐使用System.exit(0)，这样进程退出的更干净
        //messageThread.isRunning = false;
        super.onDestroy();
    }
}
