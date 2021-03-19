package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.bean.Point_address;
import com.example.yilaoapp.databinding.FragmentMyBulletinBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.example.yilaoapp.service.bur_service;
import com.example.yilaoapp.ui.bulletin.TeamAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBulletinFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FragmentMyBulletinBinding binding;
    private List<All_orders> bulletinList ;
    private MineViewModel viewModel;
    List<Integer> task_id;   //订单id
    MyBulletinAdapter adapter;
    Handler handler;

    public MyBulletinFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bulletinList = new ArrayList<>();
        task_id = new LinkedList<>();
    }

    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_bulletin, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(MineViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());

        binding.swipeMybulletin.setOnRefreshListener(this);
        initContents();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.MyBulletinRecyclerview.setLayoutManager(layoutManager);
                    adapter = new MyBulletinAdapter(bulletinList);
                    binding.MyBulletinRecyclerview.setAdapter(adapter);

                    //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
                    adapter.setOnItemClickListener(new MyBulletinAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, All_orders data) {
                            new Handler(new Handler.Callback() {
                                @Override
                                public boolean handleMessage(@NonNull android.os.Message msg) {
                                    //Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                                    NavController controller = Navigation.findNavController(view);
                                    Log.d("测试", "在跳转之前");
                                    controller.navigate(R.id.action_myTaskFragment_to_myBulletinDetailFragment);
                                    Log.d("测试", "在跳转之后");
                                    viewModel.setBulletin(data);
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

    private void initContents() {
        new Thread() {
            @Override
            public void run() {
                bulletinList.clear();
                task_id.clear();
                UserService bur = new RetrofitUser().get(getContext()).create(UserService.class);
                SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String mobile=pre.getString("mobile","");
                String token=pre.getString("token","");
                Call<ResponseBody> get_team = bur.get_myorder(mobile,token,"df3b72a07a0a4fa1854a48b543690eab","公告");
                get_team.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        String str = "";
                        try {
//                            assert response.body() != null;
                            if (response.body() == null)
                                Toast.makeText(getContext(), "网络延迟", Toast.LENGTH_SHORT).show();
                            else {
                                str = response.body().string();
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<All_orders>>() {
                                }.getType();
                                List<All_orders> all = gson.fromJson(str, type);
                                //获取每个用户的照片的字节流
                                for (int i = 0; i < all.size(); i++) {
                                    if (!task_id.contains(all.get(i).getId())
                                    ) {
                                        task_id.add(all.get(i).getId());
                                        String content = all.get(i).getDetail();                       //详情
                                        Point_address address = all.get(i).getDestination();          //地址
                                        String money = String.valueOf(all.get(i).getReward());       //订单酬劳
                                        String time = all.get(i).getCreate_at();                     //订单创建时间
                                        BigInteger getfromUser = all.get(i).getFrom_user();
                                        BigInteger phone = all.get(i).getPhone();               //发布订单的电话号码
                                        String protected_info = all.get(i).getProtected_info();    //隐藏信息
                                        String uuid = all.get(i).getId_photo();                   //头像的uuid
                                        String photos = all.get(i).getPhotos();                     //订单的图片
                                        String category = all.get(i).getCategory();                //订单分类
                                        String name = all.get(i).getName();                       //订单名字
                                        String nickname=all.get(i).getId_name();             //订单人的昵称
                                        String close_state=all.get(i).getClose_state();
                                        String receive_at=all.get(i).getReceive_at();
                                        BigInteger executor=all.get(i).getExecutor();       //接单人的电话号码
                                        String id_photo1=all.get(i).getId_photo1();        //接单人的头像
                                        String id_name1=all.get(i).getId_name1();         //接单人的昵称
                                        All_orders Mybulletin = new All_orders(getfromUser,phone, address, time,
                                                task_id.get(i), content, Float.parseFloat(money),
                                                close_state,receive_at,executor,protected_info,
                                                category, photos, uuid, name,nickname,id_photo1,id_name1);
                                        bulletinList.add(Mybulletin);
                                        Message message = new Message();
                                        message.what = 1;
                                        //然后将消息发送出去
                                        handler.sendMessage(message);
                                        Log.d("Close_stateBullentin", "onCreateView: "+all.get(i).getDetail()+"  "
                                                +all.get(i).getClose_state());
                                    }
                                }
                                response.body().close();
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
        initContents();
        binding.swipeMybulletin.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeMybulletin.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        }, 1000);
    }
}