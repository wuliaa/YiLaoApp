package com.example.yilaoapp.bean;

public class chat_user {
    String mobile;
    String id_photo;
    String id_name;
    String last_send_at;
    String last_content;

    public chat_user(String mobile, String id_photo, String id_name, String last_send_at, String last_content) {
        this.mobile = mobile;
        this.id_photo = id_photo;
        this.id_name = id_name;
        this.last_send_at = last_send_at;
        this.last_content = last_content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
