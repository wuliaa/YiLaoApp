package com.example.yilaoapp.ui.bulletin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentShareToolsBinding;
import com.example.yilaoapp.ui.mine.Message;
import com.example.yilaoapp.ui.mine.MessageAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShareToolsFragment extends Fragment {

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
        for (int i = 0; i < 2; i++) {
            Share s1 = new Share("网球拍","若想和朋友打一次或两次网球却不想花钱买网球拍，" +
                    "这里有闲置的网球拍可共享使用~","下午 6:00", R.drawable.photo1);
            shareList.add(s1);
            Share s2 = new Share("针线","若想使用针线手头上却没有，这里提供免费针线使用","上午 12:00", R.drawable.photo2);
            shareList.add(s2);
            Share s3 = new Share("雨伞","下雨天忘记带伞了吗，这里提供共享雨伞，用完请记得归还",
                    "下午14:00", R.drawable.photo3);
            shareList.add(s3);
        }
    }
}