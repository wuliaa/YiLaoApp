package com.example.yilaoapp.ui.errands;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.DiffUtil;
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
import com.example.yilaoapp.bean.chat_task;
import com.example.yilaoapp.databinding.FragmentErrandsBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.accept_service;
import com.example.yilaoapp.service.chat_service;
import com.example.yilaoapp.service.errand_service;
import com.example.yilaoapp.utils.AdapterDiffCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrandsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private DrawerLayout mDrawerLayout;
    private List<All_orders> errandList;
    FragmentErrandsBinding binding;
    List<All_orders> all;
    List<Integer> task_id;
    ErrandAdapter adapter;
    Handler handler;
    Callback<ResponseBody> callback;

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

    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_errands, container, false);
        ErrandsViewModel mViewModel = ViewModelProviders.of(requireActivity()).get(ErrandsViewModel.class);
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
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.errandRecyclerview.setLayoutManager(layoutManager);
                    Log.d("errandlist", "onCreateView: " + errandList.size());
                    adapter = new ErrandAdapter(errandList);
                    binding.errandRecyclerview.setHasFixedSize(true);
                    binding.errandRecyclerview.setAdapter(adapter);
                    adapter.setOnItemClickListener(new ErrandAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, All_orders data, int position) {
                            Log.d("errandlist", "OnItemClick: 13");
                            SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                            String mobile = pre.getString("mobile", "");
                            String token = pre.getString("token", "");
                            accept_service ac = new RetrofitUser().get(getContext()).create(accept_service.class);
                            Call<ResponseBody> act = ac.accept_order(mobile, data.getId(), token, "df3b72a07a0a4fa1854a48b543690eab", "true");
                            act.enqueue(callback = new retrofit2.Callback<ResponseBody>() {
                                @Override
                                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                    Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();
                                    chat_task ch = new chat_task("您的任务我已领取，订单信息如下:" + data.getDetail(), data.getFrom_user());
                                    chat_service send = new RetrofitUser().get(getContext()).create(chat_service.class);
                                    Call<ResponseBody> sen_mes = send.send_message(mobile, token, "df3b72a07a0a4fa1854a48b543690eab", ch);
                                    sen_mes.enqueue(callback = new retrofit2.Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                            Toast.makeText(getContext(), "信息已success", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                                }
                            });
                            List<All_orders> oldDatas = errandList;
                            errandList.remove(position);
                            adapter.notifyItemRemoved(position);
                            if (position != errandList.size()) { // 如果移除的是最后一个，忽略
                                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new AdapterDiffCallback<>(oldDatas, errandList), true);
                                diffResult.dispatchUpdatesTo(adapter);
                            }
                        }
                    });
                }
            }
        };


//        binding.errandRecyclerview.requestLayout();
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item,
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment));
        return true;
    }

    private void initErrands() {
        new Thread() {
            public void run() {
                errandList.clear();
                task_id.clear();
                errand_service errand = new RetrofitUser().get(getContext()).create(errand_service.class);
                Call<ResponseBody> get_errand = errand.get_orders("跑腿");
                get_errand.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String str = "";
                        try {
                            if (response.body() == null)
                                Toast.makeText(getContext(), "网络延迟", Toast.LENGTH_SHORT).show();
                            else {
                                str = response.body().string();
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<All_orders>>() {
                                }.getType();
                                all = gson.fromJson(str, type);
                                for (int i = 0; i < all.size(); i++) {
                                    if (!task_id.contains(all.get(i).getId()) && all.get(i).getExecutor() == null) {
                                        Log.d("executor", "onResponse" + i + ": " + all.get(i).getExecutor());
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
                                        Message message = new Message();
                                        message.what = 1;
                                        //然后将消息发送出去
                                        handler.sendMessage(message);
                                        Log.d("errand", "message: " + content + "1" + address + "2" + money + "3" + time);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    }
                });
            }
        }.start();
    }

    @Override
    public void onRefresh() {
        initErrands();
        binding.swipeErrands.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeErrands.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        }, 1000);
    }
}