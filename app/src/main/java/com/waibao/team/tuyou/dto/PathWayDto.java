package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/5/22.
 */
public class PathWayDto {

    private String address;
    private String time;

    public PathWayDto() {
    }

    public PathWayDto(String address, String time) {
        this.address = address;
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
