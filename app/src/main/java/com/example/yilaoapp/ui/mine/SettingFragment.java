package com.example.yilaoapp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yilaoapp.LoginActivity;
import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentSettingBinding;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentSettingBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setting,container,false);
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
        binding.settingCardView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_settingFragment_to_changePsdFragment);
            }
        });
        binding.settingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_setting, container, false);
    }
    public void logout(View view){//"退出登录"button的回调方法
        //1、将保存在sp中的数据删除
        SharedPreferences sp = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().clear().commit();//清除数据必须要提交:提交以后，文件仍存在，只是文件中的数据被清除了
        //2、将本地保存的图片的file删除
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = getContext().getExternalFilesDir("");
        } else {//手机内部存储
            //路径：data/data/包名/files
            filesDir = getContext().getFilesDir();
        }
        File file = new File(filesDir, "icon.png");
        if (file.exists()){
            file.delete();//删除存储中的文件
        }
        //3、销毁所有的Activity
        requireActivity().finish();
        //4、重新进入首页面
        goToActivity(LoginActivity.class,null);
    }
    public void goToActivity(Class Activity,Bundle bundle){
        Intent intent = new Intent(requireActivity(),Activity);
        if (bundle!=null&&bundle.size()!=0){
            intent.putExtra("data",bundle);
        }
        startActivity(intent);
    }
}