package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMissBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.example.yilaoapp.service.Verify_service;
import com.example.yilaoapp.bean.Verify;
import com.example.yilaoapp.utils.ConfigUtil;
import com.kongzue.dialog.v3.TipDialog;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissFragment extends Fragment {

    public MissFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentMissBinding binding;
    //验证码的倒计时 new倒计时对象,总共的时间,每隔多少秒更新一次时间
    final MissFragment.MyCountDownTimer myCountDownTimer = new MissFragment.MyCountDownTimer(60000,1000);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_miss,container,false);
        //binding.setData(mineViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });
        binding.missgetYZM.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String phone=binding.missPhone.getText().toString();
                if(!ConfigUtil.isPhoneNum(phone))
                    Toast.makeText(getContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                else{
                    getCode(phone);
                }
            }
        });
        binding.missQrxg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改用户密码
                SharedPreferences pre=getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String psd=pre.getString("password","");
                String mobile=pre.getString("mobile","");
                if(binding.missPass.getText().toString().equals("")||
                        binding.missAgainpass.getText().toString().equals("")){
                    Toast.makeText(getContext(),"密码输入不能为空",Toast.LENGTH_LONG).show();
                }
                else if(!binding.missPass.getText().toString()
                        .equals(binding.missAgainpass.getText().toString())){
                    Toast.makeText(getContext(),"输入新密码不一致",Toast.LENGTH_LONG).show();
                }
                else {
                    changePsd(mobile,psd,v);
                }
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_miss, container, false);
    }

    public void getCode(String phone){
        Verify_service yzmservice=new RetrofitUser().get().create(Verify_service.class);
        Verify yz=new Verify("df3b72a07a0a4fa1854a48b543690eab",phone,"PUT","/v1.0/users/"+phone);
        Call<ResponseBody> callback=yzmservice.send_code(yz);
        callback.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TipDialog.show((AppCompatActivity) getActivity(), "发送成功", TipDialog.TYPE.SUCCESS);
                myCountDownTimer.start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TipDialog.show((AppCompatActivity) getActivity(), "发送失败", TipDialog.TYPE.ERROR);
                binding.missgetYZM.setText("重新获取");
            }
        });
    }

    public void changePsd(String mobile,String psd,View v){
        UserService service = new RetrofitUser().get().create(UserService.class);
        String password = binding.missPass.getText().toString();
        Call<ResponseBody> back = service.login_password(mobile, "df3b72a07a0a4fa1854a48b543690eab", psd);
        back.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("passwd", binding.missPass.getText().toString());
                Call<ResponseBody> updatepsd = service.updatePsd(mobile, "df3b72a07a0a4fa1854a48b543690eab", password,
                        map);
                updatepsd.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        SharedPreferences pre=getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor e = pre.edit();
                        e.putString("password", password);
                        e.apply();
                        NavController controller = Navigation.findNavController(v);
                        controller.popBackStack();
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "原始密码输入错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //倒计时的类和函数
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            binding.missgetYZM.setClickable(false);
            binding.missgetYZM.setText(l/1000+"秒");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            binding.missgetYZM.setText("重新获取");
            //设置可点击
            binding.missgetYZM.setClickable(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
    }
}