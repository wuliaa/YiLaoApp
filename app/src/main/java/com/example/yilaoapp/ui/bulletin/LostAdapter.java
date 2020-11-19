package com.example.yilaoapp.ui.bulletin;

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

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.LostViewHolder> {
    private List<Lost> mLostList = new ArrayList<>();
    public LostAdapter(List<Lost> LostList) {
        mLostList = LostList;
    }
    @NonNull
    @Override
    public LostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_lost,parent,false);
        return new LostAdapter.LostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LostViewHolder holder, int position) {
        Lost lost = mLostList.get(position);
        holder.photo.setImageResource(lost.getImageId());
        holder.objectName.setText(lost.getObjectName());
        holder.content.setText(lost.getContent());
        holder.time.setText(lost.getTime());
        holder.address.setText(lost.getAddress());
    }

    @Override
    public int getItemCount() {
        return mLostList.size();
    }

    class LostViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView objectName,time;
        Chip address;
        ExpandTextView content;
        public LostViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.lostPhoto);
            objectName=itemView.findViewById(R.id.lostname);
            content= (ExpandTextView)itemView.findViewById(R.id.lostExText);
            time=itemView.findViewById(R.id.losttime);
            address=itemView.findViewById(R.id.chip);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mLostList.get(getLayoutPosition()));
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
        public void OnItemClick(View view, Lost data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private LostAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(LostAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
