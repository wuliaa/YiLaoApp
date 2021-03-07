package com.example.yilaoapp.bean;

import java.math.BigInteger;

//用户实体类
public class User {

    private String nickname;  //昵称
    private Point_address default_location;
    private int portrait;  //头像
    private String id_school;
    private String id_photo;
    private String create_at;
    private String mark;
    private String sex;    //enum('male','female')
    //@SerializedName("moblie")
    private BigInteger mobile;//bigint unsigned primary key,手机号
    private String id_name;
    public User(String nickname, Point_address default_location, int portrait, String id_school, String id_photo, String create_at, String mark, String sex, BigInteger mobile, String id_name) {
        this.nickname = nickname;
        this.default_location = default_location;
        this.portrait = portrait;
        this.id_school = id_school;
        this.id_photo = id_photo;
        this.create_at = create_at;
        this.mark = mark;
        this.sex = sex;
        this.mobile = mobile;
        this.id_name = id_name;
    }


    public BigInteger getMobile() {
        return mobile;
    }

    public void setMobile(BigInteger mobile) {
        this.mobile = mobile;
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

    public Point_address getDefault_location() {
        return default_location;
    }

    public void setDefault_location(Point_address default_location) {
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

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
