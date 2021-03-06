package com.example.yilaoapp.bean;

import java.math.BigInteger;

public class chat {
    String content;
    BigInteger from_user;
    BigInteger to_user;
    public chat(String content, BigInteger from_user, BigInteger to_user) {
        this.content = content;
        this.from_user = from_user;
        this.to_user = to_user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigInteger getFrom_user() {
        return from_user;
    }

    public void setFrom_user(BigInteger from_user) {
        this.from_user = from_user;
    }

    public BigInteger getTo_user() {
        return to_user;
    }

    public void setTo_user(BigInteger to_user) {
        this.to_user = to_user;
    }
}
