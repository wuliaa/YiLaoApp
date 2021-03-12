package com.example.yilaoapp.ui.bulletin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.bean.All_orders;

public class BullentinViewModel extends ViewModel {
    private final MutableLiveData<Share> selected = new MutableLiveData<Share>();
    private final MutableLiveData<Lost> lost=new MutableLiveData<Lost>();
    private final MutableLiveData<Team> team=new MutableLiveData<Team>();
    public void select(Share item) {
        selected.setValue(item);
    }
    public LiveData<Share> getSelected() {
        return selected;
    }
    public void setLost(Lost item){lost.setValue(item);}
    public LiveData<Lost> getLost(){return lost;}
    public void setTeam(Team item){team.setValue(item);}
    public LiveData<Team> getTeam(){return team;}
}
