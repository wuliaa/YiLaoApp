package com.example.yilaoapp.bean;

import android.graphics.Bitmap;

public class messbean {
    Bitmap id_photo;
    String id_name;
    String sex;
    String id_school;

    public messbean(Bitmap id_photo, String id_name, String sex, String id_school) {
        this.id_photo = id_photo;
        this.id_name = id_name;
        this.sex = sex;
        this.id_school = id_school;
    }

    public Bitmap getId_photo() {
        return id_photo;
    }

    public void setId_photo(Bitmap id_photo) {
        this.id_photo = id_photo;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId_school() {
        return id_school;
    }

    public void setId_school(String id_school) {
        this.id_school = id_school;
    }
}
