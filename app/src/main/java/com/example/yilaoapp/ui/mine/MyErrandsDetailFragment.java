package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMyErrandsDetailBinding;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

    @SuppressLint({"SetTextI18n", "ShowToast"})
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
        assert wm != null;
        wm.getDefaultDisplay().getSize(p);
        int screenWidth = p.x; // 屏幕宽度
        binding.toolbar.setTitleMarginStart(screenWidth / 3);
        AtomicReference<String> label= new AtomicReference<>("");

        viewModel.getErrands().observe(getViewLifecycleOwner(), item -> {
            binding.MyErrandscontent.setText(item.getContent());
            binding.MyErrandsmoney.setText("金额："+item.getMoney());
            binding.MyErrandschip.setText(item.getIsErrands());
            binding.chip1.setText(item.getIsPublish());
            //设置完成按钮
            label.set(item.getIsPublish());
            Log.d("MyErrandDetail", "labelMessage1: "+label.toString());
            if(label.toString().equals("发布的任务"))
            {
                //Toast.makeText(getContext(),label.toString(),Toast.LENGTH_SHORT).show();
                binding.compeleteButtonErrands.setVisibility(View.VISIBLE);
            }
            else {
                //Toast.makeText(getContext(),label.toString(),Toast.LENGTH_SHORT).show();
                binding.compeleteButtonErrands.setVisibility(View.GONE);
            }
            int[] imageid = item.getImageId();
            for (int i = 0; i < imageid.length; i++) {
                RoundedImageView view = new RoundedImageView(container.getContext());
                view.setImageResource(imageid[i]);
                Log.d("图片", String.valueOf(imageid[i]));
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(600, 600);
                if(i==0) lp.setMargins(0,0,0,40);
                else lp.setMargins(0,40,0,40);
                view.setLayoutParams(lp);
                int res = imageid[i];
                view.setOnClickListener(v-> {
                    new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(@NonNull android.os.Message msg) {
                            NavController controller = Navigation.findNavController(v);
                            viewModel.setMinephotoId(res);
                            controller.navigate(R.id.action_myErrandsDetailFragment_to_minePhotoFragment);
                            return false;
                        }
                    }).sendEmptyMessageDelayed(0, 300);
                });
                binding.ErrandsImageGallery.addView(view);
            }

            HorizontalStepView setpview5 =(HorizontalStepView) binding.stepViewErrand;
            List<StepBean> stepsBeanList = new ArrayList<>();
            StepBean stepBean0 = new StepBean("发布",1);//1是完成，0是正在进行时，-1是还没有进行到
            StepBean stepBean1 = new StepBean("领取",-1);
            StepBean stepBean2 = new StepBean("进行中",-1);
            StepBean stepBean3 = new StepBean("完成",-1);
            stepsBeanList.add(stepBean0);
            stepsBeanList.add(stepBean1);
            stepsBeanList.add(stepBean2);
            stepsBeanList.add(stepBean3);
            setStepStytle(setpview5,stepsBeanList);
            if(label.toString().equals("领取的任务"))
            {
                stepsBeanList.get(1).setState(1);
                stepsBeanList.get(2).setState(0);
                Log.d("MyErrandDetail", "labelMessage: "+label.toString());
            }

            binding.compeleteButtonErrands.setOnClickListener(view -> {
                stepsBeanList.get(2).setState(1);
                stepsBeanList.get(3).setState(1);
                Log.d("MyErrandDetail", "onClick: click");
                Log.d("MyErrandDetail", "StepBean2: "+stepsBeanList.get(2).getState());
                Log.d("MyErrandDetail", "StepBean3: "+stepsBeanList.get(3).getState());

            });
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_purchase_detail, container, false);
    }

    public void setStepStytle(HorizontalStepView stepView5,List<StepBean> stepBeanList)
    {
        stepView5
                .setStepViewTexts(stepBeanList)//总步骤
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(
                        ContextCompat.getColor(requireActivity(), android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(
                        ContextCompat.getColor(requireActivity(), R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(
                        ContextCompat.getColor(requireActivity(), android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(
                        ContextCompat.getColor(requireActivity(), R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(
                        ContextCompat.getDrawable(requireActivity(), R.drawable.attention));//设置StepsViewIndicator AttentionIcon
    }

 
}