package com.waibao.team.tuyou.model;

import java.util.Date;

public class User {

    private String id;
    private String nickname;		//昵称(初始化为手机号码)
    private String loginName;		//登陆名（手机号码）
    private Date birthday;		//生日日期
    private int age;		//年龄
    private int status;		//0为单身，1为已婚
    private String identity;	//身份号码
    private String sex;		//性别
    private String hobby;	//爱好
    private String imgUrl;	//头像地址
    private int times;			//出行的次数
    private float km;		//出行的路程
    private String description;	//个性签名
    private String impress;  //伴有评价
    private int credit;			//信誉度
    private double lat;			//纬度
    private double lng;			//经度

    public User() {
    }

    public String getImpress() {
        return impress;
    }

    public void setImpress(String impress) {
        this.impress = impress;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", loginName='" + loginName + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", status=" + status +
                ", identity='" + identity + '\'' +
                ", sex='" + sex + '\'' +
                ", hobby='" + hobby + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", times=" + times +
                ", km=" + km +
                ", description='" + description + '\'' +
                ", impress='" + impress + '\'' +
                ", credit=" + credit +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
