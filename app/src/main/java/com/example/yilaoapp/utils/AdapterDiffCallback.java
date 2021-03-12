package com.example.yilaoapp.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.example.yilaoapp.bean.All_orders;

import java.util.List;

public class AdapterDiffCallback<T> extends DiffUtil.Callback {
    List<T> news;
    List<T> olds;

    public AdapterDiffCallback(List<T> news, List<T> olds) {
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
        T old = olds.get(oldItemPosition);
        T New = news.get(newItemPosition);
        return old.equals(New);
    }
}
