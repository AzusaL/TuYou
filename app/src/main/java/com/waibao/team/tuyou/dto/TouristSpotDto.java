package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/6/5.
 * 景点信息
 */
public class TouristSpotDto {

    private String title;
    private String grade;
    private String price_min;
    private String address;
    private String imgurl;
    private String url;

    public TouristSpotDto(String title, String grade, String price_min, String address, String imgurl, String url) {
        this.title = title;
        this.grade = grade;
        this.price_min = price_min;
        this.address = address;
        this.imgurl = imgurl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}



