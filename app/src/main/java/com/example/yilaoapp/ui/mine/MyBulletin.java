package com.example.yilaoapp.ui.mine;

import java.util.ArrayList;

public class MyBulletin {
    private String objectName;
    private String content;
    private String time;
    private String address;
    private String whatBulletin;
    private ArrayList<String> photos;
    private int imageid;

    public MyBulletin(String objectName, String content, String time, String address,
                     String whatBulletin,ArrayList<String> photos,int imageid) {
        this.objectName = objectName;
        this.content = content;
        this.time = time;
        this.address = address;
        this.whatBulletin=whatBulletin;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWhatBulletin() {
        return whatBulletin;
    }

    public void setWhatBulletin(String whatBulletin) {
        this.whatBulletin = whatBulletin;
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
