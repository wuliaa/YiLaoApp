package com.example.yilaoapp.ui.bulletin;

import java.util.ArrayList;

public class Lost {
    private String objectName;
    private String content;
    private String time;
    private String address;
    private int imageId;  //作为头像存在
    private ArrayList<String> photos;

    public Lost(String objectName, String content, String time, String address, int imageId,
                ArrayList<String> photos) {
        this.objectName = objectName;
        this.content = content;
        this.time = time;
        this.address = address;
        this.imageId = imageId;
        this.photos=photos;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }
}
