package com.example.yilaoapp.ui.bulletin;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.bean.Point_address;
import com.example.yilaoapp.databinding.FragmentTeamStudyBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.pur_service;
import com.example.yilaoapp.ui.purchase.PurchaseAdapter;
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
public class TeamStudyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FragmentTeamStudyBinding binding;
    private List<All_orders> teamList;
    private BullentinViewModel viewModel;
    List<Integer> task_id;   //订单id
    TeamAdapter adapter;
    Handler handler;

    public TeamStudyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamList = new ArrayList<>();
        task_id = new LinkedList<>();
    }

    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_team_study, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(BullentinViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());

        binding.swipeTeam.setOnRefreshListener(this);
        initTeams();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.teamRecyclerview.setLayoutManager(layoutManager);
                    adapter = new TeamAdapter(teamList);
                    binding.teamRecyclerview.setAdapter(adapter);

                    //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
                    adapter.setOnItemClickListener(new TeamAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, All_orders data) {
                            new Handler(new Handler.Callback() {
                                @Override
                                public boolean handleMessage(@NonNull android.os.Message msg) {
                                    NavController controller = Navigation.findNavController(view);
                                    controller.navigate(R.id.action_bullentinFragment_to_teamDetailFragment);
                                    viewModel.setTeam(data);
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


    private void initTeams() {
        new Thread() {
            @Override
            public void run() {
                teamList.clear();
                task_id.clear();
                pur_service pur = new RetrofitUser().get(getContext()).create(pur_service.class);
                Call<ResponseBody> get_team = pur.get_orders("代购");
                get_team.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String str = "";
                        try {
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
                                    BigInteger phone = all.get(i).getFrom_user();               //发布订单的电话号码
                                    String protected_info = all.get(i).getProtected_info();    //隐藏信息
                                    String uuid = all.get(i).getId_photo();                   //头像的uuid
                                    String photos = all.get(i).getPhotos();                     //订单的图片
                                    String category = all.get(i).getCategory();                //订单分类
                                    String name = all.get(i).getName();                       //订单名字
                                    All_orders purchase1 = new All_orders(phone, address, time, task_id.get(i), content
                                            , Float.parseFloat(money), protected_info, category, photos, uuid, name);
                                    teamList.add(purchase1);
                                    Message message = new Message();
                                    message.what = 1;
                                    //然后将消息发送出去
                                    handler.sendMessage(message);
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
        }.start();
    }

    @Override
    public void onRefresh() {
        initTeams();
        binding.swipeTeam.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeTeam.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        }, 1000);
    }
}