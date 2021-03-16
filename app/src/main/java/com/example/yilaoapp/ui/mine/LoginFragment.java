package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yilaoapp.MainActivity;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.bean.tok;
import com.example.yilaoapp.databinding.FragmentLoginBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;

import java.io.IOException;
import java.util.Objects;

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

    private Callback<ResponseBody> callback;
    ProgressDialog mProgressDialog;//新建一个ProgressDialog
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        //binding.setData(mineViewModel);
        binding.setLifecycleOwner(requireActivity());

        binding.loginImageview1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                String mobile, password;
                mobile = binding.loginEdittext.getText().toString();
                password = binding.loginEdittext1.getText().toString();
                if (mobile.equals(""))
                    Toast.makeText(getContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                else if (password.equals(""))
                    Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                else {
                    initProgressDialog();
                    login(v, mobile, password);
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
    }

    public void initProgressDialog() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Loading...");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.show();
    }

    public void login(View v, String mobile, String password) {
        UserService service = new RetrofitUser().get(getContext()).create(UserService.class);
        Call<ResponseBody> loginback = service.login_password(mobile, "df3b72a07a0a4fa1854a48b543690eab", password);
        loginback.enqueue(callback=new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(222);
                System.out.println(response.message());
                if (response.code() / 100 == 4) {
                    UIOp('1',null);
                } else if (response.code() / 100 == 5) {
                    UIOp('2',null);
                } else {
                    String str = "";
                    try {
                        str = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    tok token = gson.fromJson(str, tok.class);
                    SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = pre.edit();
                    e.putString("token", token.getToken());
                    e.putString("mobile", mobile);
                    e.putString("password", password);
                    e.apply(); //apply()比commit()更快
                    GetInfo(service,mobile,token.getToken(),v);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
                UIOp('5',null);
            }
        });
    }

    public void GetInfo(UserService service,String mobile,String token,View v){
        Call<ResponseBody> userCall = service.get_user(mobile, "df3b72a07a0a4fa1854a48b543690eab", token);
        userCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String u = null;
                try {
                    u = response.body().string();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Gson gson1 = new Gson();
                User user = gson1.fromJson(u, User.class);
                if (user.getId_name() == null || user.getId_photo() == null || user.getSex() == null || user.getId_school() == null) {
                    UIOp('3',v);
                } else {
                    UIOp('4',null);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                UIOp('5',null);
            }
        });
    }

    public void UIOp(char c,View v) {
        switch (c) {
            case '1':
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), "账号或密码输入错误", Toast.LENGTH_SHORT).show();
                break;
            case '2':
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), "服务器错误", Toast.LENGTH_SHORT).show();
                break;
            case '3':
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), "该账户还没有进行认证，请前往认证，填写完整信息！", Toast.LENGTH_SHORT).show();
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_loginFragment2_to_userFragment2);
                break;
            case '4':
                mProgressDialog.dismiss();
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
                break;
            case '5':
                mProgressDialog.dismiss();
                Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public Callback<ResponseBody> getCallBack(){
        return callback;
    }

    @Override
    public void onDestroy() {
        //将线程销毁掉
        //handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
