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
import java.util.StringTokenizer;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<All_orders> mTeamList = new ArrayList<All_orders>();
    public TeamAdapter(List<All_orders> TeamList) {
        mTeamList = TeamList;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_team,parent,false);
        return new TeamAdapter.TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        All_orders team = mTeamList.get(position);
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("http://api.yilao.tk:15000/v1.0/users/")
                .append(team.getFrom_user())
                .append("/resources/")
                .append(team.getId_photo());
        String url=stringBuilder.toString();
        Glide.with(MyApplication.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.head1)
                .error(R.drawable.head2)
                .into(holder.photo);
        holder.activityName.setText(team.getName());
        holder.content.setText(team.getDetail());
        holder.time.setText(team.getCreate_at().split(" ")[0]);
    }

    @Override
    public int getItemCount() {
        return mTeamList.size();
    }

    class TeamViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView activityName,time;
        ExpandTextView content;
        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.teamPhoto);
            activityName=itemView.findViewById(R.id.teamName);
            content= (ExpandTextView)itemView.findViewById(R.id.teamExText);
            time=itemView.findViewById(R.id.teamTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, mTeamList.get(getLayoutPosition()));
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
    private TeamAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(TeamAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
