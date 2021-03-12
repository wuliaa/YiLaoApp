package com.example.yilaoapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import android.telecom.CallScreeningService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.bean.messbean;
import com.example.yilaoapp.bean.tok;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.example.yilaoapp.service.image_service;
import com.example.yilaoapp.utils.PhotoOperation;
import com.example.yilaoapp.utils.ServiceHelp;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yilaoapp.MyApplication.getContext;


public class  MainActivity extends AppCompatActivity {
    private NavController navController;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置状态栏半透明
        StatusBarUtil.setTranslucent(this, 100);
        //抽屉式导航栏
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navigationView = findViewById(R.id.navigationView);
        View headerview=navigationView.inflateHeaderView(R.layout.navigation_view_header_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationUI.setupWithNavController(navigationView, navController);
        //抽屉导航栏的用户信息
        ImageView portrait = (ImageView)headerview.findViewById(R.id.pcircularImageView);
        TextView nick = (TextView)headerview.findViewById(R.id.mnickn);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                if(pre.contains("id_name")){
                    nick.setText(pre.getString("id_name",""));
                }
                if(pre.contains("id_photo")){
                    String url = "http://api.yilao.tk:15000/v1.0/users/" + pre.getString("mobile","") + "/resources/" +
                            pre.getString("id_photo","");
                    Glide.with(getApplicationContext())
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(portrait);
                }else {
                    getNickAddPortrait(portrait, nick);
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        //底部导航栏
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.bullentinFragment ||
                    destination.getId() == R.id.purchaseFragment ||
                    destination.getId() == R.id.errandsFragment) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }

    public void getNickAddPortrait(ImageView portrait,TextView nick){
        UserService service = new RetrofitUser().get(getContext()).create(UserService.class);
        SharedPreferences pre = this.getSharedPreferences("login", Context.MODE_PRIVATE);
        String mobile = pre.getString("mobile", "");
        String token = pre.getString("token", "");
        Call<ResponseBody> get = service.get_user(mobile, "df3b72a07a0a4fa1854a48b543690eab", token);
        get.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() / 100 == 4) {
//                    ServiceHelp.GetToken(getApplicationContext(), mobile, password2);
//                    onCreate(savedInstanceState);
                    Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
                } else {
                    String str = "";
                    try {
                        str = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    User user = gson.fromJson(str, User.class);
                    if (user.getId_name() != null) {
                        nick.setText(user.getId_name());
                    }
                    if (user.getId_photo() != null) {
                        BigInteger mobile = user.getMobile();
                        String url = "http://api.yilao.tk:15000/v1.0/users/" + mobile + "/resources/" +
                                user.getId_photo();
                        Glide.with(getApplicationContext())
                                .load(url)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(portrait);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("1", "failed");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout) || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText
            // （特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，
     * 来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，
        // 第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert im != null;
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}