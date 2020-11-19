package com.example.yilaoapp.ui.bulletin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentShareDetailBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareDetailFragment extends Fragment {

    public ShareDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentShareDetailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_share_detail,container,false);
        BullentinViewModel viewModel = ViewModelProviders.of(requireActivity()).get(BullentinViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.popBackStack();
        });
        viewModel.getSelected().observe(getViewLifecycleOwner(), item -> {
            binding.sharedcontent.setText(item.getContent());
            binding.sharedphoto.setImageResource(item.getImageId());
            binding.sharedtime.setText(item.getTime());
            viewModel.setPhotoId(item.getImageId());
        });
        binding.sharebphoto.setOnClickListener(v-> {
            new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull android.os.Message msg) {
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_shareDetailFragment_to_sharePhotoFragment);
                    return false;
                }
            }).sendEmptyMessageDelayed(0, 300);
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_share_detail, container, false);
    }
}
