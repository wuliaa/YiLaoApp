package com.example.yilaoapp.ui.mine;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMyBulletinBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 */
public class MyBulletinFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{



    public MyBulletinFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentMyBulletinBinding binding;
    private List<MyBulletin> bulletinList = new ArrayList<>();
    private MineViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_bulletin,container,false);
        viewModel = ViewModelProviders.of(requireActivity()).get(MineViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());

        binding.swipeMybulletin.setOnRefreshListener(this);
        initContents();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.MyBulletinRecyclerview.setLayoutManager(layoutManager);
        MyBulletinAdapter adapter = new MyBulletinAdapter(bulletinList);
        binding.MyBulletinRecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new MyBulletinAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, MyBulletin data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        //Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        Log.d("测试","在跳转之前");
                        controller.navigate(R.id.action_myTaskFragment_to_myBulletinDetailFragment);
                        Log.d("测试","在跳转之后");
                        viewModel.setBulletin(data);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_lost_found, container, false);
    }
    private void initContents() {
        ArrayList<String> photos = new ArrayList<>();
        for (int i = 0; i <9; i++) {
            photos.add("http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered2.png");
//            photos.add("/storage/emulated/0/sina/weibo/storage/photoalbum_save/weibo/img-ebc3581e69b48d8c1bc1365971f12d90.jpg");
//            photos.add("http://api.yilao.tk:15000/v1.0/users/13060887368/resources/2f5a0fff-5f37-4bb9-8667-2c3beb00dfe8");
        }
        for (int i = 0; i < 2; i++) {
            MyBulletin l1 = new MyBulletin("校卡","在北座402捡到一张来自计算机学院18级的一卡通",
                    "今天下午6:00", "北座402","失物招领",photos,R.drawable.head2);
            bulletinList.add(l1);
            MyBulletin l2 = new MyBulletin("网球拍","若想和朋友打一次或两次网球却不想花钱买网球拍，" +
                    "这里有闲置的网球拍可共享使用~","可以商讨",
                    "可以商讨","共享工具",photos,R.drawable.head2);
            bulletinList.add(l2);
            MyBulletin l3 = new MyBulletin("高数小组学习","我这里有比较好的学习资料，" +
                    "为了考到更好的分数，高数一起学习吧",
                    "可以商讨", "图书馆","组对学习",photos,R.drawable.head2);
            bulletinList.add(l3);
        }
    }

    @Override
    public void onRefresh() {
        binding.swipeMybulletin.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeMybulletin.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        },3000);
    }
}