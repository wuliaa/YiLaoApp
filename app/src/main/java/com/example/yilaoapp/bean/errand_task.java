package com.example.yilaoapp.bean;

public class errand_task {
    String type;
    String detail;
    String destination;
    String reward;

    public errand_task(String type, String detail, String destination, String reward) {
        this.type = type;
        this.detail = detail;
        this.destination = destination;
        this.reward = reward;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
