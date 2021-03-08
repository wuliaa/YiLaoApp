package com.example.yilaoapp.ui.mine;

public class MyErrands {
    private String objectName;
    private String content;
    private String money;
    private String isPublish;
    private int imageid;   //头像
    private String PhoneNumber;
    private String Address;  //收货地址

    public MyErrands(String objectName, String content, String money,
                      String isPublish,int imageid,String PhoneNumber,String Address) {
        this.objectName = objectName;
        this.content = content;
        this.money = money;
        this.isPublish=isPublish;
        this.imageid=imageid;
        this.PhoneNumber=PhoneNumber;
        this.Address=Address;
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

    public String getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(String isPublish) {
        this.isPublish = isPublish;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
