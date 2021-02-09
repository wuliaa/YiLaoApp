package com.example.yilaoapp.user;

public class yzmbean {
    String appid;
    String mobile;
    String method;
    String base_url;

    public yzmbean(String appid, String mobile, String method, String base_url) {
        this.appid = appid;
        this.mobile = mobile;
        this.method = method;
        this.base_url = base_url;
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
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }
}
