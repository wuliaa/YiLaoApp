package com.example.yilaoapp.ui.mine;

public class MyErrands {
    private String objectName;
    private String content;
    private String money;
    private String isErrands;
    private String isPublish;
    private int[] imageId;

    public MyErrands(String objectName, String content, String money, String isErrands,
                      String isPublish,int []imageId) {
        this.objectName = objectName;
        this.content = content;
        this.money = money;
        this.isErrands = isErrands;
        this.isPublish=isPublish;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIsErrands() {
        return isErrands;
    }

    public void setIsErrands(String isPurchase) {
        this.isErrands = isPurchase;
    }

    public String getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(String isPublish) {
        this.isPublish = isPublish;
    }

    public int[] getImageId() {
        return imageId;
    }

    public void setImageId(int[] imageId) {
        this.imageId = imageId;
    }

}
