package com.example.yilaoapp.ui.purchase;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.yilaoapp.databinding.FragmentPurchaseListBinding;

import java.util.ArrayList;
import java.util.List;


public class PurchaseListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public PurchaseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentPurchaseListBinding binding;
    private List<Purchase> purchaseList = new ArrayList<>();
    private PurchaseViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_list_,container,false);
        viewModel = ViewModelProviders.of(requireActivity()).get(PurchaseViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.swipePurchase.setOnRefreshListener(this);
        initContents();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.purchaserecyclerview.setLayoutManager(layoutManager);
        PurchaseAdapter adapter = new PurchaseAdapter(purchaseList);
        binding.purchaserecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new PurchaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Purchase data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
//                        Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_purchaseFragment_to_purchaseDetailFragment);
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
        int []imageid={R.drawable.kouhong, R.drawable.yanying, R.drawable.mianmo, R.drawable.kouhong,
        R.drawable.yanying, R.drawable.mianmo};
        for (int i = 0; i < 2; i++) {
            Purchase l1 = new Purchase("口红","2020年12月3日 下午6:00前，可以在日本买到免税的MAC口红",
                    "￥90/一个", "代购",imageid);
            purchaseList.add(l1);
            Purchase l2 = new Purchase("眼影","希望买一个3CE的眼影盘","￥150/一盘",
                    "找代购",imageid);
            purchaseList.add(l2);
            Purchase l3 = new Purchase("面膜","2020年12月1日前，可以在欧洲买到La Prairie蓓丽鱼子精华睡眠面膜",
                    "￥2800/50ml", "代购",imageid);
            purchaseList.add(l3);
        }
    }

    @Override
    public void onRefresh() {
        binding.swipePurchase.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipePurchase.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        },3000);
    }
}