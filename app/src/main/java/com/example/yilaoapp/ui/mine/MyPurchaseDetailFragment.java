package com.example.yilaoapp.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yilaoapp.MyApplication;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.chat.activity.ChatActivity;
import com.example.yilaoapp.chat.util.LogUtil;
import com.example.yilaoapp.chat.widget.SetPermissionDialog;
import com.example.yilaoapp.databinding.FragmentMyPurchaseDetailBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicReference;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.baseadapter.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyPurchaseDetailFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate {

    private static final int PRC_PHOTO_PREVIEW = 1;
    String uuid ;
    ArrayList<String> photosUrl;
    String nickName ;
    String label;

    FragmentMyPurchaseDetailBinding binding;

    public MyPurchaseDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uuid="";
        photosUrl=new ArrayList<>();
        nickName="";
        label="";
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

        viewModel.getPurchase().observe(getViewLifecycleOwner(), item -> {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                    .append(item.getFrom_user())
                    .append("/resources/")
                    .append(item.getId_photo());
            String headurl = stringBuilder.toString();
            Glide.with(MyApplication.getContext())
                    .load(headurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.head1)
                    .error(R.drawable.head2)
                    .into(binding.myPurchaseHead);
            //获得昵称
            binding.Mypurchasedname.setText(item.getId_name());
            binding.MyPurchasescontent.setText("详情："+item.getDetail());
            binding.MyPurchasesphoneNumber.setText("联系电话："+item.getPhone());
            binding.MyPurchasesAddress.setText("联系地址："+item.getDestination().getName());
            binding.MyPurchasesmoney.setText("劳务费："+item.getReward()+"元");
            binding.chip1.setText(item.getCategory());

            //设置完成按钮
            SharedPreferences pre2 = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            String mobile2 = pre2.getString("mobile", "");
            if(mobile2.equals(String.valueOf(item.getFrom_user())))
                label="发布的任务";
            else
                label="领取的任务";


            //设置底下的button的出现还是消失
            //完成任务的button只能在“发布任务”中可以看见
            if (label.equals("发布的任务")) {
                binding.compeleteButtonPurchases.setVisibility(View.VISIBLE);
            } else {
                binding.compeleteButtonPurchases.setVisibility(View.GONE);
            }
            //如果任务是cancel或者finish，那么button都不可见

            if (item.getClose_state() != null) {
                if (item.getClose_state().equals("cancel") ||
                        item.getClose_state().equals("finish") ||
                        item.getClose_state().equals("canceling")) {
                    binding.compeleteButtonPurchases.setVisibility(View.GONE);
                    binding.acceptButtonPurchases.setVisibility(View.GONE);
                }
                if (item.getClose_state().equals("canceling") &&
                        label.equals("领取的任务")) {
                    binding.acceptButtonPurchases.setVisibility(View.VISIBLE);
                    binding.refuseButtonPurchase.setVisibility(View.VISIBLE);
                }
            }
            if(item.getReceive_at()==null){
                binding.ChatMyPurchase.setVisibility(View.GONE);
            }



            //添加对图片的代码
            binding.ninePhotoLayout.setDelegate(this);
            StringTokenizer st = new StringTokenizer(item.getPhotos(), ",");
            while (st.hasMoreTokens()) {
                uuid = st.nextToken();
                StringBuilder stringBuilder1=new StringBuilder();
                stringBuilder1.append("http://api.yilao.tk:15000/v1.0/users/")
                        .append(item.getPhone())
                        .append("/resources/")
                        .append(uuid);
                String url = stringBuilder1.toString();
                photosUrl.add(url);
            }
            binding.ninePhotoLayout.setData(photosUrl);

            HorizontalStepView setpview5 =(HorizontalStepView) binding.stepView;
            List<StepBean> stepsBeanList = new ArrayList<>();
            StepBean stepBean0=new StepBean("取消",-1);
            StepBean stepBean1 = new StepBean("发布", 1);//1是完成，0是正在进行时，-1是还没有进行到
            StepBean stepBean2 = new StepBean("领取", -1);
            StepBean stepBean3 = new StepBean("进行中", -1);
            StepBean stepBean4 = new StepBean("完成", -1);
            stepsBeanList.add(stepBean0);
            stepsBeanList.add(stepBean1);
            stepsBeanList.add(stepBean2);
            stepsBeanList.add(stepBean3);
            stepsBeanList.add(stepBean4);

            //如果为领取的任务，状态直接到进行中
            if (label.equals("领取的任务")) {
                stepsBeanList.get(2).setState(1);
                stepsBeanList.get(3).setState(0);
            } else if (label.equals("发布的任务")) {
                if (item.getReceive_at() != null) {
                    stepsBeanList.get(2).setState(1);
                    stepsBeanList.get(3).setState(0);
                }
            }

            //如果是cancel或finish的话，一开始初始化就要设置一下状态
            if (item.getClose_state() != null) {
                if (item.getClose_state().equals("cancel")) {
                    stepsBeanList.get(0).setState(1);
                    for (int i = 0; i < 4; i++) {
                        stepsBeanList.get(i + 1).setState(-1);
                    }
                } else if (item.getClose_state().equals("finish")) {
                    for (int i = 0; i < 4; i++) {
                        stepsBeanList.get(i + 1).setState(1);
                    }
                } else if (item.getClose_state().equals("canceling")) {
                    stepsBeanList.get(0).setState(0);
                    for (int i = 0; i < 4; i++) {
                        stepsBeanList.get(i + 1).setState(-1);
                    }
                }
            }

            setStepStytle(setpview5, stepsBeanList);

            binding.cancelButtonPurchases.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserService user= new RetrofitUser().get(getContext()).create(UserService.class);
                    SharedPreferences pre=getContext().getSharedPreferences("login",Context.MODE_PRIVATE);
                    String mobile=pre.getString("mobile","");
                    String token=pre.getString("token","");
                    String orderid=String.valueOf(item.getId());
                    if(label.equals("发布的任务")){
                        if (stepsBeanList.get(2).getState() == 1 || item.getExecutor() != null) {
                            //已经被领取了，就不能点击取消任务
                            Toast.makeText(requireContext(), "任务已被领取，取消接单需要与接单人进行沟通。"
                                    , Toast.LENGTH_LONG).show();
                            stepsBeanList.get(0).setState(0);
                        } else {
                            stepsBeanList.get(0).setState(1);
                            TipDialog.show((AppCompatActivity) getActivity(), "取消成功", TipDialog.TYPE.SUCCESS);
                        }
                        //cancel 请求
                        for (int i = 0; i < 4; i++) {
                            stepsBeanList.get(i + 1).setState(-1);
                        }
                        setStepStytle(setpview5, stepsBeanList);
                        Call<ResponseBody> cancelTask = user.Put_Fin_Cancel_Task(mobile, orderid,
                                token, "df3b72a07a0a4fa1854a48b543690eab", "cancel");
                        cancelTask.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                if (response.body() != null) {

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                            }
                        });
                    }else if (label.equals("领取的任务")){
                        Call<ResponseBody> cancelTask=user.Get_Acc_Cancel_Order(mobile,orderid,token,
                                "df3b72a07a0a4fa1854a48b543690eab","false");
                        cancelTask.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                stepsBeanList.get(0).setState(1);
                                for(int i=0;i<4;i++)
                                {
                                    stepsBeanList.get(i+1).setState(-1);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        TipDialog.show((AppCompatActivity) getActivity(), "接单取消成功", TipDialog.TYPE.SUCCESS);
                    }
                }
            });

            binding.compeleteButtonPurchases.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stepsBeanList.get(2).getState()==-1)
                    {
                        //没被领取了，就不能点击完成任务
                        Toast.makeText(requireContext(),"任务未被领取，不能完成任务。" +
                                "取消任务可以按取消按钮",Toast.LENGTH_LONG).show();;
                    }else{
                        stepsBeanList.get(0).setState(-1);
                        for (int i = 0; i < 4; i++) {
                            stepsBeanList.get(i + 1).setState(1);
                        }
                        setStepStytle(setpview5, stepsBeanList);
                        UserService user = new RetrofitUser().get(getContext()).create(UserService.class);
                        SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                        String mobile = pre.getString("mobile", "");
                        String token = pre.getString("token", "");
                        String order_id = String.valueOf(item.getId());
                        Call<ResponseBody> finish = user.Put_Fin_Cancel_Task(mobile, order_id, token,
                                "df3b72a07a0a4fa1854a48b543690eab", "finish");
                        finish.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        TipDialog.show((AppCompatActivity) getActivity(), "完成任务", TipDialog.TYPE.SUCCESS);
                    }
                }
            });

            binding.acceptButtonPurchases.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(label.equals("领取的任务")){
                        UserService user = new RetrofitUser().get(getContext()).create(UserService.class);
                        SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                        String mobile = pre.getString("mobile", "");
                        String token = pre.getString("token", "");
                        String orderid = String.valueOf(item.getId());
                        Call<ResponseBody> accept=user.Put_Fin_Cancel_Task(mobile,orderid,token
                                ,"df3b72a07a0a4fa1854a48b543690eab","close");
                        accept.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                stepsBeanList.get(0).setState(1);
                                for (int i = 0; i < 4; i++) {
                                    stepsBeanList.get(i + 1).setState(-1);
                                }
                                setStepStytle(setpview5, stepsBeanList);
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                            }
                        });
                        TipDialog.show((AppCompatActivity) getActivity(), "订单取消成功", TipDialog.TYPE.SUCCESS);
                    }
                }
            });

            binding.refuseButtonPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(label.equals("领取的任务")){
                        UserService user = new RetrofitUser().get(getContext()).create(UserService.class);
                        SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                        String mobile = pre.getString("mobile", "");
                        String token = pre.getString("token", "");
                        String orderid = String.valueOf(item.getId());
                        Call<ResponseBody> accept=user.Put_Fin_Cancel_Task(mobile,orderid,token
                                ,"df3b72a07a0a4fa1854a48b543690eab","reopen");
                        accept.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                            }
                        });
                        TipDialog.show((AppCompatActivity) getActivity(), "订单取消拒绝成功", TipDialog.TYPE.SUCCESS);
                    }
                }
            });

            //联系对方
            binding.ChatMyPurchase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!String.valueOf(item.getExecutor()).equals("") ||
                            !String.valueOf(item.getFrom_user()).equals("")) {
                        new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(@NonNull android.os.Message msg) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (label.equals("发布的任务")) {
                                            requestPermisson(v, String.valueOf(item.getExecutor())
                                                    , item.getId_photo1(), item.getId_name1());
                                        } else if (label.equals("领取的任务")) {
                                            requestPermisson(v, String.valueOf(item.getFrom_user())
                                                    , item.getId_photo(), item.getId_name());
                                        }
                                    }
                                }, 100);
                                LogUtil.d(new String(Character.toChars(0x1F60E)));
                                //viewModel.select(d1ata);
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 500);
                    }
                }
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


    @SuppressLint("CheckResult")
    private void requestPermisson(View view, String phone, String id_photo, String nickName) {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(requireActivity(), ChatActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("mobile", String.valueOf(phone));
                            bundle.putString("uuid", id_photo);
                            bundle.putString("id_name", nickName);
                            intent.putExtra("bundle", bundle);
                            startActivity(intent);
                        } else {
                            SetPermissionDialog mSetPermissionDialog = new SetPermissionDialog(getContext());
                            mSetPermissionDialog.show();
                            mSetPermissionDialog.setConfirmCancelListener(new SetPermissionDialog.OnConfirmCancelClickListener() {
                                @Override
                                public void onLeftClick() {
                                    //requireActivity().finish();
                                }

                                @Override
                                public void onRightClick() {
                                    //requireActivity().finish();
                                }
                            });
                        }
                    }
                });
    }
}