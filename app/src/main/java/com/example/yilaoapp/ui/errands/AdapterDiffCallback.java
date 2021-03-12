package com.example.yilaoapp.ui.errands;

import androidx.recyclerview.widget.DiffUtil;

import com.example.yilaoapp.bean.All_orders;

import java.util.List;

public class AdapterDiffCallback extends DiffUtil.Callback {
    List<All_orders> news;
    List<All_orders> olds;

    public AdapterDiffCallback(List<All_orders> news, List<All_orders> olds) {
        this.news = news;
        this.olds = olds;
    }

    @Override
    public int getOldListSize() {
        return olds.size();
    }

    @Override
    public int getNewListSize() {
        return news.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return olds.get(oldItemPosition).getClass().equals(news.get(newItemPosition).getClass());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        All_orders old = olds.get(oldItemPosition);
        All_orders New = news.get(newItemPosition);
        return old.equals(New);
    }
}
