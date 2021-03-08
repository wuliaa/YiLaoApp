package com.example.yilaoapp.bean;

import java.math.BigInteger;

public class pur_order {
    BigInteger phone;
    String type;
    String detail;
    Point_address destination;
    float reward;
    String photos;

    public pur_order(BigInteger phone, String type, String detail, Point_address destination, float reward, String id_photo) {
        this.phone = phone;
        this.type = type;
        this.detail = detail;
        this.destination = destination;
        this.reward = reward;
        this.photos = id_photo;
    }

    public BigInteger getPhone() {
        return phone;
    }

    public void setPhone(BigInteger phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Point_address getDestination() {
        return destination;
    }

    public void setDestination(Point_address destination) {
        this.destination = destination;
    }

    public float getReward() {
        return reward;
    }

    public void setReward(float reward) {
        this.reward = reward;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }
}
