package com.example.yilaoapp.bean;

import java.math.BigInteger;
import java.util.List;

public class errand_order {
    BigInteger phone;
    List<errand_task> tasks;

    public errand_order(BigInteger phone, List<errand_task> tasks) {
        this.phone = phone;
        this.tasks = tasks;
    }

    public BigInteger getPhone() {
        return phone;
    }

    public void setPhone(BigInteger phone) {
        this.phone = phone;
    }

    public List<errand_task> getTasks() {
        return tasks;
    }

    public void setTasks(List<errand_task> tasks) {
        this.tasks = tasks;
    }
}
