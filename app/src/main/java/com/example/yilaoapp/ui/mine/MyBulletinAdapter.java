package com.example.yilaoapp.ui.mine;

import android.annotation.SuppressLint;
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
import java.util.StringTokenizer;

public class MyBulletinAdapter extends  RecyclerView.Adapter<MyBulletinAdapter.MyBulletinviewHolder> {
    private List<All_orders> mbulletinList = new ArrayList<>();
    public MyBulletinAdapter(List<All_orders> BulletinList) {
        mbulletinList = BulletinList;
    }
    @NonNull
    @Override
    public MyBulletinAdapter.MyBulletinviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_my_bulletin,parent,false);
        return new MyBulletinAdapter.MyBulletinviewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyBulletinAdapter.MyBulletinviewHolder holder, int position) {
        All_orders bulletin = mbulletinList.get(position);
        String time="";
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                .append(bulletin.getPhone())
                .append("/resources/")
                .append(bulletin.getId_photo());
        String url=stringBuilder.toString();
        Glide.with(MyApplication.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.head1)
                .error(R.drawable.head2)
                .into(holder.photo);
        holder.objectName.setText(bulletin.getName());
        holder.content.setText(bulletin.getDetail());
        if(bulletin.getCreate_at()!=null)
            time=bulletin.getCreate_at().split("T")[0];
        else
            time="时间丢失了";
        holder.time.setText("发布时间："+time);
        holder.time.setText(bulletin.getCreate_at());
        holder.whatBulletin.setText(bulletin.getCategory());
    }

    @Override
    public int getItemCount() {
        return mbulletinList.size();
    }

    class MyBulletinviewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView objectName,time;
        Chip  whatBulletin;
        ExpandTextView content;
        public MyBulletinviewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.MyBulletinPhoto);
            objectName=itemView.findViewById(R.id.MyBulletinName);
            content= (ExpandTextView)itemView.findViewById(R.id.MyBulletinExText);
            time=itemView.findViewById(R.id.MyBulletintime);
            whatBulletin=itemView.findViewById(R.id.MyBulletinchip);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mbulletinList.get(getLayoutPosition()));
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
    private MyBulletinAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(MyBulletinAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
