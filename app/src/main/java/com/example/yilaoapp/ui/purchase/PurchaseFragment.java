package com.example.yilaoapp.ui.purchase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.bean.Point_address;
import com.example.yilaoapp.bean.chat_task;
import com.example.yilaoapp.databinding.FragmentPurchaseBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.accept_service;
import com.example.yilaoapp.service.chat_service;
import com.example.yilaoapp.service.image_service;
import com.example.yilaoapp.service.pur_service;
import com.example.yilaoapp.utils.AdapterDiffCallback;
import com.example.yilaoapp.ui.errands.ErrandAdapter;
import com.example.yilaoapp.ui.errands.ErrandsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Vector;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yilaoapp.MyApplication.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FragmentPurchaseBinding binding;
    private DrawerLayout mDrawerLayout;
    private PurchaseViewModel mViewModel;
    private List<All_orders> purchaseList;
    List<Integer> task_id;   //订单id
    PurchaseAdapter adapter;
    Handler handler;


    public PurchaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        purchaseList = new ArrayList<>();
        task_id = new LinkedList<>();
    }


    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for t his fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase, container, false);
        mViewModel = ViewModelProviders.of(requireActivity()).get(PurchaseViewModel.class);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(requireActivity());
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        //隐藏toolbar的标题
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        binding.toolbar.inflateMenu(R.menu.menu_main);
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24);
        mDrawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Log.d("PurchaseList", "onCreateView:");
        setHasOptionsMenu(true);
        binding.swipePurchasess.setOnRefreshListener(this);
        initContents();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.purchasesRecyclerview.setLayoutManager(layoutManager);
                    Log.d("PurchaseList", "onCreateView: ListSize " + purchaseList.size());
                    adapter = new PurchaseAdapter(purchaseList);
                    binding.purchasesRecyclerview.setHasFixedSize(true);
                    binding.purchasesRecyclerview.setAdapter(adapter);
                    //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
                    adapter.setOnItemClickListener(new PurchaseAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, All_orders data) {
                            new Handler(new Handler.Callback() {
                                @Override
                                public boolean handleMessage(@NonNull android.os.Message msg) {
                                    Toast.makeText(getActivity(), "我是item", Toast.LENGTH_SHORT).show();
                                    mViewModel.setPurchase(data);
                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_purchaseFragment_to_purchaseDetailFragment);
                                    return false;
                                }
                            }).sendEmptyMessageDelayed(0, 500);
                        }
                    });
                }
            }
        };

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

    private void initContents() {
        Log.d("PurchaseList", "intiContents: ListSize " + purchaseList.size());
        new Thread() {
            @Override
            public void run() {
                purchaseList.clear();
                task_id.clear();
                pur_service pur = new RetrofitUser().get(getContext()).create(pur_service.class);
                SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String mobile = pre.getString("mobile", "");
                Call<ResponseBody> get_purchase = pur.get_orders(mobile,"代购");
                get_purchase.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String str = "";
                        try {
//                            assert response.body() != null;
                            if (response.code() / 100 == 4) {
                                Toast.makeText(getContext(), "4失败", Toast.LENGTH_SHORT).show();
                            } else if (response.code() / 100 == 5) {
                                Toast.makeText(getContext(), "服务器错误", Toast.LENGTH_SHORT).show();
                            } else if (response.code() / 100 == 1 ||
                                    response.code() / 100 == 3) {
                                Toast.makeText(getContext(), "13错误", Toast.LENGTH_SHORT).show();
                            } else {
                                str = response.body().string();
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<All_orders>>() {
                                }.getType();
                                List<All_orders> all = gson.fromJson(str, type);
                                //获取每个用户的照片的字节流
                                for (int i = 0; i < all.size(); i++) {
                                    if (!task_id.contains(all.get(i).getId()) &&
                                            all.get(i).getExecutor() == null
                                    ) {
                                        task_id.add(all.get(i).getId());
                                        String content = all.get(i).getDetail();                       //详情
                                        Point_address address = all.get(i).getDestination();          //地址
                                        String money = String.valueOf(all.get(i).getReward());       //订单酬劳
                                        String time = all.get(i).getCreate_at();                     //订单创建时间
                                        BigInteger getfromUser=all.get(i).getFrom_user();
                                        BigInteger phone = all.get(i).getPhone();               //发布订单的电话号码
                                        String protected_info = all.get(i).getProtected_info();    //隐藏信息
                                        String uuid = all.get(i).getId_photo();                   //头像的uuid
                                        String photos = all.get(i).getPhotos();                     //订单的图片
                                        String category = all.get(i).getCategory();                //订单分类
                                        String name = all.get(i).getName();                       //订单名字
                                        String id_name=all.get(i).getId_name();                //订单发布者的id_name
                                        All_orders purchase1 = new All_orders(getfromUser,phone, address, time, task_id.get(i), content
                                                , Float.parseFloat(money), protected_info, category, photos, uuid, name,id_name);

                                        purchaseList.add(purchase1);
                                        Log.d(" PurchaseList", "message: " + content + "1" +
                                                address + "2" + money + "3" + time);
                                        Message message = new Message();
                                        message.what = 1;
                                        //然后将消息发送出去
                                        handler.sendMessage(message);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(response.code());
                        System.out.println(str);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                        System.out.println("pur_error:" + t.getMessage() + " "+t.getClass().getName());
                    }
                });
            }
        }.start();
    }

    @Override
    public void onRefresh() {
        initContents();
        Log.d("PurchaseList", "onRefresh: ListSize  " + purchaseList.size());
        binding.swipePurchasess.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipePurchasess.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        }, 1000);
    }
}