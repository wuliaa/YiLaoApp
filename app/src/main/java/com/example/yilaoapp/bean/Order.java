package com.example.yilaoapp.bean;

import java.math.BigInteger;
import java.sql.Timestamp;

//订单实体类
public class Order {
    private int id;
    private BigInteger from_user;
    private String address;
    private Timestamp create_at;
    private Timestamp receive_at;
    private  BigInteger executor;
    private Timestamp close_at;
    private String close_state; //enum('finish','cancel')

    public Order(int id, BigInteger from_user, String address,
                 Timestamp create_at, Timestamp receive_at,
                 BigInteger executor, Timestamp close_at, String close_state) {
        this.id = id;
        this.from_user = from_user;
        this.address = address;
        this.create_at = create_at;
        this.receive_at = receive_at;
        this.executor = executor;
        this.close_at = close_at;
        this.close_state = close_state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigInteger getFrom_user() {
        return from_user;
    }

    public void setFrom_user(BigInteger from_user) {
        this.from_user = from_user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    public Timestamp getReceive_at() {
        return receive_at;
    }

    public void setReceive_at(Timestamp receive_at) {
        this.receive_at = receive_at;
    }

    public BigInteger getExecutor() {
        return executor;
    }

    public void setExecutor(BigInteger executor) {
        this.executor = executor;
    }

    public Timestamp getClose_at() {
        return close_at;
    }

    public void setClose_at(Timestamp close_at) {
        this.close_at = close_at;
    }

    public String getClose_state() {
        return close_state;
    }

    public void setClose_state(String close_state) {
        this.close_state = close_state;
    }
}
