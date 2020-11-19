package com.example.yilaoapp.ui.purchase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentPurchaseDetailBinding;
import com.github.siyamed.shapeimageview.RoundedImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseDetailFragment extends Fragment {

    public PurchaseDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentPurchaseDetailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_purchase_detail,container,false);
        PurchaseViewModel viewModel = ViewModelProviders.of(requireActivity()).get( PurchaseViewModel.class);
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
            binding.purchasedcontent.setText(item.getContent());
            //binding.purchasedphoto.setImageResource(item.getImageId());
            binding.purchasedmoney.setText("金额："+item.getMoney());
            binding.purchasedchip.setText(item.getIsPurchase() );
            int []imageid=item.getImageId();
            for(int i=0;i<imageid.length;i++)
            {
                RoundedImageView view =new RoundedImageView(container.getContext());
                view.setImageResource(imageid[i]);
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//                params.height =200;//要设置的高度
//                params.width =200;//要设置的宽度
//                view.setLayoutParams(params);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(600, 600);
                if(i==0) lp.setMargins(0,0,0,40);
                else lp.setMargins(0,40,0,40);
                view.setLayoutParams(lp);
                binding.imageGallery.addView(view);
            }
        });
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_purchase_detail, container, false);
    }
}