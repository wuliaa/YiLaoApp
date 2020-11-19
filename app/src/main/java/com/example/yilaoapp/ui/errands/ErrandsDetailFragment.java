package com.example.yilaoapp.ui.errands;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentErrandsDetailBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class ErrandsDetailFragment extends Fragment {

    public ErrandsDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentErrandsDetailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_errands_detail,container,false);
        ErrandsViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ErrandsViewModel.class);
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
        viewModel.getErrand().observe(getViewLifecycleOwner(), item -> {
            binding.erranddcontent.setText(item.getContent());
            binding.erranddHead.setImageResource(item.getImageId());
            binding.erranddtime.setText(item.getTime());
            binding.erranddchip.setText(item.getMoney());
            binding.erranddname.setText(item.getNickName());
            binding.erranddobject.setText(item.getObjectName());
        });
        return binding.getRoot();
    }
}