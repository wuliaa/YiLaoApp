package com.example.yilaoapp.ui.purchase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.bean.All_orders;

public class PurchaseViewModel extends ViewModel {
    private final MutableLiveData<All_orders> purchase=new MutableLiveData<All_orders>();

    public void setPurchase(All_orders item){purchase.setValue(item);}
    public LiveData<All_orders> getPurchase(){return purchase;}

}
