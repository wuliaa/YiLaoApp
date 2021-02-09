package com.example.yilaoapp.ui.purchase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PurchaseViewModel extends ViewModel {
    private final MutableLiveData<Purchase> purchase=new MutableLiveData<Purchase>();
    private final MutableLiveData<Integer> PurchasephotoId=new MutableLiveData<Integer>();
    public void setPurchase(Purchase item){purchase.setValue(item);}
    public LiveData<Purchase> getPurchase(){return purchase;}
    public void setPurchasePhotoId(Integer item){PurchasephotoId.setValue(item);}
    public LiveData<Integer> getPurchasePhotoId(){return PurchasephotoId;}
}
