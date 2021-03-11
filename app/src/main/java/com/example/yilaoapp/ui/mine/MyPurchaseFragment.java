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
import com.example.yilaoapp.databinding.FragmentMyPurchaseBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPurchaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public MyPurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentMyPurchaseBinding binding;
    private List<MyPurchase> purchaseList = new ArrayList<>();
    private MineViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_purchase,container,false);
        viewModel = ViewModelProviders.of(requireActivity()).get(MineViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());

        binding.swipeMypurchase.setOnRefreshListener(this);
        initContents();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.MyPurchaseRecyclerview.setLayoutManager(layoutManager);
        MyPurchaseAdapter adapter = new MyPurchaseAdapter(purchaseList);
        binding.MyPurchaseRecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new MyPurchaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, MyPurchase data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
//                        Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        Log.d("测试","在跳转之前");
                        controller.navigate(R.id.action_myTaskFragment_to_myPurchaseDetailFragment);
                        Log.d("测试","在跳转之后");
                        viewModel.setPurchase(data);
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
//             photos.add("http://api.yilao.tk:15000/v1.0/users/13060887368/resources/2f5a0fff-5f37-4bb9-8667-2c3beb00dfe8");
        }
        for (int i = 0; i < 2; i++) {
            MyPurchase l1 = new MyPurchase("口红","2020年12月3日 下午6:00前，可以在日本买到免税的MAC口红",
                    "￥90/一个" ,"发布的任务",photos,R.drawable.head1,"13412101248","华师大厦");
            purchaseList.add(l1);
            MyPurchase l2 = new MyPurchase("眼影","希望买一个3CE的眼影盘","￥150/一盘",
                     "领取的任务",photos,R.drawable.head1,"13412101248","华师大厦");
            purchaseList.add(l2);
            MyPurchase l3 = new MyPurchase("面膜","2020年12月1日前，可以在欧洲买到La Prairie蓓丽鱼子精华睡眠面膜",
                    "￥2800/50ml",  "发布的任务",photos,R.drawable.head1,"13412101248","华师大厦");
            purchaseList.add(l3);
        }
    }

    @Override
    public void onRefresh() {
        binding.swipeMypurchase.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeMypurchase.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        },3000);
    }
}