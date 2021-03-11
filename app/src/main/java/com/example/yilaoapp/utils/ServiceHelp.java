package com.example.yilaoapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yilaoapp.bean.tok;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceHelp {
    public static void UserUpdate(Context context, String key, String value, boolean flag, View v) {
        UserService service = new RetrofitUser().get().create(UserService.class);
        SharedPreferences pre = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        String mobile = pre.getString("mobile", "");
        String token = pre.getString("token", "");
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        Call<ResponseBody> updateInfo = service.updateInfo1(mobile,
                "df3b72a07a0a4fa1854a48b543690eab", token, map);
        updateInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("1", "success");
                SharedPreferences.Editor e = pre.edit();
                e.putString(key, value);
                e.apply();
                if(flag){
                    NavController controller = Navigation.findNavController(v);
                    controller.popBackStack();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("1", "failed");
            }
        });
    }

    public static void GetToken(Context context,String mobile,String password){
        UserService loginservice = new RetrofitUser().get().create(UserService.class);
        Call<ResponseBody> loginback = loginservice.login_password(mobile, "df3b72a07a0a4fa1854a48b543690eab", password);
        loginback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String str = "";
                try {
                    str = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                tok token = gson.fromJson(str, tok.class);
                SharedPreferences pre = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor e = pre.edit();
                e.putString("token", token.getToken());
                e.putString("mobile", mobile);
                e.putString("password", password);
                e.apply();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
