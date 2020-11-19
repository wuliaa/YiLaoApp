package com.example.yilaoapp.ui.mine;

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

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> mMessageList = new ArrayList<>();
    public MessageAdapter(List<Message> messagesList) {
        mMessageList = messagesList;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_message,parent,false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.head.setImageResource(message.getImageId());
        holder.nick.setText(message.getNick());
        holder.content.setText(message.getContent());
        holder.time.setText(message.getTime());
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView nick,content,time;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            head=itemView.findViewById(R.id.messageHead);
            nick=itemView.findViewById(R.id.messageNick);
            content=itemView.findViewById(R.id.messageContent);
            time=itemView.findViewById(R.id.messageTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v,mMessageList.get(getLayoutPosition()));
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
        public void OnItemClick(View view, Message data);
    }
    //需要外部访问，所以需要设置set方法，方便调用
    private MessageAdapter.OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(MessageAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
