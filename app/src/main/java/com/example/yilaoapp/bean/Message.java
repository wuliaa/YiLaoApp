package com.example.yilaoapp.bean;

public class Message {
    private String nick;
    private String content;
    private String time;
    private String uuid;
    private String mobile;

    public Message(String nick, String content, String time, String uuid, String mobile) {
        this.nick = nick;
        this.content = content;
        this.time = time;
        this.uuid = uuid;
        this.mobile = mobile;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}