package com.example.yilaoapp.ui.mine;


import com.example.yilaoapp.BuildConfig;
import com.example.yilaoapp.bean.Token;
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
        LoginFragment loginFragment = new LoginFragment();
        UserService service = new RetrofitUser().get().create(UserService.class);
        Call<ResponseBody> loginback = service.login_password("18825133593", "df3b72a07a0a4fa1854a48b543690eab", "887709912");
        Response<ResponseBody> response = loginback.execute();
        //loginFragment.getCallBack().onResponse(null,response);
        String str = "";
        try {
            str = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        tok token = gson.fromJson(str, tok.class);
        //assertEquals(token.getToken(), "519f07255a294261b97ece794c3fb328");
        assertEquals(response.code(),201);
    }

    @Test
    public void getInfo() {
    }
}