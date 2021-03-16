package com.example.yilaoapp.bean;

import java.math.BigInteger;

public class chat_task {
    String content;
    BigInteger to_user;
    String type;

    public chat_task(String content, BigInteger to_user, String type) {
        this.content = content;
        this.to_user = to_user;
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigInteger getTo_user() {
        return to_user;
    }

    public void setTo_user(BigInteger to_user) {
        this.to_user = to_user;
    }
}
