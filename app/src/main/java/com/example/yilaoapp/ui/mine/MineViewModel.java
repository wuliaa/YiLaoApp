package com.example.yilaoapp.ui.mine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.yilaoapp.bean.User;

public class MineViewModel extends ViewModel {
    private final MutableLiveData<MyPurchase> mypurchase=new MutableLiveData<MyPurchase>();
    private final MutableLiveData<MyErrands> myerrands=new MutableLiveData<MyErrands>();
    private final MutableLiveData<MyBulletin> mybulletin=new MutableLiveData<MyBulletin>();
    private final MutableLiveData<Integer> MinephotoId=new MutableLiveData<Integer>();
    private final MutableLiveData<User> User = new MutableLiveData<User>();

    public void setPurchase(MyPurchase item){mypurchase.setValue(item);}
    public void setErrands(MyErrands item){myerrands.setValue(item);}
    public void setBulletin (MyBulletin item){mybulletin.setValue(item);}
    public LiveData<MyPurchase> getPurchase(){return mypurchase;}
    public LiveData<MyErrands> getErrands(){return myerrands;}
    public LiveData<MyBulletin> getBulletin (){return mybulletin;}
    public void setMinephotoId(Integer item){MinephotoId.setValue(item);}
    public LiveData<Integer> getMinePhotoId(){return MinephotoId;}

    public void setUser(User user){User.setValue(user);}
    public LiveData<User>getUser(){return User;}
}