package com.example.yilaoapp.ui.mine;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.databinding.FragmentMyInformationBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.example.yilaoapp.service.image_service;
import com.example.yilaoapp.utils.PhotoOperation;
import com.example.yilaoapp.utils.SavePhoto;
import com.example.yilaoapp.utils.ServiceHelp;
import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.TipDialog;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyInformationFragment extends Fragment {
    FragmentMyInformationBinding binding;

    public MyInformationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_information, container, false);
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
        initUI();
        binding.informationCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomMenu.show((AppCompatActivity) requireContext(), new String[]{"上传图片", "取消"}, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        //返回参数 text 即菜单名称，index 即菜单索引
                        if (index == 0) {
                            getAuthority();
                        }
                    }
                });
            }
        });
        binding.informationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_myInformationFragment_to_changeNickFragment);
            }
        });
        binding.recyclerItemSb.setChecked(true);
        binding.recyclerItemSb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (binding.informationTextView13.getText() == "男") {
                    binding.informationTextView13.setText("女");
                    ServiceHelp.UserUpdate(getContext(), "sex", "female", false, null);
                } else {
                    binding.informationTextView13.setText("男");
                    ServiceHelp.UserUpdate(getContext(), "sex", "male", false, null);
                }
            }
        });
        binding.informationCardView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_myInformationFragment_to_addressFragment);
            }
        });
        getInfo();
        return binding.getRoot();
    }

    public void getAuthority(){
        String[] PERMISSIONS = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"};
        //检测是否有读的权限
        int permission = ContextCompat.checkSelfPermission(requireContext(),
                "android.permission.READ_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有读的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, 1);
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            TipDialog.show((AppCompatActivity) getActivity(), "上传失败", TipDialog.TYPE.ERROR);
        } else {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            startActivityForResult(intent, 200);//打开系统相册

        }
    }

    public void getInfo(){
        UserService service = new RetrofitUser().get().create(UserService.class);
        SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String mobile = pre.getString("mobile", "");
        String token = pre.getString("token", "");
        SharedPreferences.Editor e = pre.edit();
        Call<ResponseBody> get = service.get_user(mobile, "df3b72a07a0a4fa1854a48b543690eab", token);
        get.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() / 100 == 4) {
                    Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();
                } else {
                    String str = "";
                    try {
                        str = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    User user = gson.fromJson(str, User.class);
                    binding.informationTextView16.setText(user.getMobile().toString());
                    if (user.getSex() != null) {
                        if (user.getSex().equals("male")) {
                            binding.informationTextView13.setText("男");
                            e.putString("sex", "男");
                        } else {
                            binding.informationTextView13.setText("女");
                            e.putString("sex", "女");
                        }
                    }
                    if (user.getId_school() != null) {
                        binding.school.setText(user.getId_school());
                        e.putString("id_school", user.getId_school());
                    }
                    if (user.getId_name() != null) {
                        binding.informationTextView10.setText(user.getId_name());
                        e.putString("id_name", user.getId_name());
                    }
                    e.apply();
                    if (user.getId_photo() != null) {
                        BigInteger mobile = user.getMobile();
                        String url = "http://api.yilao.tk:5000/v1.0/users/" + mobile + "/resources/" +
                                user.getId_photo();
                        Glide.with(getContext()).load(url).into(binding.informationImageView2);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && null != data) {
            if (Build.VERSION.SDK_INT >= 19) {
                handleImageOnKitkat(data);
            } else {
                handleImageBeforeKitkat(data);
            }
        }
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            binding.informationImageView2.setImageBitmap(bitmap);
            TipDialog.show((AppCompatActivity) getActivity(), "上传成功", TipDialog.TYPE.SUCCESS);
        } else {
            Toast.makeText(getContext(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public void Refresh() {
        UserService service = new RetrofitUser().get().create(UserService.class);
        SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String mobile = pre.getString("mobile", "");
        String token = pre.getString("token", "");
        SharedPreferences.Editor e = pre.edit();
        //获取用户信息
        new Thread() {
            public void run() {
                Call<ResponseBody> get = service.get_user(mobile, "df3b72a07a0a4fa1854a48b543690eab", token);
                get.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String str = "";
                        try {
                            str = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        User user = gson.fromJson(str, User.class);
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.informationTextView16.setText(user.getMobile().toString());
                                if (user.getSex() != null) {
                                    if (user.getSex().equals("male")) {
                                        binding.informationTextView13.setText("男");
                                        e.putString("sex", "男");
                                    } else {
                                        binding.informationTextView13.setText("女");
                                        e.putString("sex", "女");
                                    }
                                }
                                if (user.getId_school() != null) {
                                    binding.school.setText(user.getId_school());
                                    e.putString("id_school", user.getId_school());
                                }
                                if (user.getId_name() != null) {
                                    binding.informationTextView10.setText(user.getId_name());
                                    e.putString("id_name", user.getId_name());
                                }
                                e.apply();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "网络连接失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
    }

    public void initUI() {
        SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        binding.informationTextView16.setText(pre.getString("mobile", ""));
        binding.school.setText(pre.getString("id_school", ""));
        binding.informationTextView10.setText(pre.getString("id_name", ""));
        String sex = pre.getString("sex", "");
        if (sex.equals("")) Refresh();
        else if (sex.equals("male"))
            binding.informationTextView13.setText("男");
        else binding.informationTextView13.setText("女");

    }
}