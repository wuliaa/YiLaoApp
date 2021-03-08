package com.example.yilaoapp.ui.errands;

public class Errand {
    private int imageId;
    private String address;
    private String content;
    private String time;
    private String money;

    public Errand(int imageId, String address, String content, String time, String money) {
        this.imageId = imageId;
        this.address = address;
        this.content = content;
        this.time = time;
        this.money = money;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String objectName) {
        this.address = address;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
