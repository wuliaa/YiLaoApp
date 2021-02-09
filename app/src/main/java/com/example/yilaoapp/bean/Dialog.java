package com.example.yilaoapp.bean;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.sql.Timestamp;

//聊天实体类
public class Dialog {
    private int id;
    private Text content;
    private BigInteger from_user;
    private BigInteger to_user;
    private Timestamp send_at;

    public Dialog(int id, Text content, BigInteger from_user,
                  BigInteger to_user, Timestamp send_at) {
        this.id = id;
        this.content = content;
        this.from_user = from_user;
        this.to_user = to_user;
        this.send_at = send_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Text getContent() {
        return content;
    }

    public void setContent(Text content) {
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

    public Timestamp getSend_at() {
        return send_at;
    }

    public void setSend_at(Timestamp send_at) {
        this.send_at = send_at;
    }
}
