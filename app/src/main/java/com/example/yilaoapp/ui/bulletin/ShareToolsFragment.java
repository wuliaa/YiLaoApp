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

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentShareToolsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareToolsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public ShareToolsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentShareToolsBinding binding;
    private List<Share> shareList = new ArrayList<>();
    private BullentinViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_share_tools,container,false);
        viewModel = ViewModelProviders.of(requireActivity()).get(BullentinViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.swipeShare.setOnRefreshListener(this);
        initShares();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.shareRecyclerview.setLayoutManager(layoutManager);
        ShareAdapter adapter = new ShareAdapter(shareList);
        binding.shareRecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new ShareAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Share data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        //Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_bullentinFragment_to_shareDetailFragment);
                        viewModel.select(data);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_share_tools, container, false);
    }
    private void initShares() {
        ArrayList<String> photos = new ArrayList<>();
        for (int i = 0; i <9; i++) {
            photos.add("http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered2.png");
//            photos.add("/storage/emulated/0/sina/weibo/storage/photoalbum_save/weibo/img-ebc3581e69b48d8c1bc1365971f12d90.jpg");
//            photos.add("http://api.yilao.tk:5000/v1.0/users/13060887368/resources/2f5a0fff-5f37-4bb9-8667-2c3beb00dfe8");
        }
        for (int i = 0; i < 2; i++) {
            Share s1 = new Share("网球拍","若想和朋友打一次或两次网球却不想花钱买网球拍，" +
                    "这里有闲置的网球拍可共享使用~","下午 6:00", R.drawable.photo1,photos);
            shareList.add(s1);
            Share s2 = new Share("针线","若想使用针线手头上却没有，这里提供免费针线使用",
                    "上午 12:00", R.drawable.photo2,photos);
            shareList.add(s2);
            Share s3 = new Share("雨伞","下雨天忘记带伞了吗，这里提供共享雨伞，用完请记得归还",
                    "下午14:00", R.drawable.photo3,photos);
            shareList.add(s3);
        }
    }

    @Override
    public void onRefresh() {
        binding.swipeShare.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipeShare.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        },3000);
    }
}