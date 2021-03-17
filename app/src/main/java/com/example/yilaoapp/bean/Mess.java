package com.example.yilaoapp.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigInteger;

@Entity(tableName = "chat_table")
public class Mess {
    @PrimaryKey
    int id;
    String content;
    String from_user;
    String to_user;
    String send_at;
    String type;

    public Mess(int id, String content, String from_user, String to_user, String send_at, String type) {
        this.id = id;
        this.content = content;
        this.from_user = from_user;
        this.to_user = to_user;
        this.send_at = send_at;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public String getSend_at() {
        return send_at;
    }

    public void setSend_at(String send_at) {
        this.send_at = send_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
