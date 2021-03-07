package com.example.yilaoapp.bean;

import java.math.BigInteger;
import java.util.List;

public class All_orders {
    String receive_at;
    String close_at;
    BigInteger phone;
    BigInteger executor;
    BigInteger from_user;
    Point_address destination;
    String emergency_level;
    String close_state;
    String create_at;
    int id;
    String detail;
    String type;
    String category;
    String photos;
    String in_at;
    float reward;
    String protected_info;
    String out_at;
    int count;

    public All_orders(String receive_at, String close_at, BigInteger phone, BigInteger executor, BigInteger from_user, Point_address destination, String emergency_level, String close_state, String create_at, int id, String detail, String type, String category, String photos, String in_at, float reward, String protected_info, String out_at, int count) {
        this.receive_at = receive_at;
        this.close_at = close_at;
        this.phone = phone;
        this.executor = executor;
        this.from_user = from_user;
        this.destination = destination;
        this.emergency_level = emergency_level;
        this.close_state = close_state;
        this.create_at = create_at;
        this.id = id;
        this.detail = detail;
        this.type = type;
        this.category = category;
        this.photos = photos;
        this.in_at = in_at;
        this.reward = reward;
        this.protected_info = protected_info;
        this.out_at = out_at;
        this.count = count;
    }

    public String getReceive_at() {
        return receive_at;
    }

    public void setReceive_at(String receive_at) {
        this.receive_at = receive_at;
    }

    public String getClose_at() {
        return close_at;
    }

    public void setClose_at(String close_at) {
        this.close_at = close_at;
    }

    public BigInteger getPhone() {
        return phone;
    }

    public void setPhone(BigInteger phone) {
        this.phone = phone;
    }

    public BigInteger getExecutor() {
        return executor;
    }

    public void setExecutor(BigInteger executor) {
        this.executor = executor;
    }

    public BigInteger getFrom_user() {
        return from_user;
    }

    public void setFrom_user(BigInteger from_user) {
        this.from_user = from_user;
    }

    public Point_address getDestination() {
        return destination;
    }

    public void setDestination(Point_address destination) {
        this.destination = destination;
    }

    public String getEmergency_level() {
        return emergency_level;
    }

    public void setEmergency_level(String emergency_level) {
        this.emergency_level = emergency_level;
    }

    public String getClose_state() {
        return close_state;
    }

    public void setClose_state(String close_state) {
        this.close_state = close_state;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getIn_at() {
        return in_at;
    }

    public void setIn_at(String in_at) {
        this.in_at = in_at;
    }

    public float getReward() {
        return reward;
    }

    public void setReward(float reward) {
        this.reward = reward;
    }

    public String getProtected_info() {
        return protected_info;
    }

    public void setProtected_info(String protected_info) {
        this.protected_info = protected_info;
    }

    public String getOut_at() {
        return out_at;
    }

    public void setOut_at(String out_at) {
        this.out_at = out_at;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
