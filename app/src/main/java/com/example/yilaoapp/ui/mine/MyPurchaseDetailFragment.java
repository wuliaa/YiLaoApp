package com.example.yilaoapp.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMyPurchaseDetailBinding;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.kongzue.dialog.v3.TipDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.baseadapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPurchaseDetailFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate {

    private static final int PRC_PHOTO_PREVIEW = 1;
    String status="";

    FragmentMyPurchaseDetailBinding binding;

    public MyPurchaseDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_purchase_detail,container,false);
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
        binding.toolbar.setTitleMarginStart(screenWidth/3);
        AtomicReference<String> label= new AtomicReference<>("");

        viewModel.getPurchase().observe(getViewLifecycleOwner(), item -> {
            binding.MyPurchasescontent.setText("详情："+item.getContent());
            binding.MyPurchasesphoneNumber.setText("联系电话："+item.getPhoneNumber());
            binding.MyPurchasesAddress.setText("地址："+item.getAddress());
            binding.MyPurchasesmoney.setText("金额："+item.getMoney());
            binding.chip1.setText(item.getIsPublish());
            //设置完成按钮
            label.set(item.getIsPublish());
            status=item.getIsPublish();
            if(label.toString().equals("发布的任务"))
            {
                //Toast.makeText(getContext(),label.toString(),Toast.LENGTH_SHORT).show();
                binding.cancelButtonPurchases.setVisibility(View.VISIBLE);
                binding.compeleteButtonPurchases.setVisibility(View.VISIBLE);
            }
            else {
                //Toast.makeText(getContext(),label.toString(),Toast.LENGTH_SHORT).show();
                binding.cancelButtonPurchases.setVisibility(View.GONE);
                binding.compeleteButtonPurchases.setVisibility(View.GONE);
            }
            //添加对图片的代码
            binding.ninePhotoLayout.setDelegate(this);
            binding.ninePhotoLayout.setData(item.getPhotos());
        });

        HorizontalStepView setpview5 =(HorizontalStepView) binding.stepView;
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
        if(status.equals("领取的任务"))
        {
            stepsBeanList.get(1).setState(1);
            stepsBeanList.get(2).setState(0);
            Log.d("MyPurchasedetail", "labelMessage: "+label.toString());
        }

        binding.cancelButtonPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stepsBeanList.get(1).getState()==1)
                {
                    //已经被领取了，就不能点击取消任务
                    Toast.makeText(requireContext(),"任务已被领取，不能取消任务。"
                            ,Toast.LENGTH_LONG).show();;
                }else{
                    stepsBeanList.get(0).setState(-1);
                    TipDialog.show((AppCompatActivity) getActivity(), "取消成功", TipDialog.TYPE.SUCCESS);
                    setStepStytle(setpview5,stepsBeanList);
                }
            }
        });

        binding.compeleteButtonPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stepsBeanList.get(1).getState()==-1)
                {
                    //没被领取了，就不能点击完成任务
                    Toast.makeText(requireContext(),"任务未被领取，不能完成任务。" +
                            "取消任务可以按取消按钮",Toast.LENGTH_LONG).show();;
                }else{
                    stepsBeanList.get(2).setState(1);
                    stepsBeanList.get(3).setState(1);
                    setStepStytle(setpview5,stepsBeanList);
                    TipDialog.show((AppCompatActivity) getActivity(), "完成任务", TipDialog.TYPE.SUCCESS);
                }
            }
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

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (binding.ninePhotoLayout == null) {
            Log.d("ninePhotoLayout", "ninePhotoLayout: IsNull");
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(requireContext());

            // 保存图片的目录，如果传 null，则没有保存图片功能
            photoPreviewIntentBuilder.saveImgDir(downloadDir);

            if (binding.ninePhotoLayout.getItemCount() == 1) {
                // 预览单张图片
                photoPreviewIntentBuilder.previewPhoto(binding.ninePhotoLayout.getCurrentClickItem());
            } else if (binding.ninePhotoLayout.getItemCount() > 1) {
                // 预览多张图片
                photoPreviewIntentBuilder.previewPhotos(binding.ninePhotoLayout.getData())
                        .currentPosition(binding.ninePhotoLayout.getCurrentClickItemPosition()); // 当前预览图片的索引

            }
            startActivity(photoPreviewIntentBuilder.build());
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", PRC_PHOTO_PREVIEW, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PREVIEW) {
            Toast.makeText(requireContext(), "您拒绝了「图片预览」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        photoPreviewWrapper();
    }

    @Override
    public void onClickExpand(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        ninePhotoLayout.setIsExpand(true);
        ninePhotoLayout.flushItems();
    }

}