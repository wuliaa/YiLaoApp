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

public class MyBulletinAdapter extends  RecyclerView.Adapter<MyBulletinAdapter.MyBulletinviewHolder> {
    private List<MyBulletin> mbulletinList = new ArrayList<>();
    public MyBulletinAdapter(List<MyBulletin> BulletinList) {
        mbulletinList = BulletinList;
    }
    @NonNull
    @Override
    public MyBulletinAdapter.MyBulletinviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_my_bulletin,parent,false);
        return new MyBulletinAdapter.MyBulletinviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBulletinAdapter.MyBulletinviewHolder holder, int position) {
        MyBulletin bulletin = mbulletinList.get(position);
        holder.photo.setImageResource(bulletin.getImageId()[0]);
        holder.objectName.setText(bulletin.getObjectName());
        holder.content.setText(bulletin.getContent());
        holder.time.setText(bulletin.getTime());
        holder.whatBulletin.setText(bulletin.getWhatBulletin());
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
        public void OnItemClick(View view, MyBulletin data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private MyBulletinAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(MyBulletinAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
