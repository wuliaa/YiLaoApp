package com.example.yilaoapp.ui.errands;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.bean.All_orders;

import java.util.Date;


public class ErrandsViewModel extends ViewModel {
    private int  flag=1;
    private Date date=null;
    private final MutableLiveData<All_orders> errand=new MutableLiveData<All_orders>();
    public void setErrand(All_orders item){errand.setValue(item);}
    public LiveData<All_orders> getErrand(){return errand;}

    public int getFlag() {
        return flag;
    }
    public void changeFlag() {
        this.flag++ ;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
