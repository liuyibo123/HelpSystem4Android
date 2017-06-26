package com.upc.help_system.utils;

/**
 * Created by Administrator on 2017/5/22.
 */

public class MyLoction {
    double Latitude;
    double Longitude;
    String Addr;
    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getAddr() {
        return Addr;
    }

    public void setAddr(String addr) {
        Addr = addr;
    }
}
