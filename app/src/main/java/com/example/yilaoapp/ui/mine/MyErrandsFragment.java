package com.example.yilaoapp.ui.mine;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMyErrandsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyErrandsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public MyErrandsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentMyErrandsBinding binding;
    private List<MyErrands> errandsList = new ArrayList<>();
    private MineViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_errands,container,false);
        viewModel = ViewModelProviders.of(requireActivity()).get(MineViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.swipeMyerrand.setOnRefreshListener(this);
        initContents();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.MyErrandsRecyclerview.setLayoutManager(layoutManager);
        MyErrandsAdapter adapter = new MyErrandsAdapter(errandsList);
        binding.MyErrandsRecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new MyErrandsAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, MyErrands data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        //Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        Log.d("测试","在跳转之前");
                        controller.navigate(R.id.action_myTaskFragment_to_myErrandsDetailFragment);
                        Log.d("测试","在跳转之后");
                        viewModel.setErrands(data);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_lost_found, container, false);
    }
    private void initContents() {
        for (int i = 0; i < 2; i++) {
            MyErrands l1 = new MyErrands("肯德基","2020年12月3日 下午6:00前，买一份肯德基的全家桶",
                    "￥100/一个", "跑腿","发布的任务" ,R.drawable.head3);
            errandsList.add(l1);
            MyErrands l2 = new MyErrands("麦当劳","今天下午5:00前买一份新奥尔良烤鸡腿堡","￥15/一个",
                    "找跑腿","领取的任务" ,R.drawable.head3);
            errandsList.add(l2);
            MyErrands l3 = new MyErrands("生煎包","明天上午8.买一份陈记生煎包",
                    "￥28/一份", "跑腿","发布的任务",R.drawable.head3 );
            errandsList.add(l3);
        }
    }

    @Override
    public void onRefresh() {
        binding.swipeMyerrand.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeMyerrand.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        },3000);
    }
}