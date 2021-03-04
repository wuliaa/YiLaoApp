package com.example.yilaoapp.bean;

public class errand_task {
    String type;
    String detail;
    String destination;
    float reward;

    public errand_task(String type, String detail, String destination, float reward) {
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

    public float getReward() {
        return reward;
    }

    public void setReward(float reward) {
        this.reward = reward;
    }
}
