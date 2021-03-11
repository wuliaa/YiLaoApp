package com.example.yilaoapp.ui.errands;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.bean.Point_address;
import com.example.yilaoapp.databinding.FragmentErrandsBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.errand_service;
import com.example.yilaoapp.service.image_service;
import com.example.yilaoapp.ui.bulletin.BullentinViewModel;
import com.example.yilaoapp.ui.bulletin.Share;
import com.example.yilaoapp.ui.bulletin.ShareAdapter;
import com.example.yilaoapp.ui.bulletin.Team;
//import com.example.yilaoapp.utils.LruCacheImageLoader;
import com.example.yilaoapp.utils.PhotoOperation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrandsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ErrandsViewModel mViewModel;
    private DrawerLayout mDrawerLayout;
    private List<All_orders> errandList;
    FragmentErrandsBinding binding;
    List<All_orders> all  ;
    List<Integer> task_id ;

    public ErrandsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        errandList = new ArrayList<>();
        all = new LinkedList<>();
        task_id = new LinkedList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_errands, container, false);
        mViewModel = ViewModelProviders.of(requireActivity()).get(ErrandsViewModel.class);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(requireActivity());
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).
                getSupportActionBar()).setDisplayShowTitleEnabled(false);
        binding.toolbar.inflateMenu(R.menu.menu_main);
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24);
        mDrawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setHasOptionsMenu(true);

        initErrands();
        binding.swipeErrands.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.errandRecyclerview.setLayoutManager(layoutManager);
        ErrandAdapter adapter = new ErrandAdapter(errandList);
        binding.errandRecyclerview.setHasFixedSize(true);
        binding.errandRecyclerview.setAdapter(adapter);


        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new ErrandAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, All_orders data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        Toast.makeText(getActivity(), "我是item", Toast.LENGTH_SHORT).show();
//                        mViewModel.setErrand(data);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                adapter.notifyDataSetChanged();
//                adapter.notifyItemInserted(adapter.getItemCount());
//            }
//        },  1000);
        adapter.notifyItemInserted(adapter.getItemCount());
//        binding.errandRecyclerview.requestLayout();
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavigationUI.onNavDestinationSelected(item,
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment));
        return true;
    }

    private void initErrands() {
        errand_service errand = new RetrofitUser().get().create(errand_service.class);
        Call<ResponseBody> get_errand = errand.get_orders("跑腿");
        get_errand.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String str = "";
                try {
                    str = response.body().string();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<All_orders>>() {
                    }.getType();
                    all = gson.fromJson(str, type);
                    for (int i = 0; i < all.size(); i++) {
                        if (!task_id.contains(all.get(i).getId())) {
                            task_id.add(all.get(i).getId());
                            String content = all.get(i).getDetail();
                            Point_address address = all.get(i).getDestination();
                            String money = String.valueOf(all.get(i).getReward());
                            String time = all.get(i).getCreate_at();
                            BigInteger phone = all.get(i).getFrom_user();
                            String protected_info = all.get(i).getProtected_info();
                            String uuid = all.get(i).getId_photo();
                            All_orders errand1 = new All_orders(phone, address, time, task_id.get(i),
                                    content, Float.parseFloat(money), protected_info, uuid);
                            errandList.add(errand1);
                            Log.d("errand", "message: " + content + "1" + address + "2" + money + "3" + time);
                            // photo.add()
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        errand_service errand = new RetrofitUser().get().create(errand_service.class);
        Call<ResponseBody> get_errand = errand.get_orders("跑腿");
        get_errand.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String str = "";
                try {
                    str = response.body().string();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<All_orders>>() {
                    }.getType();
                    all = gson.fromJson(str, type);
                    for (int i = 0; i < all.size(); i++) {
                        if (!task_id.contains(all.get(i).getId())) {
                            task_id.add(all.get(i).getId());
                            String content = all.get(i).getDetail();
                            Point_address address = all.get(i).getDestination();
                            String money = String.valueOf(all.get(i).getReward());
                            String time = all.get(i).getCreate_at();
                            BigInteger phone = all.get(i).getFrom_user();
                            String protected_info = all.get(i).getProtected_info();
                            String uuid = all.get(i).getId_photo();
                            All_orders errand1 = new All_orders(phone, address, time, task_id.get(i),
                                    content, Float.parseFloat(money), protected_info, uuid);
                            errandList.add(errand1);
                            Log.d("errand", "message: " + content + "1" + address + "2" + money + "3" + time);
                            // photo.add()
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        binding.swipeErrands.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeErrands.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        }, 3000);
    }
}