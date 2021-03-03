package com.example.yilaoapp.user;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Set;

//用户实体类
public class User {
    @SerializedName("moblie")
    private BigInteger mobile;//bigint unsigned primary key,手机号
    @SerializedName("passwd")
    private String passwd;    //char(64),密码
    private String nickname;  //昵称
    private String sex;    //enum('male','female')
    private int portrait;  //头像
    private String address; //地址
    private Set<Double> mark;//评分
    private Timestamp create_at; //创建时间

}
