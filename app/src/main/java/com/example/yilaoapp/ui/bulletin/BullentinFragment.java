package com.example.yilaoapp.ui.bulletin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

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
import com.example.yilaoapp.databinding.FragmentBullentinBinding;
import com.example.yilaoapp.service.RetrofitUser;
import com.example.yilaoapp.service.bur_service;
import com.example.yilaoapp.service.image_service;
import com.example.yilaoapp.service.pur_service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BullentinFragment extends Fragment {
    public BullentinFragment() {
        // Required empty public constructor
    }
    FragmentBullentinBinding binding;
    private DrawerLayout mDrawerLayout;
    FragmentPagerItemAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDrawerLayout=requireActivity().findViewById(R.id.drawer_layout);

        adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add(R.string.shareTools, ShareToolsFragment.class)
                .add(R.string.lost, LostFoundFragment.class)
                .add(R.string.teamStudy, TeamStudyFragment.class)
                .create());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bullentin,container,false);
        //binding.setData(BullentinViewModel);
        binding.setLifecycleOwner(requireActivity());
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        binding.toolbar.inflateMenu(R.menu.menu_main);
        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_dehaze_24);
//        mDrawerLayout=requireActivity().findViewById(R.id.drawer_layout);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
//        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
//                getChildFragmentManager(), FragmentPagerItems.with(getContext())
//                .add(R.string.shareTools, ShareToolsFragment.class)
//                .add(R.string.lost, LostFoundFragment.class)
//                .add(R.string.teamStudy, TeamStudyFragment.class)
//                .create());
        binding.viewpager.setAdapter(adapter);
        binding.viewpagertab.setViewPager(binding.viewpager);

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_bullentin, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        binding.toolbar.getMenu().clear();
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavigationUI.onNavDestinationSelected(item,
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment));
        return true;
    }

    public void onRefresh() {
        bur_service bul = new RetrofitUser().get().create(bur_service.class);
        Call<ResponseBody> get_errand = bul.get_orders("代购");
        List<InputStream> photo = new LinkedList<>();//用户照片
        List<List<InputStream>> order_photos = new LinkedList<>();//订单照片
        List<InputStream> photos = new LinkedList<>();
        get_errand.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String str = "";
                try {
                    str = response.body().string();
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<All_orders>>() {
                    }.getType();
                    List<All_orders> all = gson.fromJson(str, type);
                    String uid = "";
                    BigInteger mobile;
                    image_service load = new RetrofitUser().get().create(image_service.class);
                    //获取每个用户的照片的字节流
                    for (int i = 0; i < all.size(); i++) {
                        uid = all.get(i).getId_photo();
                        mobile = all.get(i).getFrom_user();
                        Call<ResponseBody> load_back = load.load_photo(mobile, uid, "df3b72a07a0a4fa1854a48b543690eab");
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
                        String ids = all.get(0).getPhotos();
                        StringTokenizer st = new StringTokenizer(ids, ",");
                        while (st.hasMoreTokens()) {
                            uid = st.nextToken();
                            load_back = load.load_photo(mobile, uid, "df3b72a07a0a4fa1854a48b543690eab");
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
                            System.out.println("");
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
    }
}