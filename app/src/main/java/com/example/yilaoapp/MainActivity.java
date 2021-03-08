package com.example.yilaoapp;

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


public class MainActivity extends AppCompatActivity {
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
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationUI.setupWithNavController(navigationView, navController);
        //抽屉导航栏的用户信息
        UserService service = new RetrofitUser().get().create(UserService.class);
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
                    Toast.makeText(getApplicationContext(),"失败",Toast.LENGTH_SHORT).show();
                } else {
                    String str = "";
                    try {
                        str = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    User user = gson.fromJson(str, User.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (user.getId_name() != null) {
                                TextView nick = findViewById(R.id.mnickn);
                                nick.setText(user.getId_name());
                            }
                        }
                    });
                    if(user.getId_photo()!=null){
                        BigInteger mobile=user.getMobile();
                        image_service load=new RetrofitUser().get().create(image_service.class);
                        Call<ResponseBody> load_back=load.load_photo(mobile,user.getId_photo(),"df3b72a07a0a4fa1854a48b543690eab");
                        load_back.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                assert response.body() != null;
                                InputStream photo = null;
                                photo = response.body().byteStream();
                                PhotoOperation Operation = new PhotoOperation();
                                byte[] ba = null;
                                try {
                                    ba = Operation.read(photo);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Bitmap bmp = Operation.ByteArray2Bitmap(ba);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bmp != null) {
                                            ImageView portrait = findViewById(R.id.pcircularImageView);
                                            portrait.setImageBitmap(bmp);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("1", "failed");
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