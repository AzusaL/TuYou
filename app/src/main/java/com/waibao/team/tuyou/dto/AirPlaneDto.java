package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/6/8.
 */
public class AirPlaneDto {
    private String name;   //	航班号
    private String date;  //	日期
    private String airmode;   //	机型
    private String dep;   //起飞机场
    private String arr;    //	降落机场
    private String company;  //航空公司
    private String status;    //	航班状态
    private String dep_time;   //	计划起飞时间
    private String arr_time;   //	计划到达时间
    private String fly_time;   //	飞行时间
    private String distance;   //飞行距离
    private String punctuality;            //准点率
    private String dep_temperature;  //	起飞机场天气
    private String arr_temperature;  //	到达机场天气
    private String etd;          //预计/实际起飞时间
    private String eta;           //	预计/实际到达时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAirmode() {
        return airmode;
    }

    public void setAirmode(String airmode) {
        this.airmode = airmode;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getArr() {
        return arr;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDep_time() {
        return dep_time;
    }

    public void setDep_time(String dep_time) {
        this.dep_time = dep_time;
    }

    public String getArr_time() {
        return arr_time;
    }

    public void setArr_time(String arr_time) {
        this.arr_time = arr_time;
    }

    public String getFly_time() {
        return fly_time;
    }

    public void setFly_time(String fly_time) {
        this.fly_time = fly_time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(String punctuality) {
        this.punctuality = punctuality;
    }

    public String getDep_temperature() {
        return dep_temperature;
    }

    public void setDep_temperature(String dep_temperature) {
        this.dep_temperature = dep_temperature;
    }

    public String getArr_temperature() {
        return arr_temperature;
    }

    public void setArr_temperature(String arr_temperature) {
        this.arr_temperature = arr_temperature;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }
}
