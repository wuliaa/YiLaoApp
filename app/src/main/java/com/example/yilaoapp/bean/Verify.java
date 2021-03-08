package com.example.yilaoapp.bean;

public class Verify {
    String appid;
    String mobile;
    String method;
    String path;

    public Verify(String appid, String mobile, String method, String base_url) {
        this.appid = appid;
        this.mobile = mobile;
        this.method = method;
        this.path = base_url;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBase_url() {
        return path;
    }

    public void setBase_url(String base_url) {
        this.path = base_url;
    }
}
