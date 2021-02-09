package com.example.yilaoapp.bean;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Set;

public class Token {
    private int id;
    private BigInteger user;  //user_id
    private String appid;
    private String hex;
    //private Set<> privilege;
    private Timestamp deadline;

    public Token(int id, BigInteger user, String appid,
                 String hex, Timestamp deadline) {
        this.id = id;
        this.user = user;
        this.appid = appid;
        this.hex = hex;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigInteger getUser() {
        return user;
    }

    public void setUser(BigInteger user) {
        this.user = user;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }
}
