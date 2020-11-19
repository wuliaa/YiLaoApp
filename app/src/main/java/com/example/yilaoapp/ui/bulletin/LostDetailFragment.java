package com.example.yilaoapp.ui.bulletin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentLostDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class LostDetailFragment extends Fragment {

    public LostDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentLostDetailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_lost_detail,container,false);
        BullentinViewModel viewModel = ViewModelProviders.of(requireActivity()).get(BullentinViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });
        viewModel.getLost().observe(getViewLifecycleOwner(), item -> {
            binding.lostdcontent.setText(item.getContent());
            binding.lostdphoto.setImageResource(item.getImageId());
            binding.lostdtime.setText(item.getTime());
            binding.lostdchip.setText(item.getAddress());
            viewModel.setLostPhotoId(item.getImageId());
        });
        binding.lostbphoto.setOnClickListener(v-> {
            new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull android.os.Message msg) {
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_lostDetailFragment_to_lostPhotoFragment);
                    return false;
                }
            }).sendEmptyMessageDelayed(0, 300);
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_lost_detail, container, false);
    }
}