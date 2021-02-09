package com.example.yilaoapp.ui.errands;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yilaoapp.R;
import com.example.yilaoapp.ui.bulletin.Share;
import com.example.yilaoapp.ui.bulletin.ShareAdapter;
import com.lcodecore.extextview.ExpandTextView;
import com.robertlevonyan.views.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ErrandAdapter extends RecyclerView.Adapter<ErrandAdapter.ErrandViewHolder> {
    private List<Errand> mErrandList = new ArrayList<>();
    public ErrandAdapter(List<Errand> errandList) {
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
        Errand errand = mErrandList.get(position);
        holder.head.setImageResource(errand.getImageId());
        holder.objectName.setText(errand.getObjectName());
        holder.content.setText(errand.getContent());
        holder.time.setText(errand.getTime());
        holder.money.setText(errand.getMoney());
        holder.RemainTime.setText(errand.getRemainTime());
    }

    @Override
    public int getItemCount() {
        return mErrandList.size();
    }

    class ErrandViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView objectName,time,RemainTime;
        Chip money;
        ExpandTextView content;
        public ErrandViewHolder(@NonNull View itemView) {
            super(itemView);
            head=itemView.findViewById(R.id.errandPhoto);
            objectName=itemView.findViewById(R.id.errandObject);
            RemainTime=itemView.findViewById(R.id.errandRtime);
            content= (ExpandTextView)itemView.findViewById(R.id.errandExText);
            time=itemView.findViewById(R.id.errandTime);
            money=itemView.findViewById(R.id.errandchip);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mErrandList.get(getLayoutPosition()));
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
        public void OnItemClick(View view, Errand data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private ErrandAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(ErrandAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
