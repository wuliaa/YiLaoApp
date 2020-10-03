package com.example.yilaoapp.ui.errands;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yilaoapp.R;

import java.util.List;
import java.util.Map;

public class errandsadapter extends BaseAdapter {
    private List<Map<String, Object>> listdata;
    private Context mcontext;
    private LayoutInflater layoutInflater;
    public errandsadapter(List<Map<String, Object>> listdata, Context mcontext){
        this.listdata=listdata;
        this.mcontext=mcontext;
        this.layoutInflater= LayoutInflater.from(mcontext);
    }
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.listitem,null);
            holder.image=(ImageView)convertView.findViewById(R.id.im);
            holder.bt=(Button)convertView.findViewById(R.id.bt);
            holder.ed=(TextView)convertView.findViewById(R.id.ed);
            holder.lo=(TextView)convertView.findViewById(R.id.lo);
            holder.mo=(TextView)convertView.findViewById(R.id.mo);
            holder.ti=(TextView)convertView.findViewById(R.id.ti);
            holder.na=(TextView)convertView.findViewById(R.id.na);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
        holder.na.setText((String) listdata.get(position).get("name"));
        holder.ti.setText((String) listdata.get(position).get("ltime"));
        holder.mo.setText((String) listdata.get(position).get("money"));
        holder.lo.setText((String) listdata.get(position).get("lock"));
        holder.ed.setText((String) listdata.get(position).get("edit"));
        holder.bt.setText("查看");
        holder.image.setBackgroundResource((Integer)listdata.get(position).get("image"));
        return convertView;
    }
    public class ViewHolder{
        public ImageView image;
        public TextView na;
        public TextView ti;
        public TextView lo;
        public TextView mo;
        public TextView ed;
        public Button bt;
    }
}
