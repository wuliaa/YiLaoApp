package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMissBinding;
import com.example.yilaoapp.user.RetrofitUser;
import com.example.yilaoapp.user.yzm;
import com.example.yilaoapp.user.yzmbean;
import com.kongzue.dialog.v3.TipDialog;

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
                            binding.missgetYZM.setText("重新获取");
                        }
                    });
                }
            }
        });
        binding.missQrxg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_miss, container, false);
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