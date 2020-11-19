package com.example.yilaoapp.ui.purchase;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yilaoapp.R;
import com.example.yilaoapp.databinding.FragmentPurchaseMessageBinding;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchaseMessageFragment extends Fragment {

    public PurchaseMessageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentPurchaseMessageBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_purchase_message,container,false);
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
        Point p = new Point();//获取窗口管理器
        WindowManager wm = (WindowManager) container.getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(p);
        int screenWidth = p.x; // 屏幕宽度
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(200,200);
        lp.setMargins(20,20,screenWidth/3,20);
        binding.addPhoto.setLayoutParams(lp);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_purchase_message, container, false);
    }
}