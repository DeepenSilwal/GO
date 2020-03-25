package com.example.myapp;

public class Address {
    public String Standard_address;
    public Double bottom_lat;
    public Double left_log;
    public Double right_log;
    public Double top_lat;

    public Address(Double bottom_lat, Double left_log, Double right_log, String Standard_address, Double top_lat) {
        this.bottom_lat = bottom_lat;
        this.left_log = left_log;
        this.right_log = right_log;
        this.Standard_address = Standard_address;
        this.top_lat = top_lat;
    }



    public Address(){

    }



}
