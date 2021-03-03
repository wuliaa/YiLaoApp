package com.example.yilaoapp.bean;

import com.example.yilaoapp.bean.Point;
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
    private Point default_location;
    private String create_at;
    private String id_name;
    private String id_school;
    private String photo;
    public User(BigInteger mobile, String passwd, String nickname, String sex, int portrait, Point default_location, String create_at, String id_name, String id_school, String photo) {
        this.mobile = mobile;
        this.passwd = passwd;
        this.nickname = nickname;
        this.sex = sex;
        this.portrait = portrait;
        this.default_location = default_location;
        this.create_at = create_at;
        this.id_name = id_name;
        this.id_school = id_school;
        this.photo = photo;
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

    public Point getDefault_location() {
        return default_location;
    }

    public void setDefault_location(Point default_location) {
        this.default_location = default_location;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getId_school() {
        return id_school;
    }

    public void setId_school(String id_school) {
        this.id_school = id_school;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
