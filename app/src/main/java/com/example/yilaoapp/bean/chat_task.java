package com.example.yilaoapp.bean;

import java.math.BigInteger;

public class chat_task {
    String content;
    BigInteger to_user;

    public chat_task(String content, BigInteger to_user) {
        this.content = content;
        this.to_user = to_user;
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
