package com.example.yilaoapp.bean;

public class chat_user {
    String from_user;
    String to_user;
    String id_photo;
    String id_name;
    String last_send_at;
    String last_content;
    String type;

    public chat_user(String from_user,String to_user, String id_photo, String id_name, String last_send_at, String last_content,String type) {
        this.from_user = from_user;
        this.to_user=to_user;
        this.id_photo = id_photo;
        this.id_name = id_name;
        this.last_send_at = last_send_at;
        this.last_content = last_content;
        this.type=type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getLast_send_at() {
        return last_send_at;
    }

    public void setLast_send_at(String last_send_at) {
        this.last_send_at = last_send_at;
    }

    public String getLast_content() {
        return last_content;
    }

    public void setLast_content(String last_content) {
        this.last_content = last_content;
    }
}
