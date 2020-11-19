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

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMyPurchaseBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPurchaseFragment extends Fragment {

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
        int []imageid={R.drawable.kouhong, R.drawable.yanying, R.drawable.mianmo, R.drawable.kouhong,
                R.drawable.yanying, R.drawable.mianmo};
        for (int i = 0; i < 2; i++) {
            MyPurchase l1 = new MyPurchase("口红","2020年12月3日 下午6:00前，可以在日本买到免税的MAC口红",
                    "￥90/一个", "代购","发布的任务",imageid);
            purchaseList.add(l1);
            MyPurchase l2 = new MyPurchase("眼影","希望买一个3CE的眼影盘","￥150/一盘",
                    "找代购","领取的任务",imageid);
            purchaseList.add(l2);
            MyPurchase l3 = new MyPurchase("面膜","2020年12月1日前，可以在欧洲买到La Prairie蓓丽鱼子精华睡眠面膜",
                    "￥2800/50ml", "代购","发布的任务",imageid);
            purchaseList.add(l3);
        }
    }
}