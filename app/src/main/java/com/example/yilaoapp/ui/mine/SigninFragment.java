package com.example.yilaoapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yilaoapp.user.User;
import com.example.yilaoapp.user.pd;
import com.example.yilaoapp.user.yzm;
import com.example.yilaoapp.user.yzmbean;
import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentSigninBinding;
import com.example.yilaoapp.user.RetrofitUser;
import com.example.yilaoapp.user.UserService;
import com.kongzue.dialog.v3.TipDialog;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;

/**
 * A simple {@link Fragment} subclass.
 */
public class SigninFragment extends Fragment {
    public SigninFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentSigninBinding binding;
    //验证码的倒计时 new倒计时对象,总共的时间,每隔多少秒更新一次时间
    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_signin,container,false);
        //binding.setData(mineViewModel);
        ((ViewDataBinding) binding).setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });
        binding.getYZM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=binding.signinPhone.getText().toString();
                if(phone.length()!=11)
                    Toast.makeText(getContext(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                else{
                    yzm yzmservice=new RetrofitUser().get().create(yzm.class);
                    yzmbean yz=new yzmbean("df3b72a07a0a4fa1854a48b543690eab",phone,"PUT","http://api.yilao.tk:5000/v1.0/users/"+phone);
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
                            binding.getYZM.setText("重新获取");
                        }
                    });
                }
            }
        });
        binding.signinXyb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1,pwd2,code,phone;
                pwd1=binding.signinPass.getText().toString();
                pwd2=binding.signinPassagain.getText().toString();
                code=binding.signinInputyzm.getText().toString();
                phone=binding.signinPhone.getText().toString();
               if(pwd1.equals(pwd2)&&phone.length()==11&&code.length()==4){
                    pd pass=new pd(pwd1);
                    UserService xybservice=new RetrofitUser().get().create(UserService.class);
                    Call<ResponseBody> xybback=xybservice.sigin(phone,"df3b72a07a0a4fa1854a48b543690eab",code,pass);
                    xybback.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            System.out.println(response.body());


                            NavController controller = Navigation.findNavController(v);
                            controller.navigate(R.id.action_signinFragment2_to_userFragment2);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                             System.out.println(t.toString());
                        }
                    });
                }
               else{
                   Toast.makeText(getContext(),"请输入正确密码或验证码",Toast.LENGTH_LONG).show();
               }
            }
        });

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_signin, container, false);
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
            binding.getYZM.setClickable(false);
            binding.getYZM.setText(l/1000+"秒");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            binding.getYZM.setText("重新获取");
            //设置可点击
            binding.getYZM.setClickable(true);
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