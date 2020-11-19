package com.example.yilaoapp.ui.errands;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentErrandsBinding;
import com.example.yilaoapp.ui.bulletin.BullentinViewModel;
import com.example.yilaoapp.ui.bulletin.Share;
import com.example.yilaoapp.ui.bulletin.ShareAdapter;
import com.example.yilaoapp.ui.bulletin.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrandsFragment extends Fragment {

    private ErrandsViewModel mViewModel;
        public ErrandsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    FragmentErrandsBinding binding;
    private NavController navController;
    private DrawerLayout mDrawerLayout;
    private List<Errand> errandList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_errands,container,false);
        mViewModel = ViewModelProviders.of(requireActivity()).get(ErrandsViewModel.class);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(requireActivity());
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
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

        initErrands();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.errandRecyclerview.setLayoutManager(layoutManager);
        ErrandAdapter adapter = new ErrandAdapter(errandList);
        binding.errandRecyclerview.setAdapter(adapter);

        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        adapter.setOnItemClickListener(new ErrandAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, Errand data) {
                new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        //Toast.makeText(getActivity(),"我是item",Toast.LENGTH_SHORT).show();
                        NavController controller = Navigation.findNavController(view);
                        controller.navigate(R.id.action_errandsFragment_to_errandsDetailFragment);
                        mViewModel.setErrand(data);
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, 500);
            }
        });
        return binding.getRoot();

        //return inflater.inflate(R.layout.fragment_errands, container, false);
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
    private void initErrands() {
        for (int i = 0; i < 2; i++) {
            Errand e1 = new Errand(R.drawable.head1,"Bob","快递","南区" +
                    "菜鸟驿站拿两个快递","下午 6:00","2￥");
            errandList.add(e1);
            Errand e2 = new Errand(R.drawable.head2,"Tom","外卖","西门外" +
                    "卖可帮忙拿到陶园吗","上午 9:00","1￥");
            errandList.add(e2);
            Errand e3 = new Errand(R.drawable.head3,"Jack","水果","西门" +
                    "水果店代买",
                    "下午14:00", "2￥");
            errandList.add(e3);
        }
    }
}