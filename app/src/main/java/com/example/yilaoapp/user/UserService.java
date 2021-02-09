package com.example.yilaoapp.user;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    //登录
    //通过验证码
    //通过密码
    @POST("users/{mobile}/tokens")
    Call<String>  login_password(@Path("mobile")String mobile,@Query("appid")String appid,@Query("password")String password);

    //更新
    @PATCH("users/{mobile}")
    Call<String>  update(@Path("mobile")String mobile,@Query("appid")String app,@Query("token")String token,@Body messbean msee);

    //获取
//    @GET("users/{mobile}")
//    Call<User> get_message(@Path("mobile")String mobile,@Query("appid")String app,@Query("token")String token);

    //注册
    //发送验证码
    @FormUrlEncoded
    @POST("sms")
    Call<String> post_code(@FieldMap Map<String,String> message_map);//validation.py
    @GET("users/{mobile}")
    Call<User> get_acode(@Path("mobile") String mobile);//user.py

    //验证注册
    @PUT("users/{mobile}")
    Call<ResponseBody> sigin(@Path("mobile")String mobile, @Query("appid")String app, @Query("code") String code, @Body pd pp);


}
