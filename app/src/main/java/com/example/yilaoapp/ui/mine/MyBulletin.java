package com.example.yilaoapp.ui.mine;

public class MyBulletin {
    private String objectName;
    private String content;
    private String time;
    private String address;
    private String whatBulletin;
    private int[] imageId;

    public MyBulletin(String objectName, String content, String time, String address,
                     String whatBulletin,int []imageId) {
        this.objectName = objectName;
        this.content = content;
        this.time = time;
        this.address = address;
        this.whatBulletin=whatBulletin;
        this.imageId = imageId;
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

    public int[] getImageId() {
        return imageId;
    }

    public void setImageId(int[] imageId) {
        this.imageId = imageId;
    }
}
