package com.example.yilaoapp.bean;

import java.math.BigInteger;
import java.util.List;

public class All_orders {
    String receive_at;
    String close_at;
    BigInteger phone;
    BigInteger executor;
    BigInteger from_user;
    List<errand_task> tasks;
    Point_address destination;
    String emergency_level;
    String close_state;
    String create_at;
    int id;

    public All_orders(String receive_at, String close_at, BigInteger phone, BigInteger executor, BigInteger from_user, List<errand_task> tasks, Point_address destination, String emergency_level, String close_state, String create_at, int id) {
        this.receive_at = receive_at;
        this.close_at = close_at;
        this.phone = phone;
        this.executor = executor;
        this.from_user = from_user;
        this.tasks = tasks;
        this.destination = destination;
        this.emergency_level = emergency_level;
        this.close_state = close_state;
        this.create_at = create_at;
        this.id = id;
    }

    public String getReceive_at() {
        return receive_at;
    }

    public void setReceive_at(String receive_at) {
        this.receive_at = receive_at;
    }

    public String getClose_at() {
        return close_at;
    }

    public void setClose_at(String close_at) {
        this.close_at = close_at;
    }

    public BigInteger getPhone() {
        return phone;
    }

    public void setPhone(BigInteger phone) {
        this.phone = phone;
    }

    public BigInteger getExecutor() {
        return executor;
    }

    public void setExecutor(BigInteger executor) {
        this.executor = executor;
    }

    public BigInteger getFrom_user() {
        return from_user;
    }

    public void setFrom_user(BigInteger from_user) {
        this.from_user = from_user;
    }

    public List<errand_task> getTasks() {
        return tasks;
    }

    public void setTasks(List<errand_task> tasks) {
        this.tasks = tasks;
    }

    public Point_address getDestination() {
        return destination;
    }

    public void setDestination(Point_address destination) {
        this.destination = destination;
    }

    public String getEmergency_level() {
        return emergency_level;
    }

    public void setEmergency_level(String emergency_level) {
        this.emergency_level = emergency_level;
    }

    public String getClose_state() {
        return close_state;
    }

    public void setClose_state(String close_state) {
        this.close_state = close_state;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
