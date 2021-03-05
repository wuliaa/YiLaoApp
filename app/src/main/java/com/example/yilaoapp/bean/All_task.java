package com.example.yilaoapp.bean;

public class All_task {
    String detail;
    String type;
    String in_at;
    String name;
    Point_address destination;
    float reward;
    String protected_info;
    String out_at;
    int id;
    int count;

    public All_task(String detail, String type, String in_at, String name, Point_address destination, float reward, String protected_info, String out_at, int id, int count) {
        this.detail = detail;
        this.type = type;
        this.in_at = in_at;
        this.name = name;
        this.destination = destination;
        this.reward = reward;
        this.protected_info = protected_info;
        this.out_at = out_at;
        this.id = id;
        this.count = count;
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

    public String getIn_at() {
        return in_at;
    }

    public void setIn_at(String in_at) {
        this.in_at = in_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
