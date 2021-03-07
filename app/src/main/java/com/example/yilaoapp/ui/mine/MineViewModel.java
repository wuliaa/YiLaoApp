package com.example.yilaoapp.ui.mine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MineViewModel extends ViewModel {
    private final MutableLiveData<MyPurchase> mypurchase=new MutableLiveData<MyPurchase>();
    private final MutableLiveData<MyErrands> myerrands=new MutableLiveData<MyErrands>();
    private final MutableLiveData<MyBulletin> mybulletin=new MutableLiveData<MyBulletin>();

    public void setPurchase(MyPurchase item){mypurchase.setValue(item);}
    public void setErrands(MyErrands item){myerrands.setValue(item);}
    public void setBulletin (MyBulletin item){mybulletin.setValue(item);}
    public LiveData<MyPurchase> getPurchase(){return mypurchase;}
    public LiveData<MyErrands> getErrands(){return myerrands;}
    public LiveData<MyBulletin> getBulletin (){return mybulletin;}
}