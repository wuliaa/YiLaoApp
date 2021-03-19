package com.example.yilaoapp.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yilaoapp.MyApplication;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.databinding.FragmentMyBulletinDetailBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBulletinDetailFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate {
    private static final int PRC_PHOTO_PREVIEW = 1;
    FragmentMyBulletinDetailBinding binding;
    String uuid;
    String nickName;
    ArrayList<String> photosUrl;

    public MyBulletinDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uuid = "";
        nickName = "";
        photosUrl = new ArrayList<String>();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_bulletin_detail, container, false);
        MineViewModel viewModel = ViewModelProviders.of(requireActivity()).get(MineViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });
        Point p = new Point();//获取窗口管理器
        WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        wm.getDefaultDisplay().getSize(p);
        int screenWidth = p.x; // 屏幕宽度
        binding.toolbar.setTitleMarginStart(screenWidth / 3);

        viewModel.getBulletin().observe(getViewLifecycleOwner(), item -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                    .append(item.getFrom_user())
                    .append("/resources/")
                    .append(item.getId_photo());
            String headurl = stringBuilder.toString();
            Glide.with(MyApplication.getContext())
                    .load(headurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.head1)
                    .error(R.drawable.head2)
                    .into(binding.BulletinHead);
            //获得昵称
            binding.BulletinName.setText(item.getId_name());
            binding.BulletinContent.setText(item.getDetail());
            binding.Bulletinaddress.setText("联系地址：" + item.getDestination().getName());
            binding.Bulletintime.setText("发布时间：" + item.getCreate_at());
            binding.chip1.setText(item.getCategory());
            binding.MyBulletinninePhotoLayout.setDelegate(this);
            StringTokenizer st = new StringTokenizer(item.getPhotos(), ",");
            while (st.hasMoreTokens()) {
                uuid = st.nextToken();
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("http://api.yilao.tk:15000/v1.0/users/")
                        .append(item.getPhone())
                        .append("/resources/")
                        .append(uuid);
                String url = stringBuilder1.toString();
                photosUrl.add(url);
            }
            binding.MyBulletinninePhotoLayout.setData(photosUrl);
            if(item.getClose_state()!=null) {
                if(item.getClose_state().equals("cancel"))
                binding.cancelBulletin.setVisibility(View.GONE);
            }


            binding.cancelBulletin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserService user = new RetrofitUser().get(getContext()).create(UserService.class);
                    SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                    String mobile = pre.getString("mobile", "");
                    String token = pre.getString("token", "");
                    String orderid = String.valueOf(item.getId());
                    Call<ResponseBody> cancelTask = user.Put_Fin_Cancel_Task(mobile, orderid,
                            token, "df3b72a07a0a4fa1854a48b543690eab", "cancel");
                    cancelTask.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.body() != null) {
                                Log.d("MyBulletinCancel", "onResponse: " + response.body().toString());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            Log.d("MyBulletinCancel", "onFailure: ");
                        }
                    });
                    TipDialog.show((AppCompatActivity) getActivity(), "取消成功", TipDialog.TYPE.SUCCESS);
                    binding.cancelBulletin.setVisibility(View.GONE);
                }
        });
    });

        return binding.getRoot();
    //return inflater.inflate(R.layout.fragment_purchase_detail, container, false);
}

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (binding.MyBulletinninePhotoLayout == null) {
            Log.d("ninePhotoLayout", "ninePhotoLayout: IsNull");
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(requireContext());

            // 保存图片的目录，如果传 null，则没有保存图片功能
            photoPreviewIntentBuilder.saveImgDir(downloadDir);

            if (binding.MyBulletinninePhotoLayout.getItemCount() == 1) {
                // 预览单张图片
                photoPreviewIntentBuilder.previewPhoto(binding.MyBulletinninePhotoLayout.getCurrentClickItem());
            } else if (binding.MyBulletinninePhotoLayout.getItemCount() > 1) {
                // 预览多张图片
                photoPreviewIntentBuilder.previewPhotos(binding.MyBulletinninePhotoLayout.getData())
                        .currentPosition(binding.MyBulletinninePhotoLayout.getCurrentClickItemPosition()); // 当前预览图片的索引

            }
            startActivity(photoPreviewIntentBuilder.build());
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", PRC_PHOTO_PREVIEW, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == PRC_PHOTO_PREVIEW) {
            Toast.makeText(requireContext(), "您拒绝了「图片预览」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        photoPreviewWrapper();
    }

    @Override
    public void onClickExpand(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        ninePhotoLayout.setIsExpand(true);
        ninePhotoLayout.flushItems();
    }
}