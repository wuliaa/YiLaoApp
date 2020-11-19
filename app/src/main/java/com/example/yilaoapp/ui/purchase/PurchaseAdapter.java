package com.example.yilaoapp.ui.purchase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yilaoapp.R;
import com.lcodecore.extextview.ExpandTextView;
import com.robertlevonyan.views.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {
    private List<Purchase> mpurchaseList = new ArrayList<>();
    public PurchaseAdapter(List<Purchase> PurchaseList) {
        mpurchaseList = PurchaseList;
    }
    @NonNull
    @Override
    public PurchaseAdapter.PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_purchase,parent,false);
        return new PurchaseAdapter.PurchaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.PurchaseViewHolder holder, int position) {
        Purchase purchase = mpurchaseList.get(position);
        holder.photo.setImageResource(purchase.getImageId()[0]);
        holder.objectName.setText(purchase.getObjectName());
        holder.content.setText(purchase.getContent());
        holder.money.setText(purchase.getMoney());
        holder.isPurchase.setText(purchase.getIsPurchase());
    }

    @Override
    public int getItemCount() {
        return mpurchaseList.size();
    }

    class PurchaseViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView objectName,money;
        Chip isPurchase;
        ExpandTextView content;
        public PurchaseViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.purchasePhoto);
            objectName=itemView.findViewById(R.id.purchasename);
            content= (ExpandTextView)itemView.findViewById(R.id.purchaseExText);
            money=itemView.findViewById(R.id.money);
            isPurchase=itemView.findViewById(R.id.chip);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mpurchaseList.get(getLayoutPosition()));
                    }
                }
            });
        }
    }
    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         */
        public void OnItemClick(View view, Purchase data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private PurchaseAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(PurchaseAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
