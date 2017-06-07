package com.upc.help_system.model;


import java.io.Serializable;

/**
 * Created by Liuyibo on 2017/5/9.
 */

public class Express implements Serializable {

    private int id;
    private String company;
    private String take_number;
    private String phone_number;
    private String name;
    private int volume;
    private int weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTake_number() {
        return take_number;
    }

    public void setTake_number(String take_number) {
        this.take_number = take_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}



