package com.example.yilaoapp.ui.mine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.bean.All_orders;

public class MineViewModel extends ViewModel {
    private final MutableLiveData<All_orders> mypurchase=new MutableLiveData<All_orders>();
    private final MutableLiveData<All_orders> myerrands=new MutableLiveData<All_orders>();
    private final MutableLiveData<All_orders> mybulletin=new MutableLiveData<All_orders>();

    public void setPurchase(All_orders item){mypurchase.setValue(item);}
    public void setErrands(All_orders item){myerrands.setValue(item);}
    public void setBulletin (All_orders item){mybulletin.setValue(item);}
    public LiveData<All_orders> getPurchase(){return mypurchase;}
    public LiveData<All_orders> getErrands(){return myerrands;}
    public LiveData<All_orders> getBulletin (){return mybulletin;}
}