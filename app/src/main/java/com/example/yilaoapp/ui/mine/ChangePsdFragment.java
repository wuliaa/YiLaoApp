package com.example.yilaoapp.ui.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentChangePsdBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePsdFragment extends Fragment {

    public ChangePsdFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentChangePsdBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_change_psd,container,false);
        //binding.setData(MineViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });
        //修改用户密码
        //获取用户信息
        UserService service=new RetrofitUser().get().create(UserService.class);
        SharedPreferences pre=getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String mobile=pre.getString("mobile","");
        String token=pre.getString("token","");
        String psd=pre.getString("password","");
        binding.changePsdButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.editText9.getText().toString().equals("")||
                binding.changePsdEditText10.getText().toString().equals("")||
                binding.changePsdEditText11.getText().toString().equals("")){
                    Toast.makeText(getContext(),"密码输入不能为空",Toast.LENGTH_LONG).show();
                }
                else if(binding.editText9.getText().toString()!=psd){
                    Toast.makeText(getContext(),"原始密码输入错误",Toast.LENGTH_SHORT).show();
                }else if(binding.editText9.getText().toString()
                        ==binding.changePsdEditText10.getText().toString()&&
                        binding.changePsdEditText10.getText().toString()
                                ==binding.changePsdEditText11.getText().toString()){
                    String password=binding.changePsdEditText10.getText().toString();
                    Call<ResponseBody> updateInfo=service.updateInfo(mobile,"df3b72a07a0a4fa1854a48b543690eab",token,password);
                    updateInfo.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();
                            NavController controller = Navigation.findNavController(v);
                            controller.popBackStack();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_change_psd, container, false);
    }
}