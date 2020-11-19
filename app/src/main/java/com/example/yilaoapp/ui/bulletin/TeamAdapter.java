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

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<Team> mTeamList = new ArrayList<>();
    public TeamAdapter(List<Team> TeamList) {
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
        Team team = mTeamList.get(position);
        holder.photo.setImageResource(team.getImageId());
        holder.activityName.setText(team.getActivityName());
        holder.content.setText(team.getContent());
        holder.time.setText(team.getTime());
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
        public void OnItemClick(View view, Team data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private TeamAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(TeamAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
