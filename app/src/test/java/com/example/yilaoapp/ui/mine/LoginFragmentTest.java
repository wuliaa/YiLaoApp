package com.example.yilaoapp.ui.mine;


import com.example.yilaoapp.BuildConfig;
import com.example.yilaoapp.bean.Token;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.bean.tok;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.google.gson.Gson;

import org.junit.Test;
import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricTestRunner;
//import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;
//@RunWith(RobolectricTestRunner.class)
//@Config( constants = BuildConfig.class, sdk = 21)
public class LoginFragmentTest {

    @Test
    public void login() throws IOException {
        UserService service = new RetrofitUser().get().create(UserService.class);
        Call<ResponseBody> loginback = service.login_password("18825133593", "df3b72a07a0a4fa1854a48b543690eab", "887709912");
        Response<ResponseBody> response = loginback.execute();
        String str = "";
        try {
            str = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(response.code(),201);
    }

    @Test
    public void getInfo() throws IOException {
        UserService service = new RetrofitUser().get().create(UserService.class);
        Call<ResponseBody> loginback = service.login_password("18825133593", "df3b72a07a0a4fa1854a48b543690eab", "887709912");
        Response<ResponseBody> response = loginback.execute();
        String str = "";
        try {
            str = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        tok token = gson.fromJson(str, tok.class);
        Call<ResponseBody> userCall = service.get_user("18825133593", "df3b72a07a0a4fa1854a48b543690eab", token.getToken());
        Response<ResponseBody> response2 =userCall.execute();
        String u = null;
        try {
            u = response2.body().string();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Gson gson1 = new Gson();
        User user = gson1.fromJson(u, User.class);
        assertNotNull(user.getId_name());
    }
}