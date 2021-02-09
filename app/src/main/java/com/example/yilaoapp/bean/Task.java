package com.example.yilaoapp.bean;

import org.w3c.dom.Text;

import java.sql.Timestamp;

public class Task {
    private int id;
    private String name; //任务名字，跑腿，代购，公告
    private Text detail;
    private Text protected_info;
    private int photo;  //photo_id
    private int order;  //order_id
    private boolean tangible;
    private String address;
    private int count;
    private double reward; //decimal(16,2)
    private Timestamp in_at;
    private Timestamp out_at;

    public Task(int id, String name, Text detail, Text protected_info,
                int photo, int order, boolean tangible, String address,
                int count, double reward, Timestamp in_at, Timestamp out_at) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.protected_info = protected_info;
        this.photo = photo;
        this.order = order;
        this.tangible = tangible;
        this.address = address;
        this.count = count;
        this.reward = reward;
        this.in_at = in_at;
        this.out_at = out_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Text getDetail() {
        return detail;
    }

    public void setDetail(Text detail) {
        this.detail = detail;
    }

    public Text getProtected_info() {
        return protected_info;
    }

    public void setProtected_info(Text protected_info) {
        this.protected_info = protected_info;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isTangible() {
        return tangible;
    }

    public void setTangible(boolean tangible) {
        this.tangible = tangible;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public Timestamp getIn_at() {
        return in_at;
    }

    public void setIn_at(Timestamp in_at) {
        this.in_at = in_at;
    }

    public Timestamp getOut_at() {
        return out_at;
    }

    public void setOut_at(Timestamp out_at) {
        this.out_at = out_at;
    }
}
