package com.example.yilaoapp.bean;

import java.math.BigInteger;

public class errand_order {
    String phone;
    errand_task tasks;

    public errand_order(String phone, errand_task tasks) {
        this.phone = phone;
        this.tasks = tasks;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public errand_task getTasks() {
        return tasks;
    }

    public void setTasks(errand_task tasks) {
        this.tasks = tasks;
    }
}
