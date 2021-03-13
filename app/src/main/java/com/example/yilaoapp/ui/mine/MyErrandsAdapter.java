package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yilaoapp.MyApplication;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.lcodecore.extextview.ExpandTextView;
import com.robertlevonyan.views.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class MyErrandsAdapter extends  RecyclerView.Adapter<MyErrandsAdapter.MyErrandsviewHolder> {
    private List<All_orders> merrandsList = new ArrayList<>();
    public MyErrandsAdapter(List<All_orders> ErrandsList) {
        merrandsList = ErrandsList;
    }
    @NonNull
    @Override
    public MyErrandsAdapter.MyErrandsviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_my_errands,parent,false);
        return new MyErrandsAdapter.MyErrandsviewHolder(itemView);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyErrandsAdapter.MyErrandsviewHolder holder, int position) {
        All_orders errands = merrandsList.get(position);
        String time="";
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                .append(errands.getPhone())
                .append("/resources/")
                .append(errands.getId_photo());
        String url=stringBuilder.toString();
        Glide.with(MyApplication.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.head1)
                .error(R.drawable.head2)
                .into(holder.photo);
        holder.objectName.setText(errands.getName());
        holder.content.setText(errands.getDetail());
        if(errands.getCreate_at()!=null)
            time=errands.getCreate_at().split("T")[0];
        else
            time="时间丢失了";
        holder.time.setText("发布时间："+time);
        SharedPreferences pre2 = MyApplication.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String mobile2 = pre2.getString("mobile", "");
        if(mobile2.equals(String.valueOf(errands.getPhone())))
            holder.isPublish.setText("发布的任务");
        else
            holder.isPublish.setText("领取的任务");
    }

    @Override
    public int getItemCount() {
        return merrandsList.size();
    }

    class MyErrandsviewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView objectName,time;
        Chip  isPublish;
        ExpandTextView content;
        public MyErrandsviewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.errandsPhoto);
            objectName=itemView.findViewById(R.id.errandsname);
            content= (ExpandTextView)itemView.findViewById(R.id.errandsExText);
            time=itemView.findViewById(R.id.MyErandtime);
            isPublish=itemView.findViewById(R.id.chip);
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
        public void OnItemClick(View view, All_orders data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private MyErrandsAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(MyErrandsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
