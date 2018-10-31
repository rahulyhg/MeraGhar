package com.dudefinanceindia.meraghar;

public class DataForRecyclerView {

    private String address, date_time, price, property_image_1, type, key, carpet_area;

    DataForRecyclerView(){
        }

    public String getDate_time() {
        return date_time;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getProperty_image_1() {
        return property_image_1;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCarpet_area() {
        return carpet_area;
    }
}
