package com.example.yilaoapp.service;

import com.example.yilaoapp.bean.Verify;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Verify_service {
    @POST("sms")
    Call<ResponseBody> send_code(@Body Verify yzbean);
}
