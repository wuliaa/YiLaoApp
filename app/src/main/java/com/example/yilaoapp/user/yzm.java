package com.example.yilaoapp.user;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface yzm {
    @POST("sms")
    Call<ResponseBody> send_code(@Body yzmbean yzbean);
}
