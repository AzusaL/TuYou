package com.waibao.team.tuyou.dto;

/**
 * Created by Azusa on 2016/6/4.
 */
public class SeatInfosBean {
    private String seat;
    private String seatPrice;
    private int remainNum;

    public SeatInfosBean() {
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(String seatPrice) {
        this.seatPrice = seatPrice;
    }

    public int getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(int remainNum) {
        this.remainNum = remainNum;
    }
}
