package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/6/4.
 */
public class HotelMsgDto {
    private String name;
    private String price;
    private String detail_url;
    private String phone;
    private String address;
    private String rating;

    private double lat;
    private double lng;

    public HotelMsgDto() {
    }

    public HotelMsgDto(String name, String price, String detail_url, String phone, String address, String rating, double lat, double lng) {
        this.name = name;
        this.price = price;
        this.detail_url = detail_url;
        this.phone = phone;
        this.address = address;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "HotelMsgDto{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", detail_url='" + detail_url + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
