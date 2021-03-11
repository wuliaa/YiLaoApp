package com.example.yilaoapp.ui.mine;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yilaoapp.bean.Password;
import com.example.yilaoapp.bean.Verify;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.example.yilaoapp.service.Verify_service;
import com.kongzue.dialog.v3.TipDialog;

import org.junit.Test;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

public class SigninFragmentTest {

    @Test
    public void getCode() throws IOException {
        Verify_service yzmservice=new RetrofitUser().get().create(Verify_service.class);
        Verify yz=new Verify("df3b72a07a0a4fa1854a48b543690eab","18825133593","PUT","/v1.0/users/"+"18825133593");
        Call<ResponseBody> callback=yzmservice.send_code(yz);
        Response<ResponseBody> response=callback.execute();
        assertEquals(response.code(),201);
    }

    @Test
    public void signin() throws IOException {
        UserService xybservice=new RetrofitUser().get().create(UserService.class);
        Password pass =new Password("887709912");
        Call<ResponseBody> xybback=xybservice.sigin("18825133593","df3b72a07a0a4fa1854a48b543690eab","1234",pass);
        Response<ResponseBody> response=xybback.execute();
        assertEquals(response.code(),201);

    }
}