package com.waibao.team.tuyou.dto;

public class UserCountDto {

	private String imgUrl;	//用户头像
	private int credit;		//信誉度
	private String uid;		//用户id
	private String nickname;	//用户名
	private String sex;			//性别
	private int age;			//年龄

	public UserCountDto() {
	}

	public UserCountDto(String imgUrl, int credit, String uid, String nickname, String sex, int age) {
		this.imgUrl = imgUrl;
		this.credit = credit;
		this.uid = uid;
		this.nickname = nickname;
		this.sex = sex;
		this.age = age;
	}

	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
