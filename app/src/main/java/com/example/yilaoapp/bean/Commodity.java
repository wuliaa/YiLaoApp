package com.example.yilaoapp.bean;

//商品类
public class Commodity {
    private int id;
    private String name; //商品名字
    private boolean on_offer;
    private double price; //decimal(16,2)
    private int sales_volume; //销量
    private int photo; //photo_id

    public Commodity(int id, String name, boolean on_offer,
                     double price, int sales_volume, int photo) {
        this.id = id;
        this.name = name;
        this.on_offer = on_offer;
        this.price = price;
        this.sales_volume = sales_volume;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOn_offer() {
        return on_offer;
    }

    public void setOn_offer(boolean on_offer) {
        this.on_offer = on_offer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(int sales_volume) {
        this.sales_volume = sales_volume;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
