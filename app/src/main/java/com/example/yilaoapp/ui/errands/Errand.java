package com.example.yilaoapp.ui.errands;

public class Errand {
    private int imageId;
    private String nickName;
    private String objectName;
    private String content;
    private String time;
    private String money;

    public Errand(int imageId, String nickName, String objectName, String content, String time, String money) {
        this.imageId = imageId;
        this.nickName = nickName;
        this.objectName = objectName;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
