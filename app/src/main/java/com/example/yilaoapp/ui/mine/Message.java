package com.example.yilaoapp.ui.mine;

public class Message {
    private String nick;
    private String content;
    private String time;
    private int imageId;

    public Message(String nick, String content, String time, int imageId) {
        this.nick = nick;
        this.content = content;
        this.time = time;
        this.imageId = imageId;
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
