package com.example.yilaoapp.bean;

import java.math.BigInteger;
import java.util.List;

public class All_orders {
    String receive_at;
    String close_at;
    BigInteger phone;
    BigInteger executor;
    BigInteger from_user;
    Point_address destination;
    String emergency_level;
    String close_state;
    String create_at;
    int id;
    String detail;
    String type;//类型
    String category;
    String photos;
    String in_at;
    float reward;
    String protected_info;
    String out_at;
    int count;
    String id_photo;
    String name;//物品名称
    String id_name;



    public All_orders(String receive_at, String close_at, BigInteger phone, BigInteger executor,
                      BigInteger from_user, Point_address destination, String emergency_level,
                      String close_state, String create_at, int id, String detail, String type,
                      String category, String photos, String in_at, float reward, String protected_info,
                      String out_at, int count, String id_photo,String name,String id_name) {
        this.receive_at = receive_at;
        this.close_at = close_at;
        this.phone = phone;
        this.executor = executor;
        this.from_user = from_user;
        this.destination = destination;
        this.emergency_level = emergency_level;
        this.close_state = close_state;
        this.create_at = create_at;
        this.id = id;
        this.detail = detail;
        this.type = type;
        this.category = category;
        this.photos = photos;
        this.in_at = in_at;
        this.reward = reward;
        this.protected_info = protected_info;
        this.out_at = out_at;
        this.count = count;
        this.id_photo=id_photo;
        this.name=name;
        this.id_name=id_name;
    }

    //针对跑腿的初始化函数
    public All_orders(BigInteger phone, Point_address destination, String create_at,
                      int id, String detail,  float reward,
                      String protected_info, String id_photo,String id_name) {
        this.receive_at = "";
        this.close_at = "";
        this.phone = phone;   //发布的任务的联系电话
        this.executor = null; //接受任务的人的电话号码
        this.from_user = null;   //发布的任务的人的电话号码
        this.destination = destination;   //收获地址
        this.close_state = "";
        this.create_at = create_at;   //任务创建时间
        this.id = id;                //订单id
        this.detail = detail;        //订单详情
        this.type = type;            //订单种类
        this.category = "";
        this.photos = "";        //图片
        this.in_at = "";
        this.reward = reward;    //订单金额
        this.protected_info = protected_info;      //订单隐藏的信息
        this.out_at = "";
        this.count = 0;
        this.id_photo=id_photo;     //发布订单的人的头像
        this.id_name=id_name;
    }

    //针对代购和公告的初始化函数
    public All_orders(BigInteger phone, Point_address destination, String create_at,
                      int id, String detail,  float reward,
                      String protected_info, String category, String photos,String id_photo,String name,String id_name) {
        this.receive_at = "";
        this.close_at = "";
        this.phone = phone;   //发布的任务的联系电话
        this.executor = null; //接受任务的人的电话号码
        this.from_user = null;   //发布的任务的人的电话号码
        this.destination = destination;   //收获地址
        this.close_state = "";
        this.create_at = create_at;   //任务创建时间
        this.id = id;                //订单id
        this.detail = detail;        //订单详情
        this.type = type;            //订单种类
        this.category = category;     //公告的子分类
        this.photos = photos;        //图片
        this.in_at = "";
        this.reward = reward;    //订单金额
        this.protected_info = protected_info;      //订单隐藏的信息
        this.out_at = "";
        this.count = 0;
        this.id_photo=id_photo;     //发布订单的人的头像
        this.name=name;
        this.id_name=id_name;
    }


    public String getReceive_at() {
        return receive_at;
    }

    public void setReceive_at(String receive_at) {
        this.receive_at = receive_at;
    }

    public String getClose_at() {
        return close_at;
    }

    public void setClose_at(String close_at) {
        this.close_at = close_at;
    }

    public BigInteger getPhone() {
        return phone;
    }

    public void setPhone(BigInteger phone) {
        this.phone = phone;
    }

    public BigInteger getExecutor() {
        return executor;
    }

    public void setExecutor(BigInteger executor) {
        this.executor = executor;
    }

    public BigInteger getFrom_user() {
        return from_user;
    }

    public void setFrom_user(BigInteger from_user) {
        this.from_user = from_user;
    }

    public Point_address getDestination() {
        return destination;
    }

    public void setDestination(Point_address destination) {
        this.destination = destination;
    }

    public String getEmergency_level() {
        return emergency_level;
    }

    public void setEmergency_level(String emergency_level) {
        this.emergency_level = emergency_level;
    }

    public String getClose_state() {
        return close_state;
    }

    public void setClose_state(String close_state) {
        this.close_state = close_state;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getIn_at() {
        return in_at;
    }

    public void setIn_at(String in_at) {
        this.in_at = in_at;
    }

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public float getReward() {
        return reward;
    }

    public void setReward(float reward) {
        this.reward = reward;
    }

    public String getProtected_info() {
        return protected_info;
    }

    public void setProtected_info(String protected_info) {
        this.protected_info = protected_info;
    }

    public String getOut_at() {
        return out_at;
    }

    public void setOut_at(String out_at) {
        this.out_at = out_at;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }
}
