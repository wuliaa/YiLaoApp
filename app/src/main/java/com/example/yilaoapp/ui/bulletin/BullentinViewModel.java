package com.example.yilaoapp.ui.bulletin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.bean.All_orders;

public class BullentinViewModel extends ViewModel {
    private final MutableLiveData<All_orders> selected = new MutableLiveData<All_orders>();
    private final MutableLiveData<All_orders> lost=new MutableLiveData<All_orders>();
    private final MutableLiveData<All_orders> team=new MutableLiveData<All_orders>();
    public void select(All_orders item) {
        selected.setValue(item);
    }
    public LiveData<All_orders> getSelected() {
        return selected;
    }
    public void setLost(All_orders item){lost.setValue(item);}
    public LiveData<All_orders> getLost(){return lost;}
    public void setTeam(All_orders item){team.setValue(item);}
    public LiveData<All_orders> getTeam(){return team;}
}
