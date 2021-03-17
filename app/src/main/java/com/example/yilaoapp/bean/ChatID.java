package com.example.yilaoapp.bean;

public class ChatID {
    int id;
    String send_at;

    public ChatID(int id, String send_at) {
        this.id = id;
        this.send_at = send_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSend_at() {
        return send_at;
    }

    public void setSend_at(String send_at) {
        this.send_at = send_at;
    }
}
