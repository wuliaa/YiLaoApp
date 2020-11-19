package com.example.yilaoapp.ui.mine;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentAddressBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentAddressBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_address,container,false);
        //binding.setData(mineViewModel);
        binding.setLifecycleOwner(requireActivity());
        binding.aToolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        binding.aToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
                //controller.navigate(R.id.action_myInformationFragment_to_changeNickFragment);
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_address, container, false);
    }
}