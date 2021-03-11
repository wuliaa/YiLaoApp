package com.example.yilaoapp.bean;

import java.math.BigInteger;

public class pur_order {
    BigInteger phone;
    String type;
    String detail;
    Point_address destination;
    String category;
    float reward;
    String photos;
    String name;

    public pur_order(BigInteger phone, String type, String detail, Point_address destination, String category, float reward, String photos, String name) {
        this.phone = phone;
        this.type = type;
        this.detail = detail;
        this.destination = destination;
        this.category = category;
        this.reward = reward;
        this.photos = photos;
        this.name = name;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
