package com.example.yilaoapp.bean;

import java.math.BigInteger;
import java.sql.Timestamp;

//资源(文件)实体类
public class Resource {
    private int id;
    private String suffix; //文件后缀
    private byte[] data;  //图片→位图→字节数组流→字节数组
    private BigInteger from_user;
    private Timestamp create_at;
    private Timestamp delete_at;

    public Resource(int id, String suffix, byte[] data,
                    BigInteger from_user, Timestamp create_at,
                    Timestamp delete_at) {
        this.id = id;
        this.suffix = suffix;
        this.data = data;
        this.from_user = from_user;
        this.create_at = create_at;
        this.delete_at = delete_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public BigInteger getFrom_user() {
        return from_user;
    }

    public void setFrom_user(BigInteger from_user) {
        this.from_user = from_user;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    public Timestamp getDelete_at() {
        return delete_at;
    }

    public void setDelete_at(Timestamp delete_at) {
        this.delete_at = delete_at;
    }
}
