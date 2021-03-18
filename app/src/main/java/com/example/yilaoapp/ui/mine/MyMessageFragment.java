package com.example.yilaoapp.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
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

    List<chat_user> all_chat = new LinkedList<>();
    Handler handler;

    public MyMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentMyMessageBinding binding;
    private List<Message> messageList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
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
        init();
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull android.os.Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.messageRecyclerview.setLayoutManager(layoutManager);
                    MessageAdapter adapter = new MessageAdapter(messageList);
                    binding.messageRecyclerview.setAdapter(adapter);
                    adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
                        @Override
                        public void OnItemClick(View view, Message data) {
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
                }
            }
        };
        return binding.getRoot();
    }

    //获取所有聊天用户
    public void init() {
        new Thread() {
            @Override
            public void run() {
                messageList.clear();
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
                                    Message mm;
                                    if (!all_chat.get(i).getFrom_user().equals("") &&
                                            !all_chat.get(i).getTo_user().equals("")) {
                                        if (all_chat.get(i).getType().equals("TEXT")) {
                                            if (mobile.equals(all_chat.get(i).getFrom_user())) {
                                                mm = new Message(all_chat.get(i).getId_name(), all_chat.get(i).getLast_content(), all_chat.get(i).getLast_send_at(),
                                                        all_chat.get(i).getId_photo(), new BigInteger(all_chat.get(i).getTo_user()),"TEXT");
                                            } else {
                                                mm = new Message(all_chat.get(i).getId_name(), all_chat.get(i).getLast_content(), all_chat.get(i).getLast_send_at(),
                                                        all_chat.get(i).getId_photo(), new BigInteger(all_chat.get(i).getFrom_user()),"TEXT");
                                            }
                                            messageList.add(mm);
                                        }else if(all_chat.get(i).getType().equals("IMAGE")){
                                            if (mobile.equals(all_chat.get(i).getFrom_user())) {
                                                mm = new Message(all_chat.get(i).getId_name(), all_chat.get(i).getLast_content(), all_chat.get(i).getLast_send_at(),
                                                        all_chat.get(i).getId_photo(), new BigInteger(all_chat.get(i).getTo_user()),"IMAGE");
                                            } else {
                                                mm = new Message(all_chat.get(i).getId_name(), all_chat.get(i).getLast_content(), all_chat.get(i).getLast_send_at(),
                                                        all_chat.get(i).getId_photo(), new BigInteger(all_chat.get(i).getFrom_user()),"IMAGE");
                                            }
                                            messageList.add(mm);
                                        }
                                    }
                                }
                                android.os.Message message = new android.os.Message();
                                message.what = 1;
                                //然后将消息发送出去
                                handler.sendMessage(message);
                                response.body().close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }.start();
    }

    @SuppressLint("CheckResult")
    private void requestPermisson(View view, Message message) {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                )//1
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {

                            Intent intent = new Intent(requireActivity(), ChatActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("mobile", message.getMobile().toString());
                            bundle.putString("uuid", message.getUuid());
                            bundle.putString("id_name", message.getNick());
                            intent.putExtra("bundle", bundle);
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
        init();
        binding.swipeMymessage.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeMymessage.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        }, 1000);
    }
}