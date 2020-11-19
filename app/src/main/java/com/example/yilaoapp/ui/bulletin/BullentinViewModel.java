package com.example.yilaoapp.ui.bulletin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BullentinViewModel extends ViewModel {
    private final MutableLiveData<Share> selected = new MutableLiveData<Share>();
    private final MutableLiveData<Lost> lost=new MutableLiveData<Lost>();
    private final MutableLiveData<Integer> photoId=new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> lostphotoId=new MutableLiveData<Integer>();
    private final MutableLiveData<Team> team=new MutableLiveData<Team>();
    public void select(Share item) {
        selected.setValue(item);
    }
    public LiveData<Share> getSelected() {
        return selected;
    }
    public void setLost(Lost item){lost.setValue(item);}
    public LiveData<Lost> getLost(){return lost;}
    public void setPhotoId(Integer item){photoId.setValue(item);}
    public LiveData<Integer> getPhotoId(){return photoId;}
    public void setLostPhotoId(Integer item){lostphotoId.setValue(item);}
    public LiveData<Integer> getLostPhotoId(){return lostphotoId;}
    public void setTeam(Team item){team.setValue(item);}
    public LiveData<Team> getTeam(){return team;}
}
