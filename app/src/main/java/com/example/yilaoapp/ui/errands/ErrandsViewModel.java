package com.example.yilaoapp.ui.errands;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.ui.bulletin.Lost;


public class ErrandsViewModel extends ViewModel {
    private final MutableLiveData<Errand> errand=new MutableLiveData<Errand>();
    public void setErrand(Errand item){errand.setValue(item);}
    public LiveData<Errand> getErrand(){return errand;}
}
