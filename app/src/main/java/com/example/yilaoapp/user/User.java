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

    public User(BigInteger mobile, String passwd, String nickname,
                String sex, int portrait, String address,
                Set<Double> mark, Timestamp create_at) {
        this.mobile = mobile;
        this.passwd = passwd;
        this.nickname = nickname;
        this.sex = sex;
        this.portrait = portrait;
        this.address = address;
        this.mark = mark;
        this.create_at = create_at;
    }

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
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

    public Set<Double> getMark() {
        return mark;
    }

    public void setMark(Set<Double> mark) {
        this.mark = mark;
    }
}
