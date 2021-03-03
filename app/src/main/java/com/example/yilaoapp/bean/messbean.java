package com.example.yilaoapp.bean;

import android.graphics.Bitmap;

public class messbean {
    Bitmap photo;
    String name;
    String sex;
    String school;

    public messbean(Bitmap photo, String name, String sex, String school) {
        this.photo = photo;
        this.name = name;
        this.sex = sex;
        this.school = school;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
