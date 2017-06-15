package com.upc.help_system.model;

/**
 * Created by asus on 2017/6/7.
 */

public class Buy {
    private int id;
    private String shop;
    private String goods;
    private float price;
    private String time;
    private int money_method;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMoney_method() {
        return money_method;
    }

    public void setMoney_method(int money_method) {
        this.money_method = money_method;
    }


}
