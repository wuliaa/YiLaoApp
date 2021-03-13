package com.example.yilaoapp.ui.purchase;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yilaoapp.MyApplication;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.Message;
import com.example.yilaoapp.bean.User;
import com.example.yilaoapp.bean.chat_task;
import com.example.yilaoapp.chat.activity.ChatActivity;
import com.example.yilaoapp.databinding.FragmentPurchaseDetailBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.example.yilaoapp.service.accept_service;
import com.example.yilaoapp.service.chat_service;
import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
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

import static com.example.yilaoapp.MyApplication.getContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseDetailFragment extends Fragment
        implements EasyPermissions.PermissionCallbacks, BGANinePhotoLayout.Delegate {

    private static final int PRC_PHOTO_PREVIEW = 1;
    int id;
    String uuid;
    ArrayList<String> photosUrl;
    String nickName;
    BigInteger phone;
    String id_photo;
    String detail;
    public PurchaseDetailFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id=-1;
        uuid = "";
        nickName = "";
        id_photo="";
        detail="";
        photosUrl = new ArrayList<String>();
    }

    FragmentPurchaseDetailBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_detail, container, false);
        PurchaseViewModel viewModel = ViewModelProviders.of(requireActivity()).get(PurchaseViewModel.class);
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
        viewModel.getPurchase().observe(getViewLifecycleOwner(), item -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                    .append(item.getPhone())
                    .append("/resources/")
                    .append(item.getId_photo());
            id=item.getId();
            String headurl = stringBuilder.toString();
            Glide.with(MyApplication.getContext())
                    .load(headurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.head1)
                    .error(R.drawable.head2)
                    .into(binding.purchasedHead);
            id_photo=item.getId_photo();
            phone =item.getFrom_user();
            nickName = item.getId_name();
            detail=item.getDetail();
            binding.purchasedname.setText(nickName);



            binding.purchasedcontent.setText(item.getDetail());
            binding.purchasedmoney.setText("金额：" + item.getReward());
            binding.purchasedchip.setText(item.getCategory());
            binding.PurchaseninePhoto.setDelegate(this);
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
            binding.PurchaseninePhoto.setData(photosUrl);
        });
        //联系对方
        binding.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(phone) != null && id_photo!= null) {
                    Intent intent = new Intent(requireActivity(), ChatActivity.class);
                    intent.putExtra("mobile", String.valueOf(phone));
                    intent.putExtra("uuid", id_photo);
                    startActivity(intent);
                }
            }
        });
        //加入任务
        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pre = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String mobile = pre.getString("mobile", "");
                String token = pre.getString("token", "");
                accept_service ac = new RetrofitUser().get(getContext()).create(accept_service.class);
                Call<ResponseBody> act = ac.accept_order(mobile,id, token, "df3b72a07a0a4fa1854a48b543690eab", "true");
                act.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        TipDialog.show((AppCompatActivity) getActivity(), "领取成功", TipDialog.TYPE.SUCCESS);
                        new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(@NonNull android.os.Message msg) {
                                return false;
                            }
                        }).sendEmptyMessageDelayed(0, 3000);
                        chat_task ch = new chat_task("您的任务我已领取，订单信息如下:" + detail,phone);
                        chat_service send = new RetrofitUser().get(getContext()).create(chat_service.class);
                        Call<ResponseBody> sen_mes = send.send_message(mobile, token, "df3b72a07a0a4fa1854a48b543690eab", ch);
                        sen_mes.enqueue(new Callback<ResponseBody>() {
                           @Override
                           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(getContext(),"success",Toast.LENGTH_LONG).show();
                           }

                           @Override
                           public void onFailure(Call<ResponseBody> call, Throwable t) {

                           }
                       });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        return binding.getRoot();
    }

    /**

     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (binding.PurchaseninePhoto == null) {
            Log.d("ninePhotoLayout", "ninePhotoLayout: IsNull");
            return;
        }
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
            BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(requireContext());

            // 保存图片的目录，如果传 null，则没有保存图片功能
            photoPreviewIntentBuilder.saveImgDir(downloadDir);

            if (binding.PurchaseninePhoto.getItemCount() == 1) {
                // 预览单张图片
                photoPreviewIntentBuilder.previewPhoto(binding.PurchaseninePhoto.getCurrentClickItem());
            } else if (binding.PurchaseninePhoto.getItemCount() > 1) {
                // 预览多张图片
                photoPreviewIntentBuilder.previewPhotos(binding.PurchaseninePhoto.getData())
                        .currentPosition(binding.PurchaseninePhoto.getCurrentClickItemPosition()); // 当前预览图片的索引

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