package com.example.yilaoapp.ui.bulletin;

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


import java.util.ArrayList;
import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private List<All_orders> mShareList = new ArrayList<>();
    public ShareAdapter(List<All_orders> shareList) {
        mShareList = shareList;
    }

    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_share,parent,false);
        return new ShareAdapter.ShareViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder holder, int position) {
        All_orders share = mShareList.get(position);
        String url="http://api.yilao.tk:15000/v1.0/users/"+share.getPhone()+
                "/resources/"+share.getId_photo();
        Glide.with(MyApplication.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.head1)
                .error(R.drawable.head2)
                .into(holder.photo);
        holder.objectName.setText(share.getName());
        holder.content.setText(share.getDetail());
        holder.time.setText(share.getCreate_at());
    }

    @Override
    public int getItemCount() {
        return mShareList.size();
    }

    class ShareViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView objectName,time;
        ExpandTextView content;
        public ShareViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.sharePhoto);
            objectName=itemView.findViewById(R.id.shareName);
            content= (ExpandTextView)itemView.findViewById(R.id.ExText);
            time=itemView.findViewById(R.id.shareTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mShareList.get(getLayoutPosition()));
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
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
