package com.example.yilaoapp.ui.mine;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMyErrandsDetailBinding;
import com.github.siyamed.shapeimageview.RoundedImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyErrandsDetailFragment extends Fragment {

    public MyErrandsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentMyErrandsDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_errands_detail, container, false);
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
        wm.getDefaultDisplay().getSize(p);
        int screenWidth = p.x; // 屏幕宽度
        binding.toolbar.setTitleMarginStart(screenWidth / 3);

        viewModel.getErrands().observe(getViewLifecycleOwner(), item -> {
            binding.MyErrandscontent.setText(item.getContent());
            binding.MyErrandsmoney.setText("金额："+item.getMoney());
            binding.MyErrandschip.setText(item.getIsErrands());
            binding.chip1.setText(item.getIsPublish());
            int[] imageid = item.getImageId();
            for (int i = 0; i < imageid.length; i++) {
                RoundedImageView view = new RoundedImageView(container.getContext());
                view.setImageResource(imageid[i]);
                Log.d("图片", String.valueOf(imageid[i]));
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(600, 600);
                if(i==0) lp.setMargins(0,0,0,40);
                else lp.setMargins(0,40,0,40);
                view.setLayoutParams(lp);
                binding.ErrandsImageGallery.addView(view);
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_purchase_detail, container, false);
    }
}