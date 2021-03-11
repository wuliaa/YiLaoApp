package com.example.yilaoapp.ui.bulletin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
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
import com.example.yilaoapp.databinding.FragmentLostFoundBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LostFoundFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public LostFoundFragment() {}
    FragmentLostFoundBinding binding;
    private List<Lost> lostList = new ArrayList<>();
    private BullentinViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLosts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_lost_found,container,false);
        viewModel = ViewModelProviders.of(requireActivity()).get(BullentinViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());

        binding.swipeLost.setOnRefreshListener(this);
//        initLosts();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.lostrecyclerview.setLayoutManager(layoutManager);
        LostAdapter adapter = new LostAdapter(lostList);
        binding.lostrecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new LostAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Lost data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
//                        Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_bullentinFragment_to_lostDetailFragment);
                        viewModel.setLost(data);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_lost_found, container, false);
    }
    private void initLosts() {
        ArrayList<String> photos = new ArrayList<>();
        for (int i = 0; i <9; i++) {
            photos.add("http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered2.png");
//            photos.add("/storage/emulated/0/sina/weibo/storage/photoalbum_save/weibo/img-ebc3581e69b48d8c1bc1365971f12d90.jpg");
//            photos.add("http://api.yilao.tk:15000/v1.0/users/13060887368/resources/2f5a0fff-5f37-4bb9-8667-2c3beb00dfe8");
        }
        for (int i = 0; i < 2; i++) {
            Lost l1 = new Lost("校卡","在北座402捡到一张来自计算机学院18级的一卡通",
                    "下午 6:00", "北座402",R.drawable.lost1,photos);
            lostList.add(l1);
            Lost l2 = new Lost("钥匙","在陶园捡到一个钥匙",
                    "上午 12:00","陶园", R.drawable.lost2,photos);
            lostList.add(l2);
            Lost l3 = new Lost("高数课本","一课北403谁落下了高数辅导书吗",
                    "下午14:00", "一课北座",R.drawable.lost3,photos);
            lostList.add(l3);
        }
    }

    @Override
    public void onRefresh() {
        binding.swipeLost.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeLost.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        },3000);
    }
}