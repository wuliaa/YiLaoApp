package com.example.yilaoapp.ui.purchase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.databinding.FragmentPurchaseBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.image_service;
import com.example.yilaoapp.service.pur_service;
import com.example.yilaoapp.ui.errands.ErrandsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Vector;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    FragmentPurchaseBinding binding;
    private DrawerLayout mDrawerLayout;
    private PurchaseViewModel mViewModel;
    private List<Purchase> purchaseList = new ArrayList<>();

    public PurchaseFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for t his fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_purchase,container,false);
        mViewModel = ViewModelProviders.of(requireActivity()).get( PurchaseViewModel.class);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(requireActivity());
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        //隐藏toolbar的标题
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        binding.toolbar.inflateMenu(R.menu.menu_main);
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24);
        mDrawerLayout=requireActivity().findViewById(R.id.drawer_layout);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        setHasOptionsMenu(true);

        binding.swipePurchasess.setOnRefreshListener(this);
        initContents();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.purchasesRecyclerview.setLayoutManager(layoutManager);
        PurchaseAdapter adapter = new PurchaseAdapter(purchaseList);
        binding.purchasesRecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new PurchaseAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Purchase data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
//                        Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_purchaseFragment_to_purchaseDetailFragment);
                         mViewModel.setPurchase(data);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_purchase, container, false);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavigationUI.onNavDestinationSelected(item,
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment));
        return true;
    }

    private void initContents() {
        ArrayList<String> photos = new ArrayList<>();
        for (int i = 0; i <9; i++) {
            photos.add("http://bgashare.bingoogolapple.cn/refreshlayout/images/staggered2.png");
//            photos.add("/storage/emulated/0/sina/weibo/storage/photoalbum_save/weibo/img-ebc3581e69b48d8c1bc1365971f12d90.jpg");
//            photos.add("http://api.yilao.tk:15000/v1.0/users/13060887368/resources/2f5a0fff-5f37-4bb9-8667-2c3beb00dfe8");
        }
        for (int i = 0; i < 2; i++) {
            Purchase l1 = new Purchase("口红","2020年12月3日 下午6:00前，可以在日本买到免税的MAC口红",
                    "￥90/一个", "代购",photos,R.drawable.head2);
            purchaseList.add(l1);
            Purchase l2 = new Purchase("眼影","希望买一个3CE的眼影盘","￥150/一盘",
                    "找代购",photos,R.drawable.head2);
            purchaseList.add(l2);
            Purchase l3 = new Purchase("面膜","2020年12月1日前，可以在欧洲买到La Prairie蓓丽鱼子精华睡眠面膜",
                    "￥2800/50ml", "代购",photos,R.drawable.head2);
            purchaseList.add(l3);
        }
    }

    @Override
    public void onRefresh() {
        pur_service pur=new RetrofitUser().get(getContext()).create(pur_service.class);
        Call<ResponseBody> get_errand=pur.get_orders("代购");
        List<InputStream> photo=new LinkedList<>();//用户照片
        List<List<InputStream>> order_photos=new LinkedList<>();//订单照片
        List<InputStream> photos=new LinkedList<>();
        get_errand.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String str="";
                try {
                    str=response.body().string();
                    Gson gson=new Gson();
                    Type type=new TypeToken<List<All_orders>>(){}.getType();
                    List<All_orders> all=gson.fromJson(str,type);
                    String uid="";
                    BigInteger mobile;
                    image_service load=new RetrofitUser().get(getContext()).create(image_service.class);
                    //获取每个用户的照片的字节流
                    for(int i=0;i<all.size();i++){
                        uid=all.get(i).getId_photo();
                        mobile=all.get(i).getFrom_user();
                        Call<ResponseBody> load_back=load.load_photo(mobile,uid,"df3b72a07a0a4fa1854a48b543690eab");
                        load_back.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                assert response.body() != null;
                                photo.add(response.body().byteStream());
                                try {
                                    response.body().byteStream().close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // photo.add()
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        //获取用户订单图片
                        String ids=all.get(0).getPhotos();
                        StringTokenizer st=new StringTokenizer(ids,",");
                        while(st.hasMoreTokens() ){
                            uid=st.nextToken();
                            load_back=load.load_photo(mobile,uid,"df3b72a07a0a4fa1854a48b543690eab");
                            load_back.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    assert response.body() != null;
                                    photos.add(response.body().byteStream());
                                    //order_photos.get(j).add(response.body().byteStream());
                                    try {
                                        response.body().byteStream().close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    // photo.add()
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                            System.out.println(uid);
                        }
                        order_photos.add(photos);
                        photos.clear();
                    }

                    //System.out.println(all.get(0).getDetail());
                    //System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        binding.swipePurchasess.postDelayed(new Runnable() { // 发送延迟消息到消息队列
            @Override
            public void run() {
                binding.swipePurchasess.setRefreshing(false); // 是否显示刷新进度;false:不显示
            }
        },3000);
    }
}