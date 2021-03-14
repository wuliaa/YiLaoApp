package com.example.yilaoapp.ui.errands;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.yilaoapp.MyApplication;
import com.example.yilaoapp.R;
import com.example.yilaoapp.bean.All_orders;
import com.lcodecore.extextview.ExpandTextView;
import com.robertlevonyan.views.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.cache.DiskLruCache;

public class ErrandAdapter extends RecyclerView.Adapter<ErrandAdapter.ErrandViewHolder> {
    private List<All_orders> mErrandList = new ArrayList<>();
    public ErrandAdapter(List<All_orders> errandList) {
        mErrandList = errandList;
    }

    @NonNull
    @Override
    public ErrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_errand,parent,false);
        return new ErrandAdapter.ErrandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ErrandViewHolder holder, int position) {
        All_orders errand = mErrandList.get(position);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                .append(errand.getFrom_user())
                .append("/resources/")
                .append(errand.getId_photo());
        String url=stringBuilder.toString();
        Glide.with(MyApplication.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.head1)
                .error(R.drawable.head2)
                .into(holder.head);
        holder.address.setText(errand.getDestination().getName());
        holder.content.setText(errand.getDetail());
        holder.time.setText(errand.getCreate_at());
        holder.money.setText(String.valueOf(errand.getReward()));
    }

    @Override
    public int getItemCount() {
        return mErrandList.size();
    }

    public List<All_orders> getItems() {
        return mErrandList;
    }

    class ErrandViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView address,time;
        Chip money;
        ExpandTextView content;
        Button getTask;
        public ErrandViewHolder(@NonNull View itemView) {
            super(itemView);
            head=itemView.findViewById(R.id.errandPhoto);
            address=itemView.findViewById(R.id.errandAddress);
            content= (ExpandTextView)itemView.findViewById(R.id.errandExText);
            time=itemView.findViewById(R.id.errandTime);
            money=itemView.findViewById(R.id.errandchip);
            getTask=itemView.findViewById(R.id.errandgetTask);
            getTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mErrandList.get(getLayoutPosition()),getLayoutPosition());
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
        public void OnItemClick(View view, All_orders data,int position);

    }
    //需要外部访问，所以需要设置set方法，方便调用
    private ErrandAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(ErrandAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
