package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.databinding.FragmentMyErrandsDetailBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v3.TipDialog;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyErrandsDetailFragment extends Fragment {

    FragmentMyErrandsDetailBinding binding;
    String label;
    String NickName;

    public MyErrandsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        label = "";
        NickName = "";
    }

    @SuppressLint({"SetTextI18n", "ShowToast"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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

        viewModel.getErrands().observe(getViewLifecycleOwner(), item -> {
            StringBuilder stringBuilder = new StringBuilder();
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
                    .into(binding.myErrandsHead);
            //获得昵称
            binding.MyErrandsname.setText(item.getId_name());
            binding.MyErrandscontent.setText("详情：" + item.getDetail());
            binding.MyErrandsphoneNumber.setText("联系电话：" + item.getPhone());
            binding.MyErrandsAddress.setText("联系地址：" + item.getDestination().getName());
            binding.MyErrandsmoney.setText("金额：" + item.getReward() + "元");
            //设置完成chip
            SharedPreferences pre2 = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            String mobile2 = pre2.getString("mobile", "");
            if (mobile2.equals(String.valueOf(item.getFrom_user())))
                label = "发布的任务";
            else
                label = "领取的任务";
            binding.chip1.setText(label);
            Log.d("MyErrandDetail", " out: " + label);

            //设置底下的button的出现还是消失
            //完成任务的button只能在“发布任务”中可以看见
            if (label.equals("发布的任务")) {
                binding.compeleteButtonErrands.setVisibility(View.VISIBLE);
            } else {
                binding.compeleteButtonErrands.setVisibility(View.GONE);
            }
            //如果任务是cancel或者finish，那么button都不可见
            Log.d("getClose_stateErrand", item.getDetail() + " DetailFragment：" + item.getClose_state());
            if (item.getClose_state() != null) {
                if (item.getClose_state().equals("cancel") ||
                        item.getClose_state().equals("finish") ||
                        item.getClose_state().equals("canceling")) {
                    binding.compeleteButtonErrands.setVisibility(View.GONE);
                    binding.cancelButtonErrands.setVisibility(View.GONE);
                }
                if (item.getClose_state().equals("canceling") &&
                        label.equals("领取的任务")) {
                    binding.acceptButtonErrands.setVisibility(View.VISIBLE);
                    binding.refuseButtonErrands.setVisibility(View.VISIBLE);
                }
            }

            //设置最上面的状态栏
            HorizontalStepView setpview5 = (HorizontalStepView) binding.stepViewErrand;
            List<StepBean> stepsBeanList = new ArrayList<>();
            StepBean stepBean0 = new StepBean("取消", -1);
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
                if (item.getReceive_at()!=null) {
                    stepsBeanList.get(2).setState(1);
                    stepsBeanList.get(3).setState(0);
                }
            }
            Log.d("getClose_stateErrand", " outlabel " + item.getReceive_at());

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
            binding.cancelButtonErrands.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserService user = new RetrofitUser().get(getContext()).create(UserService.class);
                    SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    String mobile = pre.getString("mobile", "");
                    String token = pre.getString("token", "");
                    String orderid = String.valueOf(item.getId());
                    if (label.equals("发布的任务")) {
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
                                    Log.d("MyErrandCancel", "onResponse: " + response.body().toString());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                Log.d("MyErrandCancel", "onFailure: ");
                            }
                        });
                    } else if (label.equals("领取的任务")) {
                        Call<ResponseBody> cancelTask = user.Get_Acc_Cancel_Order(mobile, orderid, token,
                                "df3b72a07a0a4fa1854a48b543690eab", "false");
                        cancelTask.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.code()/100==4) {
                                    TipDialog.show((AppCompatActivity) getActivity(), "接单取消失败", TipDialog.TYPE.ERROR);
                                }else {
                                    stepsBeanList.get(0).setState(1);
                                    for (int i = 0; i < 4; i++) {
                                        stepsBeanList.get(i + 1).setState(-1);
                                    }
                                    setStepStytle(setpview5, stepsBeanList);
                                    TipDialog.show((AppCompatActivity) getActivity(), "接单取消成功", TipDialog.TYPE.SUCCESS);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }
            });

            binding.compeleteButtonErrands.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stepsBeanList.get(2).getState() == -1) {
                        //没被领取了，就不能点击完成任务
                        Toast.makeText(requireContext(), "任务未被领取，不能完成任务。" +
                                "取消任务可以按取消按钮", Toast.LENGTH_LONG).show();
                        ;
                    } else {
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

            binding.acceptButtonErrands.setOnClickListener(new View.OnClickListener() {
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

            binding.refuseButtonErrands.setOnClickListener(new View.OnClickListener() {
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
                                stepsBeanList.get(0).setState(-1);
                                stepsBeanList.get(1).setState(1);
                                stepsBeanList.get(2).setState(1);
                                stepsBeanList.get(3).setState(0);
                                setStepStytle(setpview5, stepsBeanList);
                            }

                            @Override
                            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                            }
                        });
                        TipDialog.show((AppCompatActivity) getActivity(), "订单取消拒绝成功", TipDialog.TYPE.SUCCESS);
                    }
                }
            });

        });
        return binding.getRoot();
    }

    public void setStepStytle(HorizontalStepView stepView5, List<StepBean> stepBeanList) {
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
