package com.example.yilaoapp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yilaoapp.MainActivity;
import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentLoginBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.user.User;
import com.example.yilaoapp.service.UserService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentLoginBinding binding;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        //binding.setData(mineViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.loginImageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile,password;
                mobile=binding.loginEdittext.getText().toString();
                password=binding.loginEdittext1.getText().toString();
                if(mobile==null)
                    Toast.makeText(getContext(),"请输入手机号码",Toast.LENGTH_LONG).show();
                else if(password==null)
                    Toast.makeText(getContext(),"请输入密码",Toast.LENGTH_LONG).show();
                else if(mobile==null&&password==null)
                    Toast.makeText(getContext(),"请输入手机号码和密码",Toast.LENGTH_LONG).show();
                else{
                    UserService loginservice=new RetrofitUser().get().create(UserService.class);
                    Call<ResponseBody> loginback=loginservice.login_password(mobile,"df3b72a07a0a4fa1854a48b543690eab",password);
                    loginback.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            //System.out.println(response.body());
                            String token="";
                            try {
                                token=response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            SharedPreferences pre=getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor e=pre.edit();
                            e.putString("token",token);
                            e.putString("mobile",mobile);
                            e.putString("password",password);
                            e.commit();
                            System.out.println(token);
                            Toast.makeText(getContext(),"登录成功!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(),"账号密码错误",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        binding.loginTextview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_loginFragment2_to_missFragment2);
            }
        });
        binding.loginTextview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_loginFragment2_to_signinFragment2);
            }
        });

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_login, container, false);
    }
}