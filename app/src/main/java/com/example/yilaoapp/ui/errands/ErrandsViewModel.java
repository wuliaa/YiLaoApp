package com.example.yilaoapp.ui.errands;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.bean.All_orders;
import com.example.yilaoapp.ui.bulletin.Lost;


public class ErrandsViewModel extends ViewModel {
    private final MutableLiveData<All_orders> errand=new MutableLiveData<All_orders>();
    public void setErrand(All_orders item){errand.setValue(item);}
    public LiveData<All_orders> getErrand(){return errand;}
}
