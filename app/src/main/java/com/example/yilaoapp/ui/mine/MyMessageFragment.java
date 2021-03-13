package com.example.yilaoapp.ui.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.bean.Message;
import com.example.yilaoapp.bean.chat_task;
import com.example.yilaoapp.bean.chat_user;
import com.example.yilaoapp.chat.activity.ChatActivity;
import com.example.yilaoapp.chat.util.LogUtil;
import com.example.yilaoapp.chat.widget.SetPermissionDialog;
import com.example.yilaoapp.databinding.FragmentMyMessageBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.chat_service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yilaoapp.MyApplication.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    static boolean chat_flag = true;
    List<chat_user> all_chat = new LinkedList<>();

    public MyMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentMyMessageBinding binding;
    private List<Message> messageList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_message, container, false);
        //binding.setData(MineViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });
        binding.swipeMymessage.setOnRefreshListener(this);
        initMessages();
        //init();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.messageRecyclerview.setLayoutManager(layoutManager);
        MessageAdapter adapter = new MessageAdapter(messageList);
        binding.messageRecyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Message data) {
                chat_task ch = new chat_task("您的任务我已领取，订单信息如下:" + data.getContent(), data.getMobile());
                chat_service send = new RetrofitUser().get(getContext()).create(chat_service.class);
                SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String mobile = pre.getString("mobile", "");
                String token = pre.getString("token", "");
                Call<ResponseBody> sen_mes = send.send_message(mobile, token, "df3b72a07a0a4fa1854a48b543690eab", ch);
                sen_mes.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(getContext(), "信息已success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                requestPermisson(view, data);
                            }
                        }, 100);
                        LogUtil.d(new String(Character.toChars(0x1F60E)));
                        //viewModel.select(d1ata);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_my_message, container, false);
    }

    private void initMessages() {
       /* for (int i = 0; i < 2; i++) {
            Message m1 = new Message("Lisa","在吗","下午 6:00", R.drawable.head1);
            messageList.add(m1);
            Message m2 = new Message("Jack","你好","上午 12:00", R.drawable.head2);
            messageList.add(m2);
            Message m3 = new Message("Rose","明天和我一起去天文博物馆参观吗",
                    "下午14:00", R.drawable.head3);
            messageList.add(m3);
        }*/
        BigInteger m1 = new BigInteger("13412101248");
        BigInteger m2 = new BigInteger("13060887368");
        Message m = new Message("jgq", "hello", "16:30", "b8caed0f-48ce-4aa2-b98d-1500c6e42998", m2);
        Message m3 = new Message("jgq1", "hello", "16:30", "b8caed0f-48ce-4aa2-b98d-1500c6e42998", m1);
        messageList.add(m);
        messageList.add(m3);
    }

    //获取所有聊天用户
   /* public void init() {
        SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String mobile = pre.getString("mobile", "");
        String token = pre.getString("token", "");
        chat_service chat_us = new RetrofitUser().get(getContext()).create(chat_service.class);
        Call<ResponseBody> ch_back = chat_us.get_chatuser(mobile, token, "df3b72a07a0a4fa1854a48b543690eab");
        ch_back.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() / 100 == 4) {
                    Toast.makeText(getContext(), "4失败", Toast.LENGTH_SHORT).show();
                } else if (response.code() / 100 == 5) {
                    Toast.makeText(getContext(), "服务器错误", Toast.LENGTH_SHORT).show();
                } else if (response.code() / 100 == 1 ||
                        response.code() / 100 == 3) {
                    Toast.makeText(getContext(), "13错误", Toast.LENGTH_SHORT).show();
                } else {
                    String str = "";
                    try {
                        str = response.body().string();
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<chat_user>>() {
                        }.getType();
                        all_chat = gson.fromJson(str, type);
                        for (int i = 0; i < all_chat.size(); i++) {
                            Message mm = new Message(all_chat.get(i).getId_name(), all_chat.get(i).getLast_content(), all_chat.get(i).getLast_send_at(),
                                    all_chat.get(i).getId_photo(), new BigInteger(all_chat.get(i).getMobile()));
                            messageList.add(mm);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


     /*
                         Gson gson = new Gson();
                    Type type = new TypeToken<List<All_orders>>() {
                  }.getType();


    }*/

    private void requestPermisson(View view, Message message) {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {

                            Intent intent = new Intent(requireActivity(), ChatActivity.class);
                            //intent.putExtra("mobile", message.getMobile().toString());
                            //intent.putExtra("uuid", message.getUuid());
                            startActivity(intent);
                        } else {
                            SetPermissionDialog mSetPermissionDialog = new SetPermissionDialog(getContext());
                            mSetPermissionDialog.show();
                            mSetPermissionDialog.setConfirmCancelListener(new SetPermissionDialog.OnConfirmCancelClickListener() {
                                @Override
                                public void onLeftClick() {
                                    //requireActivity().finish();
                                }

                                @Override
                                public void onRightClick() {
                                    //requireActivity().finish();
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        binding.swipeMymessage.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeMymessage.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        }, 3000);
    }
}