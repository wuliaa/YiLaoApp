package com.example.yilaoapp.user;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class User {
    @SerializedName("moblie")
    private BigInteger mobile;//bigint unsigned primary key,手机号
    @SerializedName("passwd")
    private String passwd;    //char(64),密码

    public BigInteger getMobile() {
        return mobile;
    }

    public void setMobile(BigInteger mobile) {
        this.mobile = mobile;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
