package com.example.yilaoapp.ui.mine;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentMyTaskBinding;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTaskFragment extends Fragment {

    public MyTaskFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentMyTaskBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_task,container,false);
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
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add(R.string.Errands, MyErrandsFragment.class)
                .add(R.string.Purchase, MyPurchaseFragment.class)
                .add(R.string.Bulletin, MyBulletinFragment.class)
                .create());
        binding.viewpager.setAdapter(adapter);
        binding.viewpagertab.setViewPager(binding.viewpager);

        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_my_task, container, false);
    }
}