package com.waibao.team.tuyou.dto;

import java.util.List;

/**
 * Created by Azusa on 2016/6/4.
 */
public class TrainListBean {
    private String trainType;
    private String trainNo;
    private String from;
    private String to;
    private String startTime;
    private String endTime;
    private String duration;
    private List<SeatInfosBean> seatInfos;

    public TrainListBean() {
    }

    @Override
    public String toString() {
        return "TrainListBean{" +
                "trainType='" + trainType + '\'' +
                ", trainNo='" + trainNo + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration='" + duration + '\'' +
                ", seatInfos=" + seatInfos +
                '}';
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<SeatInfosBean> getSeatInfos() {
        return seatInfos;
    }

    public void setSeatInfos(List<SeatInfosBean> seatInfos) {
        this.seatInfos = seatInfos;
    }


}

