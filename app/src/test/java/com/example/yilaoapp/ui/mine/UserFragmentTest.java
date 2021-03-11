package com.example.yilaoapp.ui.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.yilaoapp.MyApplication;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.Uuid;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.image_service;
import com.example.yilaoapp.utils.PhotoOperation;
import com.google.gson.Gson;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

public class UserFragmentTest {

    @Test
    public void postPortrait() throws IOException {
        byte[] ba = null;
        UserFragment userFragment=new UserFragment();
        Resources res = userFragment.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.touxiang);
        PhotoOperation Operation = new PhotoOperation();
        ba = Operation.Bitmap2ByteArray(bmp);
//        SharedPreferences pre2 = MyApplication.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
//        String mobile2 = pre2.getString("mobile", "");
//        String token2 = pre2.getString("token", "");
//        Map<String, RequestBody> map = new HashMap<>();
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/from-data"), ba);
//        //注意：file就是与服务器对应的key,后面filename是服务器得到的文件名
//        map.put("file\"; filename=\"" + "1.jpeg", requestFile);
//        image_service img = new RetrofitUser().get().create(image_service.class);
//        Call<ResponseBody> image_call = img.send_photo(mobile2, token2, "df3b72a07a0a4fa1854a48b543690eab", map);
//        Response<ResponseBody> response=image_call.execute();
//        assertEquals(response.code(),201);
    }
}