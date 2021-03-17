package com.example.yilaoapp.ui.errands;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.Point_address;
import com.example.yilaoapp.bean.errand_order;
import com.example.yilaoapp.bean.errand_task;
import com.example.yilaoapp.databinding.FragmentErrandsMessageBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.errand_service;
import com.kongzue.dialog.v3.TipDialog;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrandsMessageFragment extends Fragment {

    public ErrandsMessageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentErrandsMessageBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_errands_message,container,false);
        //binding.setData(ErrandsViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NavController controller = Navigation.findNavController(v);
            controller.popBackStack();
        }
    });

        int num = 40;//限制的最大字数
        binding.editTextTextMultiLine.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                binding.numViewErrands.setText(""+number+"/40");
                selectionStart = binding.editTextTextMultiLine.getSelectionStart();
                selectionEnd = binding.editTextTextMultiLine.getSelectionEnd();
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    binding.editTextTextMultiLine.setText(s);
                    binding.editTextTextMultiLine.setSelection(tempSelection);//设置光标在最后
                }
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detail=binding.editTextTextMultiLine.getText().toString();
                BigInteger phone=new BigInteger(binding.telephoneText.getText().toString());
                float money= Float.parseFloat(binding.moneyText.getText().toString());
                String address=binding.addressText.getText().toString();;
                Point_address des=new Point_address(0,0,address);
                errand_order order=new errand_order(phone,"跑腿",detail,des,money);
                SharedPreferences pre=getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String mobile=pre.getString("mobile","");
                String token=pre.getString("token","");
                errand_service errand=new RetrofitUser().get(getContext()).create(errand_service.class);
                Call<ResponseBody> errand_back=errand.new_order(mobile,token,"df3b72a07a0a4fa1854a48b543690eab",order);
                errand_back.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        TipDialog.show((AppCompatActivity) getActivity(), "发布成功", TipDialog.TYPE.SUCCESS);
                        new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(@NonNull android.os.Message msg) {
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 3000);
                        NavController controller = Navigation.findNavController(view);
                        controller.popBackStack();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println(1111);
                    }
                });

            }
        });
        return binding.getRoot();
    }
}