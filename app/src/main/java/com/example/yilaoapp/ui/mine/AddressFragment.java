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
import com.example.yilaoapp.databinding.FragmentAddressBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentAddressBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_address,container,false);
        //binding.setData(mineViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.aToolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.aToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
                //controller.navigate(R.id.action_myInformationFragment_to_changeNickFragment);
            }
        });
        //修改用户地址
        UserService service=new RetrofitUser().get().create(UserService.class);
        SharedPreferences pre=getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String mobile=pre.getString("mobile","");
        String token=pre.getString("token","");
        binding.addressButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=binding.addressEditText4.getText().toString();
                if(address.equals("")){
                    Toast.makeText(getContext(),"输入的地址为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    Call<ResponseBody> updateInfo=service.updateInfo(mobile,"df3b72a07a0a4fa1854a48b543690eab",token,address);
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
        //return inflater.inflate(R.layout.fragment_address, container, false);
    }
}