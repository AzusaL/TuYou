package com.waibao.team.tuyou.dto;

import java.util.Date;

public class MessageDto {

    private String mid;            //消息id
    private String nickname;        //用户名
    private Date postMessageTime;        //发送时间
    private int type;        //0代表好友请求，1代表拼团请求
    private String imgUrl;    //用户头像

    private int flag;        //0为未读,1拒绝,2为同意
    private String other_id;    //其他信息用户，，主动去加的用户
    private String own_id;        //拥有该条信息的用户，，被加的用户
    private String own_gid;        //拥有该条信息的用户的团id(拼团时候才有数据)，，被请求的团
    private String other_gid;    //消息发送者的团id(拼团时候才有数据)，，主动请求的团

    public String getOwn_gid() {
        return own_gid;
    }

    public void setOwn_gid(String own_gid) {
        this.own_gid = own_gid;
    }

    public String getOther_gid() {
        return other_gid;
    }

    public void setOther_gid(String other_gid) {
        this.other_gid = other_gid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getPostMessageTime() {
        return postMessageTime;
    }

    public void setPostMessageTime(Date postMessageTime) {
        this.postMessageTime = postMessageTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOther_id() {
        return other_id;
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

    public String getOwn_id() {
        return own_id;
    }

    public void setOwn_id(String own_id) {
        this.own_id = own_id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
