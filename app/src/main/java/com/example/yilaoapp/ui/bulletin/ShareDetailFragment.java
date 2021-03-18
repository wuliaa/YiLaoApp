package com.example.yilaoapp.ui.bulletin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yilaoapp.MyApplication;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.chat.activity.ChatActivity;
import com.example.yilaoapp.chat.util.LogUtil;
import com.example.yilaoapp.chat.widget.SetPermissionDialog;
import com.example.yilaoapp.databinding.FragmentShareDetailBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.yilaoapp.MyApplication.getContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareDetailFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate{

    private static final int PRC_PHOTO_PREVIEW = 1;
    FragmentShareDetailBinding binding;
    String uuid ;
    String nickName ;
    String phone;
    ArrayList<String> photosUrl;

    public ShareDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone="";
        uuid="";
        nickName="";
        photosUrl =new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_share_detail,container,false);
        BullentinViewModel viewModel = ViewModelProviders.of(requireActivity()).get(BullentinViewModel.class);
        binding.setData(viewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.popBackStack();
        });

        viewModel.getSelected().observe(getViewLifecycleOwner(), item -> {
            StringBuilder stringBuilder=new StringBuilder();
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
                    .into(binding.sharedHead);
            binding.sharedname.setText(item.getId_name());
            binding.sharedcontent.setText(item.getDetail());
            binding.sharedtime.setText(item.getCreate_at());
            binding.ShareninePhotoLayout.setDelegate(this);
            phone=String.valueOf(item.getFrom_user());
            nickName=item.getId_name();
            uuid=item.getId_photo();

            StringTokenizer st = new StringTokenizer(item.getPhotos(), ",");
            while (st.hasMoreTokens()) {
                uuid = st.nextToken();
                StringBuilder stringBuilder1=new StringBuilder();
                stringBuilder1.append("http://api.yilao.tk:15000/v1.0/users/")
                        .append(item.getPhone())
                        .append("/resources/")
                        .append(uuid);
                String url = stringBuilder1.toString();
                photosUrl.add(url);
            }
            binding.ShareninePhotoLayout.setData(photosUrl);
        });

        //联系对方
        binding.sharedname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pre=getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String mobile=pre.getString("monile","");
                if(mobile.equals(phone)){
                    Toast.makeText(getContext(),"自己不能联系自己",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!phone.equals("")) {
                        new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(@NonNull android.os.Message msg) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        requestPermisson(v);
                                    }
                                }, 100);
                                LogUtil.d(new String(Character.toChars(0x1F60E)));
                                //viewModel.select(d1ata);
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 500);
                    }
                }
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_share_detail, container, false);
    }

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (binding.ShareninePhotoLayout == null) {
            Log.d("ninePhotoLayout", "ninePhotoLayout: IsNull");
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(requireContext());

            // 保存图片的目录，如果传 null，则没有保存图片功能
            photoPreviewIntentBuilder.saveImgDir(downloadDir);

            if (binding.ShareninePhotoLayout.getItemCount() == 1) {
                // 预览单张图片
                photoPreviewIntentBuilder.previewPhoto(binding.ShareninePhotoLayout.getCurrentClickItem());
            } else if (binding.ShareninePhotoLayout.getItemCount() > 1) {
                // 预览多张图片
                photoPreviewIntentBuilder.previewPhotos(binding.ShareninePhotoLayout.getData())
                        .currentPosition(binding.ShareninePhotoLayout.getCurrentClickItemPosition()); // 当前预览图片的索引

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

    @SuppressLint("CheckResult")
    private void requestPermisson(View view) {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储权限
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                )
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Intent intent = new Intent(requireActivity(), ChatActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("mobile",String.valueOf(phone));
                            bundle.putString("uuid",uuid);
                            bundle.putString("id_name",nickName);
                            intent.putExtra("bundle",bundle);
                            startActivity(intent);
                        } else {
                            SetPermissionDialog mSetPermissionDialog = new SetPermissionDialog(getContext());
                            mSetPermissionDialog.show();
                            mSetPermissionDialog.setConfirmCancelListener(new SetPermissionDialog.OnConfirmCancelClickListener() {
                                @Override
                                public void onLeftClick() {
                                    //requireActivity().finish();
                                }

                                @Override
                                public void onRightClick() {
                                    //requireActivity().finish();
                                }
                            });
                        }
                    }
                });
    }
}
