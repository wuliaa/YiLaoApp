package com.example.yilaoapp.ui.mine;

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

public class MyErrandsAdapter extends  RecyclerView.Adapter<MyErrandsAdapter.MyErrandsviewHolder> {
    private List<MyErrands> merrandsList = new ArrayList<>();
    public MyErrandsAdapter(List<MyErrands> ErrandsList) {
        merrandsList = ErrandsList;
    }
    @NonNull
    @Override
    public MyErrandsAdapter.MyErrandsviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_my_errands,parent,false);
        return new MyErrandsAdapter.MyErrandsviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyErrandsAdapter.MyErrandsviewHolder holder, int position) {
        MyErrands errands = merrandsList.get(position);
        holder.photo.setImageResource(errands.getImageid());
        holder.objectName.setText(errands.getObjectName());
        holder.content.setText(errands.getContent());
        holder.money.setText(errands.getMoney());
        holder.isPublish.setText(errands.getIsPublish());
        holder.isErrands.setText(errands.getIsErrands());
    }

    @Override
    public int getItemCount() {
        return merrandsList.size();
    }

    class MyErrandsviewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView objectName,money;
        Chip isErrands,isPublish;
        ExpandTextView content;
        public MyErrandsviewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.errandsPhoto);
            objectName=itemView.findViewById(R.id.errandsname);
            content= (ExpandTextView)itemView.findViewById(R.id.errandsExText);
            money=itemView.findViewById(R.id.money);
            isPublish=itemView.findViewById(R.id.chip);
            isErrands=itemView.findViewById(R.id.chip2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, merrandsList.get(getLayoutPosition()));
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
        public void OnItemClick(View view, MyErrands data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private MyErrandsAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(MyErrandsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
