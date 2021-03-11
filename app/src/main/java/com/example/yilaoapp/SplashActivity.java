package com.example.yilaoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.yilaoapp.bean.tok;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yilaoapp.MyApplication.getContext;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //自动登录
        SharedPreferences pre1 = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        String token1 = pre1.getString("token", "");
        new Thread() {
            public void run() {
                if (!token1.equals("")) {
                    String mob = pre1.getString("mobile", "");
                    String pas = pre1.getString("password", "");
                    UserService loginservice = new RetrofitUser().get().create(UserService.class);
                    Call<ResponseBody> loginback = loginservice.login_password(mob, "df3b72a07a0a4fa1854a48b543690eab", pas);
                    loginback.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() / 100 == 4) {
                                Toast.makeText(getContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                            } else if (response.code() / 100 == 5) {
                                Toast.makeText(getContext(), "服务器错误", Toast.LENGTH_SHORT).show();
                            } else {
                                String str = "";
                                try {
                                    str = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Gson gson = new Gson();
                                tok token = gson.fromJson(str, tok.class);
                                SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor e = pre.edit();
                                e.putString("token", token.getToken());
                                e.apply(); //apply()比commit()更快
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
        finish();
    }
}