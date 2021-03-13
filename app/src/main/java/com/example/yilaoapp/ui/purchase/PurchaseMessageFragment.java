package com.example.yilaoapp.ui.purchase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yilaoapp.MainActivity;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.Point_address;
import com.example.yilaoapp.bean.Uuid;
import com.example.yilaoapp.bean.messbean;
import com.example.yilaoapp.bean.pur_order;
import com.example.yilaoapp.databinding.FragmentPurchaseMessageBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.UserService;
import com.example.yilaoapp.service.image_service;
import com.example.yilaoapp.service.pur_service;
import com.example.yilaoapp.utils.PhotoOperation;
import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseMessageFragment extends Fragment implements EasyPermissions.PermissionCallbacks, BGASortableNinePhotoLayout.Delegate {

    private static final int PRC_PHOTO_PICKER = 1;
    private static final int RC_CHOOSE_PHOTO = 1;
    private static final int RC_PHOTO_PREVIEW = 2;
    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";

    public PurchaseMessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentPurchaseMessageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_message, container, false);
        //binding.setData(PurchaseViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });
        binding.mPurchasePhotosSnpl.setMaxItemCount(9);
        binding.mPurchasePhotosSnpl.setEditable(true);
        binding.mPurchasePhotosSnpl.setPlusEnable(true);
        binding.mPurchasePhotosSnpl.setSortable(true);
        binding.mPurchasePhotosSnpl.setDelegate((BGASortableNinePhotoLayout.Delegate) this);
        binding.Purchasefinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] m = null;
                PhotoOperation p = new PhotoOperation();
                Map<String, RequestBody> map = new HashMap<>();
                for (int i = 0; i < binding.mPurchasePhotosSnpl.getData().size(); i++) {
                    try {
                        m = p.Path2ByteArray(binding.mPurchasePhotosSnpl.getData().get(i));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/from-data"), m);
                    map.put("file\"; filename=\"" + Integer.toString(i) + ".jpeg", requestFile);
                }
                SharedPreferences pre2 = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                String mobile2 = pre2.getString("mobile", "");
                String token2 = pre2.getString("token", "");
                image_service img = new RetrofitUser().get(getContext()).create(image_service.class);
                Call<ResponseBody> image_call = img.send_photo(mobile2, token2, "df3b72a07a0a4fa1854a48b543690eab", map);
                image_call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse
                            (Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() / 100 == 4) {
                            Toast.makeText(getContext(), "上传失败，请重新上传", Toast.LENGTH_LONG).show();
                        } else {
                            String uid = "";
                            try {
                                uid = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println(uid);
                            Gson gson = new Gson();
                            Uuid u = gson.fromJson(uid, Uuid.class);
                            System.out.println(u.getUuid());
                            String detail = binding.editTextTextMultiLine.getText().toString();
                            String address = binding.addressText.getText().toString();
                            BigInteger phone = new BigInteger(binding.telephoneText.getText().toString());
                            String name = binding.purchaseObjectName.getText().toString();
                            float money = Float.parseFloat(binding.moneyText.getText().toString());
                            String category = "";
                            if (binding.radioButton4.isChecked())
                                category = "能够代购";
                            else
                                category = "找代购";
                            Point_address des = new Point_address(0, 0, address);
                            pur_order order = new pur_order(phone, "代购", detail, des, category, money, u.getUuid(), name);
                            pur_service pur = new RetrofitUser().get(getContext()).create(pur_service.class);
                            Call<ResponseBody> new_order = pur.new_order(mobile2, token2, "df3b72a07a0a4fa1854a48b543690eab", order);
                            new_order.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    TipDialog.show((AppCompatActivity) getActivity(), "发布成功", TipDialog.TYPE.SUCCESS);
                                    new Handler(new Handler.Callback() {
                                        @Override
                                        public boolean handleMessage(@NonNull android.os.Message msg) {
                                            return false;
                                        }
                                    }).sendEmptyMessageDelayed(0, 3000);
                                    NavController controller = Navigation.findNavController(v);
                                    controller.popBackStack();
                                }
                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_bullentin_message, container, false);
    }


    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(requireContext(), perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(requireContext())
                    .cameraFileDir(null) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(
                            binding.mPurchasePhotosSnpl.getMaxItemCount() - binding.mPurchasePhotosSnpl.getItemCount()) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", PRC_PHOTO_PICKER, perms);
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
        if (requestCode == PRC_PHOTO_PICKER) {
            Toast.makeText(requireContext(), "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            binding.mPurchasePhotosSnpl.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data));
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            binding.mPurchasePhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        binding.mPurchasePhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(requireContext())
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(binding.mPurchasePhotosSnpl.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout sortableNinePhotoLayout, int fromPosition, int toPosition, ArrayList<String> models) {
        Toast.makeText(requireContext(), "排序发生变化", Toast.LENGTH_SHORT).show();
    }
}