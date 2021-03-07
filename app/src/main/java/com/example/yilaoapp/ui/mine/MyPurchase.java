package com.example.yilaoapp.ui.mine;

import java.util.ArrayList;

public class MyPurchase {
    private String objectName;
    private String content;
    private String money;
    private String isPurchase;
    private String isPublish;
    private ArrayList<String> photos;
    private int imageid;  //头像

    public MyPurchase(String objectName, String content, String money, String isPurchase,
                      String isPublish,ArrayList<String> photos,int imageid) {
        this.objectName = objectName;
        this.content = content;
        this.money = money;
        this.isPurchase = isPurchase;
        this.isPublish=isPublish;
        this.photos = photos;
        this.imageid=imageid;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIsPurchase() {
        return isPurchase;
    }

    public void setIsPurchase(String isPurchase) {
        this.isPurchase = isPurchase;
    }

    public String getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(String isPublish) {
        this.isPublish = isPublish;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }
}
