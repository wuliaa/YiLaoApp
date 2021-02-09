package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMyBulletinDetailBinding;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBulletinDetailFragment extends Fragment {

    public MyBulletinDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentMyBulletinDetailBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_bulletin_detail, container, false);
        MineViewModel viewModel = ViewModelProviders.of(requireActivity()).get(MineViewModel.class);
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
        Point p = new Point();//获取窗口管理器
        WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        wm.getDefaultDisplay().getSize(p);
        int screenWidth = p.x; // 屏幕宽度
        binding.toolbar.setTitleMarginStart(screenWidth / 3);

        viewModel.getBulletin().observe(getViewLifecycleOwner(), item -> {
            binding.BulletinContent.setText(item.getContent());
            binding.Bulletinaddress.setText("地址："+item.getAddress());
            binding.Bulletintime.setText("时间："+item.getTime());
            binding.chip1.setText(item.getWhatBulletin());
            int[] imageid = item.getImageId();
            for (int i = 0; i < imageid.length; i++) {
                RoundedImageView view = new RoundedImageView(container.getContext());
                view.setImageResource(imageid[i]);
                Log.d("图片", String.valueOf(imageid[i]));
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(600, 600);
                if(i==0) lp.setMargins(0,0,0,40);
                else lp.setMargins(0,40,0,40);
                view.setLayoutParams(lp);
                binding.BulletinImageGallery.addView(view);
            }
        });

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_purchase_detail, container, false);
    }
}